package uk.hotten.staffog.punish;

import lombok.Getter;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.utils.Console;
import uk.hotten.staffog.utils.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class PunishmentManager {

    @Getter private static PunishmentManager instance;

    public PunishmentManager() {
        instance = this;
    }

    public void checkNameToUuid(String name, UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `staffog_history` WHERE `uuid`=?, `name`=?");
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
            statement.setInt(5, entry.getTime());
            statement.setInt(6, entry.getUntil());
            statement.setString(7, entry.isActive() ? "1" : "0");

            statement.executeUpdate();

            Message.broadcast(Message.format());

        } catch (Exception e) {
            Console.error("Failed to create punishment.");
            e.printStackTrace();
        }
    }

}
