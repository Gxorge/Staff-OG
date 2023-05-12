package uk.hotten.staffog;

import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.utils.Console;

public class StaffOGPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Console.info("Setting up Staff-OG...");

        this.saveDefaultConfig();

        DatabaseManager databaseManager = new DatabaseManager(this);

        Console.info("Staff-OG is ready!");
    }
}
