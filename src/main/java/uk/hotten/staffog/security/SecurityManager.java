package uk.hotten.staffog.security;

import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_LINKCODE;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_STAFFIP;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_WEB;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import lombok.Getter;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.data.jooq.tables.records.StaffogLinkcodeRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogStaffipRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogWebRecord;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Console;

public class SecurityManager {

	@Getter private static SecurityManager instance;
	@Getter private HashMap<UUID, StaffIPInfo> staffIPInfos;

	public SecurityManager(JavaPlugin plugin) {
		instance = this;
		staffIPInfos = new HashMap<>();

		plugin.getServer().getPluginManager().registerEvents(new SecurityEventListener(), plugin);
	}

	public StaffIPInfo isIpRecognised(UUID uuid, String ip) {
		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			StaffogStaffipRecord record = dsl.select(STAFFOG_STAFFIP.asterisk())
					.from(STAFFOG_STAFFIP)
					.where(STAFFOG_STAFFIP.UUID.eq(uuid.toString()))
					.and(STAFFOG_STAFFIP.IP.eq(ip))
					.fetchOneInto(STAFFOG_STAFFIP);

			if (record == null) {

				return null;

			}

			return new StaffIPInfo(
					record.getId(),
					UUID.fromString(record.getUuid()),
					record.getIp(),
					record.getInitial(),
					record.getPanelAcknowledged(),
					record.getPanelVerified(),
					record.getGameVerified()
					);
		}
		catch (Exception error) {
			error.printStackTrace();
			return null;
		}
	}

	public boolean isUserAccountCreated(UUID id, String ip) {

		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			StaffogStaffipRecord record = dsl.select(STAFFOG_STAFFIP.asterisk())
					.from(STAFFOG_STAFFIP)
					.where(STAFFOG_STAFFIP.UUID.eq(id.toString()))
					.and(STAFFOG_STAFFIP.IP.eq(ip))
					.fetchOneInto(STAFFOG_STAFFIP);

			if (record == null) {

				return false;

			}
			else {

				return record.getPanelVerified();

			}

		}
		catch (Exception e) {

			e.printStackTrace();

			return false;

		}

	}

	public boolean hasUUIDGotIp(UUID uuid) {
		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			return dsl.fetchExists(dsl.selectFrom(STAFFOG_STAFFIP)
					.where(STAFFOG_STAFFIP.UUID.eq(uuid.toString())));

		}
		catch (Exception error) {
			error.printStackTrace();
			return false;
		}
	}

	public void createIpEntry(StaffIPInfo info) {
		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			dsl.insertInto(STAFFOG_STAFFIP)
			.set(STAFFOG_STAFFIP.IP, info.getIp())
			.set(STAFFOG_STAFFIP.UUID, info.getUuid().toString())
			.set(STAFFOG_STAFFIP.INITIAL, info.isInitial())
			.set(STAFFOG_STAFFIP.PANEL_ACKNOWLEDGED, info.isPanelAcknowledged())
			.set(STAFFOG_STAFFIP.PANEL_VERIFIED, info.isPanelVerified())
			.set(STAFFOG_STAFFIP.GAME_VERIFIED, info.isGameVerified())
			.execute();

		}
		catch (Exception error) {
			Console.error("Failed to create ip entry.");
			error.printStackTrace();
		}
	}

	public void updateIpEntry(StaffIPInfo info) {
		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			dsl.update(STAFFOG_STAFFIP)
			.set(STAFFOG_STAFFIP.IP, info.getIp())
			.set(STAFFOG_STAFFIP.UUID, info.getUuid().toString())
			.set(STAFFOG_STAFFIP.INITIAL, info.isInitial())
			.set(STAFFOG_STAFFIP.PANEL_ACKNOWLEDGED, info.isPanelAcknowledged())
			.set(STAFFOG_STAFFIP.PANEL_VERIFIED, info.isPanelVerified())
			.set(STAFFOG_STAFFIP.GAME_VERIFIED, info.isGameVerified())
			.where(STAFFOG_STAFFIP.ID.eq(info.getId()))
			.execute();

		}
		catch (Exception error) {
			Console.error("Failed to update ip entry.");
			error.printStackTrace();
		}
	}

	private boolean doesLinkCodeExist(String code) {
		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			return dsl.fetchExists(dsl.selectFrom(STAFFOG_LINKCODE)
					.where(STAFFOG_LINKCODE.CODE.eq(code)));

		}
		catch (Exception error) {
			error.printStackTrace();
			return false;
		}
	}

	public String doesPlayerHaveLinkCode(UUID uuid) {
		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			StaffogLinkcodeRecord record = dsl.select(STAFFOG_LINKCODE.asterisk())
					.from(STAFFOG_LINKCODE)
					.where(STAFFOG_LINKCODE.UUID.eq(uuid.toString()))
					.fetchOneInto(STAFFOG_LINKCODE);

			if (record == null) {

				return null;

			}

			return record.getCode();

		}
		catch (Exception error) {
			error.printStackTrace();
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

			if (! doesLinkCodeExist(sb.toString())) {

				code = sb.toString();

			}

		}

		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);
			dsl.insertInto(STAFFOG_LINKCODE)
			.set(STAFFOG_LINKCODE.UUID, uuid.toString())
			.set(STAFFOG_LINKCODE.CODE, code)
			.set(STAFFOG_LINKCODE.ADMIN, admin)
			.execute();

		}
		catch (Exception error) {
			Console.error("Failed to create link code.");
			error.printStackTrace();
			return null;
		}

		return code;

	}

	public void checkAndDeactivateStaffAccount(UUID uuid) {

		try (Connection connection = DatabaseManager.getInstance().createConnection()) {

			DSLContext dsl = DSL.using(connection);

			StaffogWebRecord record = dsl.select(STAFFOG_WEB.asterisk())
					.from(STAFFOG_WEB)
					.where(STAFFOG_WEB.UUID.eq(uuid.toString()))
					.fetchOneInto(STAFFOG_WEB);

			if (record == null)
				return;

			dsl.update(STAFFOG_WEB)
			.set(STAFFOG_WEB.ACTIVE, false)
			.where(STAFFOG_WEB.UUID.eq(uuid.toString()))
			.execute();

		}
		catch (Exception error) {

			error.printStackTrace();

		}

	}

}