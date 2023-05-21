package uk.hotten.staffog.security;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

public class SecurityManager {

    @Getter private static SecurityManager instance;
    private JavaPlugin plugin;

    @Getter private HashMap<UUID, StaffIPInfo> staffIPInfos;

    public SecurityManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        staffIPInfos = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(new SecurityEventListener(), plugin);
    }

    public StaffIPInfo isIpRecognised(UUID uuid, String ip) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_staffip` WHERE `uuid`=? AND `ip`=?");
            statement.setString(1, uuid.toString());
            statement.setString(2, ip);

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            return new StaffIPInfo(
                    rs.getInt("id"),
                    UUID.fromString(rs.getString("uuid")),
                    rs.getString("ip"),
                    rs.getInt("initial") == 1,
                    rs.getInt("panel_acknowledged") == 1,
                    rs.getInt("panel_verified") == 1,
                    rs.getInt("game_verified") == 1
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasUUIDGotIp(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_staffip` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createIpEntry(StaffIPInfo info) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `staffog_staffip` (`ip`, `uuid`, `initial`, `panel_acknowledged`, `panel_verified`, `game_verified`) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, info.getIp());
            statement.setString(2, info.getUuid().toString());
            statement.setInt(3, (info.isInitial() ? 1 : 0));
            statement.setInt(4, (info.isPanelAcknowledged() ? 1 : 0));
            statement.setInt(5, (info.isPanelVerified() ? 1 : 0));
            statement.setInt(6, (info.isGameVerified() ? 1 : 0));


            statement.executeUpdate();

        } catch (Exception e) {
            Console.error("Failed to create ip entry.");
            e.printStackTrace();
        }
    }

    public void updateIpEntry(StaffIPInfo info) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `staffog_staffip` SET `ip`=?, `uuid`=?, `initial`=?, `panel_acknowledged`=?, `panel_verified`=?, `game_verified`=? WHERE `id`=?"
            );
            statement.setString(1, info.getIp());
            statement.setString(2, info.getUuid().toString());
            statement.setInt(3, (info.isInitial() ? 1 : 0));
            statement.setInt(4, (info.isPanelAcknowledged() ? 1 : 0));
            statement.setInt(5, (info.isPanelVerified() ? 1 : 0));
            statement.setInt(6, (info.isGameVerified() ? 1 : 0));
            statement.setInt(7, info.getId());


            statement.executeUpdate();

        } catch (Exception e) {
            Console.error("Failed to update ip entry.");
            e.printStackTrace();
        }
    }

    private boolean doesLinkCodeExist(String code) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_linkcode` WHERE `code`=?");
            statement.setString(1, code);

            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String doesPlayerHaveLinkCode(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_linkcode` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return null;

            return rs.getString("code");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createLinkCode(UUID uuid, boolean admin) {
        String code = "";
        String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder();
        while (code.equals("")) {
            for (int i = 0; i < 5; i++) {
                int index = (int)(possibleChars.length() * Math.random());

                // add Character one by one in end of sb
                sb.append(possibleChars.charAt(index));
            }

            if (!doesLinkCodeExist(sb.toString()))
                code = sb.toString();
        }

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `staffog_linkcode` (`uuid`, `code`, `admin`) VALUES (?, ?, ?)"
            );
            statement.setString(1, uuid.toString());
            statement.setString(2, code);
            statement.setInt(3, (admin ? 1 : 0));


            statement.executeUpdate();

        } catch (Exception e) {
            Console.error("Failed to create link code.");
            e.printStackTrace();
            return null;
        }

        return code;
    }

    public void checkAndDeactivateStaffAccount(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_web` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();

            if (!rs.next())
                return;

            PreparedStatement updateStatement = connection.prepareStatement("UPDATE `staffog_web` SET `active`=0 WHERE `uuid`=? ");
            updateStatement.setString(1, uuid.toString());
            updateStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
