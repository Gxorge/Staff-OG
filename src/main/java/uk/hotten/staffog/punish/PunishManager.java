package uk.hotten.staffog.punish;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Console;
import uk.hotten.staffog.utils.Message;
import uk.hotten.staffog.utils.TimeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `staffog_history` WHERE `name`=? ORDER BY `id`");
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
                        entry.getPlayer().kickPlayer(ChatColor.RED + "You have been banned for " + duration + "\nReason: " + ChatColor.WHITE + entry.getReason());
                    }
                    case MUTE -> {
                        entry.getPlayer().sendMessage(Message.format(ChatColor.RED + "You have been muted for " + duration));
                        entry.getPlayer().sendMessage(Message.format(ChatColor.RED + "Reason: " + ChatColor.WHITE + entry.getReason()));
                    }
                }
            }

            Message.staffBroadcast(Message.format(entry.getName() + ChatColor.RED + " has been " +
                    entry.getType().getBroadcastMessage() + " for " +
                    ChatColor.WHITE + duration + ChatColor.RED +
                    " by " + ChatColor.WHITE + entry.getByName()));

        } catch (Exception e) {
            Console.error("Failed to create punishment.");
            e.printStackTrace();
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
            entry.setId(rs.getInt("id"));
            entry.setUuid(uuid);
            entry.setReason(rs.getString("reason"));
            entry.setByUuid(rs.getString("by_uuid"));
            entry.setByName(rs.getString("by_name"));
            entry.setTime(rs.getLong("time"));
            entry.setUntil(rs.getLong("until"));
            entry.setActive(rs.getInt("active") == 1);

            return entry;


        } catch (Exception e) {
            Console.error("Failed to get punishment info for " + uuid);
            e.printStackTrace();
            return null;
        }
    }

    public void removePunishment(int id, String removedUuid, String removedName) {

    }
}
