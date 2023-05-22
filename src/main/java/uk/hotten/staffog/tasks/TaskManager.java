package uk.hotten.staffog.tasks;

import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.tasks.data.TaskEntry;
import uk.hotten.staffog.tasks.data.UnpunishTask;
import uk.hotten.staffog.utils.Console;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TaskManager {

    @Getter private JavaPlugin plugin;

    public TaskManager(JavaPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (TaskEntry entry : checkForTasks()) {
                switch (entry.getTask()) {
                    case "unpunish": {
                        processUnpunish(entry);
                        deleteTask(entry.getId());
                        break;
                    }
                }
            }
        }, 0, 200);
    }

    private ArrayList<TaskEntry> checkForTasks() {
        try (Connection connection = DatabaseManager.getInstance().createConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `staffog_task`;");

            ResultSet rs = statement.executeQuery();

            ArrayList<TaskEntry> toReturn = new ArrayList<>();

            while (rs.next()) {
                TaskEntry entry = new TaskEntry();
                entry.setId(rs.getInt("id"));
                entry.setTask(rs.getString("task"));
                entry.setData(rs.getString("data"));
                toReturn.add(entry);
            }

            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void processUnpunish(TaskEntry entry) {
        if (!entry.getTask().equals("unpunish"))
            return;

        Gson gson = new Gson();
        UnpunishTask task = gson.fromJson(entry.getData(), UnpunishTask.class);
        PunishEntry punishEntry = PunishManager.getInstance().getPunishment(task.getType(), task.getId());
        punishEntry.setRemovedUuid(task.getRemovedUuid().toString());
        punishEntry.setRemovedName(task.getRemovedName());
        punishEntry.setRemovedReason(task.getRemovedReason());
        punishEntry.setRemovedTime(task.getRemovedTime());
        punishEntry.setActive(false);
        PunishManager.getInstance().removePunishment(punishEntry);
    }

    private void deleteTask(int id) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `staffog_task` WHERE `id`=?");
            statement.setInt(1, id);

            statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
