package uk.hotten.staffog;

import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.commands.PermPunishCommand;
import uk.hotten.staffog.commands.UnpunishCommand;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.utils.Console;

public class StaffOGPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up Staff-OG...");

        this.saveDefaultConfig();

        DatabaseManager databaseManager = new DatabaseManager(this);
        PunishManager punishManager = new PunishManager(this);

        getCommand("permban").setExecutor(new PermPunishCommand());
        getCommand("unban").setExecutor(new UnpunishCommand());
        getCommand("permmute").setExecutor(new PermPunishCommand());
        getCommand("unmute").setExecutor(new UnpunishCommand());

        Console.info("Staff-OG is ready!");
    }
}
