package uk.hotten.staffog.data;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
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

        checkPunishmentTable("staffog_bans");
        checkPunishmentTable("staffog_mutes");
    }

    public Connection createConnection() throws SQLException, ClassNotFoundException {
        synchronized (this) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?userSSL=false", username, password);
        }
    }

    private void checkPunishmentTable(String table) {
        try(Connection connection = createConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, table, new String[] {"TABLE"});

            if (resultSet.next()) {
                Console.info(table + " exists.");
                return;
            }

            // Table does not exist
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE `" + table + "` (" +
                    " `id` bigint NOT NULL AUTO_INCREMENT," +
                    " `uuid` varchar(36) NOT NULL," +
                    " `reason` varchar(2048) NOT NULL," +
                    " `by_uuid` varchar(36) DEFAULT NULL," +
                    " `by_name` varchar(128) DEFAULT NULL," +
                    " `removed_uuid` varchar(36) DEFAULT NULL," +
                    " `removed_name` varchar(128) DEFAULT NULL," +
                    " `time` bigint NOT NULL," +
                    " `until` bigint NOT NULL," +
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

}
