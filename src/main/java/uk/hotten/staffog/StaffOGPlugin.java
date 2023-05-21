package uk.hotten.staffog;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.hotten.staffog.commands.*;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.tasks.TaskManager;
import uk.hotten.staffog.utils.Console;

public class StaffOGPlugin extends JavaPlugin {

    @Getter private static Permission vaultPerms;

    @Override
    public void onEnable() {
        Console.info("Setting up Staff-OG...");

        this.saveDefaultConfig();

        if (!setupVaultPerms()) {
            Console.error("Vault not found. Plugin will be disabled.");
            getServer().getPluginManager().disablePlugin(this);
        }

        DatabaseManager databaseManager = new DatabaseManager(this);
        PunishManager punishManager = new PunishManager(this);
        SecurityManager securityManager = new SecurityManager(this);
        TaskManager taskManager = new TaskManager(this);

        getCommand("linkpanel").setExecutor(new LinkPanelCommand());

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

    private boolean setupVaultPerms() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        vaultPerms = rsp.getProvider();
        return vaultPerms != null;
    }
}
