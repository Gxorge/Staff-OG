package uk.hotten.staffog.security;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.data.jooq.tables.records.StaffogLinkcodeRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogStaffipRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogWebRecord;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Console;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

import static uk.hotten.staffog.data.jooq.Tables.*;

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

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            StaffogStaffipRecord record = dsl.select(STAFFOG_STAFFIP.asterisk())
                    .from(STAFFOG_STAFFIP)
                    .where(STAFFOG_STAFFIP.UUID.eq(uuid.toString()))
                    .and(STAFFOG_STAFFIP.IP.eq(ip))
                    .fetchOneInto(STAFFOG_STAFFIP);

            if (record == null)
                return null;

            return new StaffIPInfo(
                    record.getId(),
                    UUID.fromString(record.getUuid()),
                    record.getIp(),
                    record.getInitial(),
                    record.getPanelAcknowledged(),
                    record.getPanelVerified(),
                    record.getGameVerified()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasUUIDGotIp(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            return dsl.fetchExists(dsl.selectFrom(STAFFOG_STAFFIP)
                    .where(STAFFOG_STAFFIP.UUID.eq(uuid.toString())));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createIpEntry(StaffIPInfo info) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            dsl.insertInto(STAFFOG_STAFFIP)
                    .set(STAFFOG_STAFFIP.IP, info.getIp())
                    .set(STAFFOG_STAFFIP.UUID, info.getUuid().toString())
                    .set(STAFFOG_STAFFIP.INITIAL, info.isInitial())
                    .set(STAFFOG_STAFFIP.PANEL_ACKNOWLEDGED, info.isPanelAcknowledged())
                    .set(STAFFOG_STAFFIP.PANEL_VERIFIED, info.isPanelVerified())
                    .set(STAFFOG_STAFFIP.GAME_VERIFIED, info.isGameVerified())
                    .execute();

        } catch (Exception e) {
            Console.error("Failed to create ip entry.");
            e.printStackTrace();
        }
    }

    public void updateIpEntry(StaffIPInfo info) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            dsl.update(STAFFOG_STAFFIP)
                    .set(STAFFOG_STAFFIP.IP, info.getIp())
                    .set(STAFFOG_STAFFIP.UUID, info.getUuid().toString())
                    .set(STAFFOG_STAFFIP.INITIAL, info.isInitial())
                    .set(STAFFOG_STAFFIP.PANEL_ACKNOWLEDGED, info.isPanelAcknowledged())
                    .set(STAFFOG_STAFFIP.PANEL_VERIFIED, info.isPanelVerified())
                    .set(STAFFOG_STAFFIP.GAME_VERIFIED, info.isGameVerified())
                    .where(STAFFOG_STAFFIP.ID.eq(info.getId()))
                    .execute();

        } catch (Exception e) {
            Console.error("Failed to update ip entry.");
            e.printStackTrace();
        }
    }

    private boolean doesLinkCodeExist(String code) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            return dsl.fetchExists(dsl.selectFrom(STAFFOG_LINKCODE)
                    .where(STAFFOG_LINKCODE.CODE.eq(code)));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String doesPlayerHaveLinkCode(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            StaffogLinkcodeRecord record = dsl.select(STAFFOG_LINKCODE.asterisk())
                    .from(STAFFOG_LINKCODE)
                    .where(STAFFOG_LINKCODE.UUID.eq(uuid.toString()))
                    .fetchOneInto(STAFFOG_LINKCODE);

            if (record == null) {
                return null;
            }

            return record.getCode();

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

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            dsl.insertInto(STAFFOG_LINKCODE)
                    .set(STAFFOG_LINKCODE.UUID, uuid.toString())
                    .set(STAFFOG_LINKCODE.CODE, code)
                    .set(STAFFOG_LINKCODE.ADMIN, admin)
                    .execute();

        } catch (Exception e) {
            Console.error("Failed to create link code.");
            e.printStackTrace();
            return null;
        }

        return code;
    }

    public void checkAndDeactivateStaffAccount(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
