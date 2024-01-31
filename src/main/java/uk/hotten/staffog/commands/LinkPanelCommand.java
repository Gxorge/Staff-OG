package uk.hotten.staffog.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Message;

public class LinkPanelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (! (sender instanceof Player player)) {

			sender.sendMessage("ERROR: Only players can run this command!");
			return true;

		}

		SecurityManager sm = SecurityManager.getInstance();

		if (! sm.getStaffIPInfos().containsKey(player.getUniqueId())) {

			Message.staffOGMessage(player, "&cRequired data is missing, please re-log.");
			return true;

		}

		StaffIPInfo ipInfo = sm.getStaffIPInfos().get(player.getUniqueId());
		if (ipInfo.isPanelVerified()) {

			Message.staffOGMessage(player, ("&6Your IP has already been verified on the panel."));
			return true;

		}

		String doesHaveCode = sm.doesPlayerHaveLinkCode(player.getUniqueId());
		if (doesHaveCode != null) {

			Message.staffOGMessage(player, ("&6Your activation code is &e" + doesHaveCode));
			return true;

		}

		String code = sm.createLinkCode(player.getUniqueId(), StaffOGPlugin.getVaultPerms().has(player, "staffog.paneladmin"));
		if (code == null) {

			Message.staffOGMessage(player, ("&cERROR: Failed to create activation code. Please contact an administrator."));
			return true;

		}

		Message.staffOGMessage(player, ("&6Your activation code is: &e" + code));
		return true;

	}

}