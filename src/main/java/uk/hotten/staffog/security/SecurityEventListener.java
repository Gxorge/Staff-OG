package uk.hotten.staffog.security;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Console;

import java.util.UUID;

public class SecurityEventListener implements Listener {

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        SecurityManager sm = SecurityManager.getInstance();
        UUID uuid = event.getUniqueId();

        if (!StaffOGPlugin.getVaultPerms().playerHas("world", Bukkit.getOfflinePlayer(uuid), "staffog.panelaccess")) {
            Console.info("No perms");
            return;
        }

        String ip = event.getAddress().getHostAddress();

        if (!sm.hasUUIDGotIp(event.getUniqueId())) {
            StaffIPInfo ipInfo = new StaffIPInfo(-1, uuid, ip, true, false, false, true);
            sm.createIpEntry(ipInfo);
            return;
        }

        StaffIPInfo ipInfo = sm.isIpRecognised(event.getUniqueId(), event.getAddress().getHostAddress());
        if (ipInfo == null) {
            ipInfo = new StaffIPInfo(-1, uuid, ip, false, false, false, true);
            sm.createIpEntry(ipInfo);
        }

        if (!ipInfo.isPanelAcknowledged() && !ipInfo.isInitial()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "New IP address detected!\n" +
                    ChatColor.WHITE + "Please login to the staff panel to authenticate your new IP address.\n\n" +
                    ChatColor.RED + "If you are using a VPN, please disconnect it and try again.");
            return;
        }

        if (!ipInfo.isGameVerified()) {
            ipInfo.setGameVerified(true);
            sm.updateIpEntry(ipInfo);
        }

        sm.getStaffIPInfos().put(uuid, ipInfo);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SecurityManager.getInstance().getStaffIPInfos().remove(event.getPlayer().getUniqueId());
    }

}
