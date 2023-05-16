package uk.hotten.staffog.data;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DatabaseManager dbm = DatabaseManager.getInstance();
        dbm.setStatEntry("player_count", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));
        if (event.getPlayer().hasPermission("staffog.isstaff")) {
            dbm.staffOnline++;
            dbm.setStatEntry("staff_count", String.valueOf(dbm.staffOnline));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getServer().getScheduler().runTaskLater(DatabaseManager.getInstance().getPlugin(), () -> {
            DatabaseManager dbm = DatabaseManager.getInstance();
            dbm.setStatEntry("player_count", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));

            if (event.getPlayer().hasPermission("staffog.isstaff")) {
                dbm.staffOnline--;
                dbm.setStatEntry("staff_count", String.valueOf(dbm.staffOnline));
            }
        }, 5);
    }

}
