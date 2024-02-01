package uk.hotten.staffog.data;

import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_STAT;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import lombok.Getter;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Console;

public class DatabaseManager {

    @Getter private static JavaPlugin plugin;
    @Getter private static DatabaseManager instance;

    private String driver, driverUrl, host, username, password, database;
    private int port;

	public DatabaseManager(JavaPlugin plugin) {
		DatabaseManager.plugin = plugin;
		instance = this;

		String sqlType = plugin.getConfig().getString("sqlType");
		if (sqlType == null) {
			Console.error("NO SQL TYPE SELECTED. DEFAULTING TO MYSQL.");
			driver = "com.mysql.jdbc.Driver";
			driverUrl = "mysql";
		} else {
			if (sqlType.equalsIgnoreCase("mysql")) {
				driver = "com.mysql.jdbc.Driver";
				driverUrl = "mysql";
			} else if (sqlType.equals("mariadb")) {
				driver = "org.mariadb.jdbc.Driver";
				driverUrl = "mariadb";
			} else {
				Console.error("INVALID SQL TYPE, PLEASE USE EITHER MYSQL OR MARIADB. DEFAULTING TO MYSQL.");
				driver = "com.mysql.jdbc.Driver";
				driverUrl = "mysql";
			}
		}

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
		checkAuditTable();
		checkAppealTable();
		checkReportTable();
		checkChatReportTable();
	}

	public Connection createConnection() throws SQLException, ClassNotFoundException {
		synchronized (this) {
			Class.forName(driver);
			return DriverManager.getConnection("jdbc:" + driverUrl + "://" + host + ":" + port + "/" + database + "?userSSL=false", username, password);
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

			DSLContext dsl = DSL.using(connection);
			return dsl.fetchExists(dsl.selectFrom(STAFFOG_STAT)
					.where(STAFFOG_STAT.NAME.eq(entry)));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setStatEntry(String entry, String stat) {
		try (Connection connection = createConnection()) {

			DSLContext dsl = DSL.using(connection);
			if (!doesStatIncludeEntry(entry)) {
				dsl.insertInto(STAFFOG_STAT)
				.set(STAFFOG_STAT.NAME, entry)
				.set(STAFFOG_STAT.STAT, stat)
				.execute();
			} else {
				dsl.update(STAFFOG_STAT)
				.set(STAFFOG_STAT.STAT, stat)
				.where(STAFFOG_STAT.NAME.eq(entry))
				.execute();
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
			ResultSet resultSet = meta.getTables(null, null, "staffog_audit", new String[] {"TABLE"});

			if (resultSet.next()) {
				return;
			}

			// Table does not exist
			Statement statement = connection.createStatement();

			String sql = "CREATE TABLE `staffog_audit` (" +
					" `id` int(11) NOT NULL AUTO_INCREMENT," +
					" `type` varchar(2048) NOT NULL," +
					" `data` varchar(2048) NOT NULL," +
					" `time` bigint(20) NOT NULL," +
					" PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

			statement.executeUpdate(sql);
			Console.info("Created 'staffog_audit' table, was missing.");

		} catch (Exception e) {
			Console.error("Failed to check 'staffog_audit' table.");
			e.printStackTrace();
		}
	}

	private void checkAppealTable() {
		try (Connection connection = createConnection()) {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet resultSet = meta.getTables(null, null, "staffog_appeal", new String[] {"TABLE"});

			if (resultSet.next()) {
				return;
			}

			// Table does not exist
			Statement statement = connection.createStatement();

			String sql = "CREATE TABLE `staffog_appeal` (" +
					" `id` int NOT NULL AUTO_INCREMENT," +
					" `uuid` varchar(36) COLLATE utf8mb4_general_ci NOT NULL," +
					" `time` bigint NOT NULL," +
					" `type` varchar(128) COLLATE utf8mb4_general_ci NOT NULL," +
					" `pid` int NOT NULL," +
					" `reason` varchar(2048) COLLATE utf8mb4_general_ci NOT NULL," +
					" `evidence` varchar(2048) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '[]'," +
					" `open` bit(1) NOT NULL DEFAULT b'1'," +
					" `assigned` varchar(36) COLLATE utf8mb4_general_ci DEFAULT NULL," +
					" `verdict` bit(1) DEFAULT NULL," +
					" `verdict_time` bigint DEFAULT NULL," +
					" `comment` varchar(2048) COLLATE utf8mb4_general_ci DEFAULT NULL," +
					" PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

			statement.executeUpdate(sql);
			Console.info("Created 'staffog_appeal' table, was missing.");

		} catch (Exception e) {
			Console.error("Failed to check 'staffog_appeal' table.");
			e.printStackTrace();
		}
	}

	private void checkReportTable() {
		try (Connection connection = createConnection()) {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet resultSet = meta.getTables(null, null, "staffog_report", new String[] {"TABLE"});

			if (resultSet.next()) {
				return;
			}

			// Table does not exist
			Statement statement = connection.createStatement();

			String sql = "CREATE TABLE `staffog_report` (" +
					" `id` int NOT NULL AUTO_INCREMENT," +
					" `uuid` varchar(36) COLLATE utf8mb4_general_ci NOT NULL," +
					" `by_uuid` varchar(36) COLLATE utf8mb4_general_ci NOT NULL," +
					" `time` bigint NOT NULL," +
					" `type` varchar(128) COLLATE utf8mb4_general_ci NOT NULL," +
					" `reason` varchar(2048) COLLATE utf8mb4_general_ci NOT NULL," +
					" `evidence` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '[]'," +
					" `crid` int DEFAULT NULL," +
					" `open` bit(1) NOT NULL DEFAULT b'1'," +
					" `assigned` varchar(36) COLLATE utf8mb4_general_ci DEFAULT NULL," +
					" `verdict` bit(1) DEFAULT NULL," +
					" `verdict_time` bigint DEFAULT NULL," +
					" `comment` varchar(2048) COLLATE utf8mb4_general_ci DEFAULT NULL," +
					" PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

			statement.executeUpdate(sql);
			Console.info("Created 'staffog_report' table, was missing.");

		} catch (Exception e) {
			Console.error("Failed to check 'staffog_report' table.");
			e.printStackTrace();
		}
	}

	private void checkChatReportTable() {
		try (Connection connection = createConnection()) {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet resultSet = meta.getTables(null, null, "staffog_chatreport", new String[] {"TABLE"});

			if (resultSet.next()) {
				return;
			}

			// Table does not exist
			Statement statement = connection.createStatement();

			String sql = "CREATE TABLE `staffog_chatreport` (" +
					" `id` int(11) NOT NULL AUTO_INCREMENT," +
					" `uuid` varchar(36) NOT NULL," +
					" `by_uuid` varchar(36) NOT NULL," +
					" `time` bigint(20) NOT NULL," +
					" `messages` varchar(2048) NOT NULL," +
					" PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

			statement.executeUpdate(sql);
			Console.info("Created 'staffog_chatreport' table, was missing.");

		}
		catch (Exception error) {
			Console.error("Failed to check 'staffog_chatreport' table.");
			error.printStackTrace();
		}
	}
}