package uk.hotten.staffog.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.hotten.staffog.StaffOGPlugin;

public class DataEventListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {

		updatePlayerCounts();

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {

		Bukkit.getServer().getScheduler().runTaskLater(DatabaseManager.getPlugin(), this::updatePlayerCounts, 10);

	}

	private void updatePlayerCounts() {

		DatabaseManager dbm = DatabaseManager.getInstance();

		// All players
		dbm.setStatEntry("player_count", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));

		// Staff
		int amount = 0;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {

			if (StaffOGPlugin.getVaultPerms().has(p, "staffog.isstaff")) {

				amount++;

			}

		}

		dbm.setStatEntry("staff_count", String.valueOf(amount));

	}

}