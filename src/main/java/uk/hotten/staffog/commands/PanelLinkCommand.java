package uk.hotten.staffog.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Message;

public class PanelLinkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.format(ChatColor.RED + "You cannot run this command!"));
            return true;
        }

        SecurityManager sm = SecurityManager.getInstance();

        if (!sm.getStaffIPInfos().containsKey(player.getUniqueId())) {
            player.sendMessage(Message.format(ChatColor.RED + "Required data is missing, please re-log."));
            return true;
        }

        StaffIPInfo ipInfo = sm.getStaffIPInfos().get(player.getUniqueId());
        if (ipInfo.isPanelVerified()) {
            player.sendMessage(Message.format(ChatColor.RED + "Your IP has already been verified on the panel."));
            return true;
        }

        String doesHaveCode = sm.doesPlayerHaveLinkCode(player.getUniqueId());
        if (doesHaveCode != null) {
            player.sendMessage(Message.format("Your activation code is " + ChatColor.YELLOW + doesHaveCode));
            return true;
        }

        String code = sm.createLinkCode(player.getUniqueId(), StaffOGPlugin.getVaultPerms().has(player, "staffog.paneladmin"));
        if (code == null) {
            player.sendMessage(Message.format(ChatColor.RED + "Failed to create activation code. Please contact an administrator."));
            return true;
        }

        player.sendMessage(Message.format("Your activation code is " + ChatColor.YELLOW + code));
        return true;
    }
}
