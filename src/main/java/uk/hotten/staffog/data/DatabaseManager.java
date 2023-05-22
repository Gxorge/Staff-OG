package uk.hotten.staffog.data;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Console;

import java.sql.*;

public class DatabaseManager {

    @Getter private JavaPlugin plugin;
    @Getter private static DatabaseManager instance;

    private String host, username, password, database;
    private int port;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        host = plugin.getConfig().getString("sqlHost");
        username = plugin.getConfig().getString("sqlUsername");
        password = plugin.getConfig().getString("sqlPassword");
        database = plugin.getConfig().getString("sqlDatabase");
        port = plugin.getConfig().getInt("sqlPort");

        plugin.getServer().getPluginManager().registerEvents(new DataEventListener(), plugin);

        for (PunishType pt : PunishType.values()) checkPunishmentTable(pt.getTable());
        checkKickTable();
        checkNameHistoryTable();
        checkTaskTable();
        checkStatTable();
        checkWebTable();
        checkLinkCodeTable();
        checkStaffIpTable();
    }

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        synchronized (this) {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + database + "?userSSL=false", username, password);
        }
    }

    private void checkPunishmentTable(String table) {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, table, new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `" + table + "` (" +
                    " `id` bigint(20) NOT NULL AUTO_INCREMENT," +
                    " `uuid` varchar(36) NOT NULL," +
                    " `reason` varchar(2048) NOT NULL," +
                    " `by_uuid` varchar(36) DEFAULT NULL," +
                    " `by_name` varchar(128) DEFAULT NULL," +
                    " `removed_uuid` varchar(36) DEFAULT NULL," +
                    " `removed_name` varchar(128) DEFAULT NULL," +
                    " `removed_reason` varchar(2048) DEFAULT NULL," +
                    " `removed_time` bigint(20) DEFAULT NULL," +
                    " `time` bigint(20) NOT NULL," +
                    " `until` bigint(20) NOT NULL," +
                    " `active` bit(1) NOT NULL," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created '" + table + "' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check '" + table + "' table.");
            e.printStackTrace();
        }
    }

    private void checkKickTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_kick", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_kick` (" +
                    " `id` bigint(20) NOT NULL AUTO_INCREMENT," +
                    " `uuid` varchar(36) NOT NULL," +
                    " `reason` varchar(2048) NOT NULL," +
                    " `by_uuid` varchar(36) NOT NULL," +
                    " `by_name` varchar(128) NOT NULL," +
                    " `time` bigint(20) NOT NULL," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_kick' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_kick' table.");
            e.printStackTrace();
        }
    }

    private void checkNameHistoryTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_history", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_history` (" +
                    " `id` int NOT NULL AUTO_INCREMENT," +
                    " `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    " `name` varchar(16) NOT NULL," +
                    " `uuid` varchar(36) NOT NULL," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_history' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_history' table.");
            e.printStackTrace();
        }
    }

    private void checkTaskTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_task", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_task` (" +
                    " `id` int(11) NOT NULL AUTO_INCREMENT," +
                    " `task` varchar(2048) NOT NULL," +
                    " `data` varchar(2048) NOT NULL," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_task' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_task' table.");
            e.printStackTrace();
        }
    }

    private void checkStatTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_stat", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_stat` (" +
                    " `name` varchar(2048) NOT NULL," +
                    " `stat` varchar(2048) NOT NULL," +
                    " UNIQUE KEY `name` (`name`) USING HASH" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_stat' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_stat' table.");
            e.printStackTrace();
        }
    }

    private boolean doesStatIncludeEntry(String entry) {
        try (Connection connection = createConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_stat` WHERE `name`=?");
            statement.setString(1, entry);

            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setStatEntry(String entry, String stat) {
        try (Connection connection = createConnection()) {

            if (!doesStatIncludeEntry(entry)) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO `staffog_stat` (`name`, `stat`) VALUE (?, ?)");
                statement.setString(1, entry);
                statement.setString(2, stat);
                statement.executeUpdate();
            } else {
                PreparedStatement statement = connection.prepareStatement("UPDATE `staffog_stat` SET `stat`=? WHERE `name`=?");
                statement.setString(1, stat);
                statement.setString(2, entry);
                statement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkWebTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_web", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_web` (" +
                    " `id` int(11) NOT NULL AUTO_INCREMENT," +
                    " `username` varchar(128) NOT NULL," +
                    " `uuid` varchar(36) NOT NULL," +
                    " `password` varchar(2048) NOT NULL," +
                    " `active` bit(1) NOT NULL DEFAULT b'1'," +
                    " `admin` bit(1) NOT NULL," +
                    " PRIMARY KEY (`id`)," +
                    " UNIQUE KEY `username` (`username`,`uuid`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_web' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_web' table.");
            e.printStackTrace();
        }
    }

    private void checkLinkCodeTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_linkcode", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_linkcode` (" +
                    " `uuid` varchar(36) NOT NULL," +
                    " `code` varchar(5) NOT NULL," +
                    " `admin` bit(1) NOT NULL," +
                    " UNIQUE KEY `uuid` (`uuid`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_linkcode' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_linkcode' table.");
            e.printStackTrace();
        }
    }

    private void checkStaffIpTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_staffip", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_staffip` (" +
                    " `id` int(11) NOT NULL AUTO_INCREMENT," +
                    " `ip` varchar(15) NOT NULL," +
                    " `uuid` varchar(36) NOT NULL," +
                    " `initial` bit(1) NOT NULL," +
                    " `panel_acknowledged` bit(1) NOT NULL," +
                    " `panel_verified` bit(1) NOT NULL," +
                    " `game_verified` bit(1) NOT NULL," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_staffip' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_staffip' table.");
            e.printStackTrace();
        }
    }

    private void checkAuditTable() {
        try (Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, "staffog_staffip", new String[] {"TABLE"});

            if (resultSet.next()) {
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `staffog_audit` (" +
                    " `id` int(11) NOT NULL AUTO_INCREMENT," +
                    " `type` varchar(2048) NOT NULL," +
                    " `data` varchar(2048) NOT NULL," +
                    " PRIMARY KEY (`id`)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            statement.executeUpdate(sql);
            Console.info("Created 'staffog_audit' table, was missing.");

        } catch (Exception e) {
            Console.error("Failed to check 'staffog_audit' table.");
            e.printStackTrace();
        }
    }
}
