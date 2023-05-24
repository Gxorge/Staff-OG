package uk.hotten.staffog.punish;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.data.KickPunishEntry;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.utils.Console;
import uk.hotten.staffog.utils.Message;
import uk.hotten.staffog.utils.TimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

public class PunishManager {

    private JavaPlugin plugin;
    @Getter private static PunishManager instance;

    public PunishManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        plugin.getServer().getPluginManager().registerEvents(new PunishEventListener(), plugin);
    }

    public void checkNameToUuid(String name, UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `staffog_history` WHERE `uuid`=? AND `name`=?");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            ResultSet rs = statement.executeQuery();

            boolean result = rs.next();

            if (result)
                return;

            statement = connection.prepareStatement("INSERT INTO `staffog_history` (`uuid`, `name`) VALUE (?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.executeUpdate();

        } catch (Exception e) {
            Console.error("Failed to check username to uuid.");
            e.printStackTrace();
        }
    }

    public String getNameFromUUID(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT `name` FROM `staffog_history` WHERE `uuid`=? ORDER BY `id` DESC LIMIT 1");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            } else {
                return null;
            }

        } catch (Exception e) {
            Console.error("Failed to get name from uuid. " + uuid.toString());
            e.printStackTrace();
            return null;
        }
    }

    public UUID getUUIDFromName(String name) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `staffog_history` WHERE `name`=? ORDER BY `id` DESC LIMIT 1");
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return UUID.fromString(rs.getString("uuid"));
            } else {
                return null;
            }

        } catch (Exception e) {
            Console.error("Failed to get name from name. " + name);
            e.printStackTrace();
            return null;
        }
    }

    public void newPunishment(PunishEntry entry) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `" + entry.getType().getTable() + "` (`uuid`, `reason`, `by_uuid`, `by_name`, `time`, `until`, `active`) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, entry.getUuid().toString());
            statement.setString(2, entry.getReason());
            statement.setString(3, entry.getByUuid().toString());
            statement.setString(4, entry.getByName());
            statement.setLong(5, entry.getTime());
            statement.setLong(6, entry.getUntil());
            statement.setInt(7, entry.isActive() ? 1 : 0);

            statement.executeUpdate();

            String duration = TimeUtils.formatMillisecondTime(entry.calculateDuration());
            if (entry.getPlayer() != null && entry.getPlayer().isOnline()) {
                switch (entry.getType()) {
                    case BAN -> {
                        entry.getPlayer().kickPlayer(ChatColor.RED + "You have been banned for:\n" +
                                ChatColor.WHITE + entry.getReason() + "\n\n" +
                                ChatColor.RED + "Your ban " + (entry.getUntil() == -1 ? "does not expire." : "will expire in: \n"
                                + ChatColor.WHITE + duration));
                    }
                    case MUTE -> {
                        entry.getPlayer().sendMessage(Message.format(ChatColor.RED + "You have been muted for " + ChatColor.WHITE + entry.getReason()));
                        entry.getPlayer().sendMessage(Message.format(ChatColor.RED + "Your mute " + (entry.getUntil() == -1 ? "does not expire." : "will expire in: \n"
                                + ChatColor.WHITE + duration)));
                    }
                }
            }

            Bukkit.getServer().broadcastMessage(Message.formatNotification(
                    entry.getType().toString(),
                    "New " + entry.getType().toString().toLowerCase()
                            + " on " + entry.getName() + " for "
                            + duration + " by " + entry.getByName()));

            SecurityManager.getInstance().checkAndDeactivateStaffAccount(entry.getUuid());

        } catch (Exception e) {
            Console.error("Failed to create punishment.");
            e.printStackTrace();
        }
    }

    public PunishEntry getPunishment(PunishType type, int id) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + type.getTable() + "` WHERE `id`=?");
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            PunishEntry entry = new PunishEntry(type);
            entry.setId(rs.getLong("id"));
            entry.setUuid(UUID.fromString(rs.getString("uuid")));
            entry.setName(getNameFromUUID(entry.getUuid()));
            entry.setReason(rs.getString("reason"));
            entry.setByUuid(rs.getString("by_uuid"));
            entry.setByName(rs.getString("by_name"));
            entry.setTime(rs.getLong("time"));
            entry.setUntil(rs.getLong("until"));
            entry.setActive(rs.getInt("active") == 1);
            if (!entry.isActive()) {
                entry.setRemovedReason(rs.getString("removed_uuid"));
                entry.setRemovedName(rs.getString("removed_name"));
                entry.setRemovedTime(rs.getLong("removed_time"));
                entry.setRemovedReason(rs.getString("removed_reason"));
            }

            if (entry.checkDurationOver()) {
                expirePunishment(entry);
                return null;
            }

            return entry;


        } catch (Exception e) {
            Console.error("Failed to get punishment info for id " + id);
            e.printStackTrace();
            return null;
        }
    }

    public PunishEntry checkActivePunishment(PunishType type, UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `" + type.getTable() + "` WHERE `uuid`=? AND `active`=1");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            PunishEntry entry = new PunishEntry(type);
            entry.setId(rs.getLong("id"));
            entry.setUuid(uuid);
            entry.setReason(rs.getString("reason"));
            entry.setByUuid(rs.getString("by_uuid"));
            entry.setByName(rs.getString("by_name"));
            entry.setTime(rs.getLong("time"));
            entry.setUntil(rs.getLong("until"));
            entry.setActive(rs.getInt("active") == 1);

            if (entry.checkDurationOver()) {
                expirePunishment(entry);
                return null;
            }

            return entry;


        } catch (Exception e) {
            Console.error("Failed to get punishment info for " + uuid);
            e.printStackTrace();
            return null;
        }
    }

    public void expirePunishment(PunishEntry entry) {
        entry.setRemovedUuid("Expired");
        entry.setRemovedName("Expired");
        entry.setRemovedReason("Punishment Expired");
        entry.setActive(false);
        removePunishment(entry);
    }

    public void removePunishment(PunishEntry entry) {
        entry.setActive(false);
        entry.setRemovedTime(System.currentTimeMillis());

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            if (entry.getName() == null)
                entry.setName(getNameFromUUID(entry.getUuid()));

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `" + entry.getType().getTable() + "` SET `removed_uuid`=?, `removed_name`=?, `removed_reason`=?, `removed_time`=?, `active`=? WHERE `id`=?"
            );

            statement.setString(1, entry.getRemovedUuid());
            statement.setString(2, entry.getRemovedName());
            statement.setString(3, entry.getRemovedReason());
            statement.setLong(4, entry.getRemovedTime());
            statement.setInt(5, 0);
            statement.setLong(6, entry.getId());

            statement.executeUpdate();

            if (entry.getType() == PunishType.MUTE && Bukkit.getServer().getPlayer(entry.getUuid()) != null) {
                Bukkit.getServer().getPlayer(entry.getUuid()).sendMessage(Message.format(ChatColor.GREEN + "You have been unmuted."));
            }

            Message.staffBroadcast(Message.formatNotification("UN" + entry.getType(), entry.getName() + " has been un" + entry.getType().getBroadcastMessage() + " by " + entry.getRemovedName()));

        } catch (Exception e) {
            Console.error("Failed to remove punishment.");
            e.printStackTrace();
        }
    }

    public void visualRemovePunishment(PunishEntry entry) {
        if (entry.getType() == PunishType.MUTE && Bukkit.getServer().getPlayer(entry.getUuid()) != null) {
            Bukkit.getServer().getPlayer(entry.getUuid()).sendMessage(Message.format(ChatColor.GREEN + "You have been unmuted."));
        }

        Message.staffBroadcast(Message.formatNotification("UN" + entry.getType(), entry.getName() + " has been un" + entry.getType().getBroadcastMessage() + " by " + entry.getRemovedName()));
    }

    public void newKickPunishment(KickPunishEntry entry) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `staffog_kick` (`uuid`, `reason`, `by_uuid`, `by_name`, `time`) VALUES (?, ?, ?, ?, ?)"
            );
            statement.setString(1, entry.getUuid().toString());
            statement.setString(2, entry.getReason());
            statement.setString(3, entry.getByUuid().toString());
            statement.setString(4, entry.getByName());
            statement.setLong(5, entry.getTime());

            statement.executeUpdate();

            if (entry.getPlayer() != null && entry.getPlayer().isOnline()) {
                entry.getPlayer().kickPlayer(ChatColor.RED + "You have been kicked for:\n" +
                        ChatColor.WHITE + entry.getReason() + "\n\n" +
                        ChatColor.RED + "Please read our rules when you rejoin by running /rules");
            }

            Bukkit.getServer().broadcastMessage(Message.formatNotification(
                    "KICK", entry.getName() + " has been kicked by " + entry.getByName()));

        } catch (Exception e) {
            Console.error("Failed to create kick punishment.");
            e.printStackTrace();
        }
    }
}
