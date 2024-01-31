package uk.hotten.staffog;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import uk.hotten.staffog.commands.ChatReportCommand;
import uk.hotten.staffog.commands.KickCommand;
import uk.hotten.staffog.commands.LinkPanelCommand;
import uk.hotten.staffog.commands.PermPunishCommand;
import uk.hotten.staffog.commands.TempPunishCommand;
import uk.hotten.staffog.commands.UnpunishCommand;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.tasks.TaskManager;
import uk.hotten.staffog.utils.Console;

public class StaffOGPlugin extends JavaPlugin {

	public static Permission getVaultPerms() {
		return vaultPerms;
	}

	public static String getReportWebAddress() {

		return reportWebAddress;

	}

	// Declare variable to hold class for passing.
	private static StaffOGPlugin plugin;

	private static Permission vaultPerms;

	private static String reportWebAddress;
	@Getter private static String appealWebAddress;

	@Override
	public void onEnable() {

		Console.info("Setting up Staff-OG...");

		// Assign the plugin variable to the main class instance.
		plugin = this;

		// Tell JOOQ to STFU
		System.setProperty("org.jooq.no-tips", "true");

		this.saveDefaultConfig();

		if (! setupVaultPerms()) {

			Console.error("Vault not found. Plugin will be disabled.");
			getServer().getPluginManager().disablePlugin(this);

		}

		reportWebAddress = this.getConfig().getString("reportWebAddress");
		appealWebAddress = this.getConfig().getString("appealWebAddress");

		DatabaseManager databaseManager = new DatabaseManager(this);
		new PunishManager(this);
		new SecurityManager(this);
		new TaskManager(this);

		getCommand("linkpanel").setExecutor(new LinkPanelCommand());
		getCommand("chatreport").setExecutor(new ChatReportCommand());

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

	// Class constructor.
	public static StaffOGPlugin getPlugin() {

		// Pass instance of main to other classes.
		return plugin;

	}

}