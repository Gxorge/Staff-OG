package uk.hotten.staffog.data;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Console;

import java.sql.*;

public class DatabaseManager {

    private JavaPlugin plugin;
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

        for (PunishType pt : PunishType.values()) checkPunishmentTable(pt.getTable());
        checkKickTable();
        checkNameHistoryTable();
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
                Console.info(table + " exists.");
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
            Console.error("Failed to check " + table + " table.");
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
            Console.error("Failed to check staffog_kick table.");
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
            Console.error("Failed to check staffog_history table.");
            e.printStackTrace();
        }
    }
}
