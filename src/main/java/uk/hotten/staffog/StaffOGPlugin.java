package uk.hotten.staffog;

import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.commands.KickCommand;
import uk.hotten.staffog.commands.PermPunishCommand;
import uk.hotten.staffog.commands.TempPunishCommand;
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
        getCommand("tempban").setExecutor(new TempPunishCommand());
        getCommand("unban").setExecutor(new UnpunishCommand());
        getCommand("permmute").setExecutor(new PermPunishCommand());
        getCommand("tempmute").setExecutor(new TempPunishCommand());
        getCommand("unmute").setExecutor(new UnpunishCommand());

        getCommand("kick").setExecutor(new KickCommand());

        databaseManager.setStatEntry("server_status", "online");

        Console.info("Staff-OG is ready!");
    }

    @Override
    public void onDisable() {
        DatabaseManager.getInstance().setStatEntry("server_status", "offline");
        DatabaseManager.getInstance().setStatEntry("player_count", "0");
        DatabaseManager.getInstance().setStatEntry("staff_count", "0");
    }
}
