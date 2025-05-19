package uk.hotten.staffog.security;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.security.data.StaffIPInfo;

public class SecurityEventListener implements Listener {

	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent event) {

		SecurityManager sm = SecurityManager.getInstance();
		UUID uuid = event.getUniqueId();

		if (! StaffOGPlugin.getVaultPerms().playerHas("world", Bukkit.getOfflinePlayer(uuid), "staffog.panelaccess")) {

			return;

		}

		String ip = event.getAddress().getHostAddress();

		if (! sm.hasUUIDGotIp(event.getUniqueId())) {

			StaffIPInfo ipInfo = new StaffIPInfo(-1, uuid, ip, true, false, false, true);
			sm.createIpEntry(ipInfo);
			return;

		}

		StaffIPInfo ipInfo = sm.isIpRecognised(event.getUniqueId(), event.getAddress().getHostAddress());
		if (ipInfo == null) {

			ipInfo = new StaffIPInfo(-1, uuid, ip, false, false, false, true);
			sm.createIpEntry(ipInfo);

		}

		// Only challenge a new IP if the users's panel account is already set up.
		if (! ipInfo.isPanelAcknowledged() && ! ipInfo.isInitial() && sm.isUserAccountCreated(uuid, ip) == true) {

			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);

			String messageString = "&cNew IP address detected!\n" +
					"&6Please login to the staff panel to authenticate your new IP address.\n\n" +
					"&cIf you are using a VPN, please disconnect it and try again.";

			// Create a MiniMessage instance.
			MiniMessage miniMessage = MiniMessage.miniMessage();
			// Deserialize the string into a TextComponent.
			Component kickMessage = miniMessage.deserialize(messageString);

			event.kickMessage(kickMessage);
			return;

		}

		if (! ipInfo.isGameVerified()) {

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