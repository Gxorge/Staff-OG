package uk.hotten.staffog.tasks;

import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.data.jooq.Tables;
import uk.hotten.staffog.data.jooq.tables.records.StaffogTaskRecord;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.tasks.data.TaskEntry;
import uk.hotten.staffog.tasks.data.UnpunishTask;

import java.sql.Connection;
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
            ArrayList<TaskEntry> toReturn = new ArrayList<>();

            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            Result<StaffogTaskRecord> result = dsl.select(Tables.STAFFOG_TASK.asterisk())
                    .from(Tables.STAFFOG_TASK)
                    .fetchInto(Tables.STAFFOG_TASK);

            for (StaffogTaskRecord record : result) {
                TaskEntry entry = new TaskEntry();
                entry.setId(record.getId());
                entry.setTask(record.getTask());
                entry.setData(record.getData());
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
        PunishManager.getInstance().visualRemovePunishment(punishEntry);
    }

    private void deleteTask(int id) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()){
            DSLContext dsl = DSL.using(connection, SQLDialect.MYSQL);
            dsl.delete(Tables.STAFFOG_TASK)
                    .where(Tables.STAFFOG_TASK.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
