package uk.hotten.staffog.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.security.data.StaffIPInfo;
import uk.hotten.staffog.utils.Message;

import java.util.UUID;

public class ChatReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.format(ChatColor.RED + "You cannot run this command!"));
            return true;
        }

        if (args == null || args.length < 1) {
            player.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /chatreport <player>"));
            return true;
        }

        PunishManager pm = PunishManager.getInstance();
        if (!pm.isIncludedInCurrentChatReport(args[0])) {
            player.sendMessage(Message.format(ChatColor.RED + args[0] + " has not used chat in the past two minutes."));
            return true;
        }

        UUID reported = pm.getUUIDFromChatReport(args[0]);

        String reportId = pm.exportChatReport(player.getUniqueId(), reported);
        if (reportId == null) {
            player.sendMessage(Message.format(ChatColor.RED + "Failed to generate chat report. Please try again."));
            return true;
        }

        player.sendMessage(Message.format(ChatColor.GREEN + "Chat report successfully created. Please quote the report ID " + ChatColor.WHITE + "#" + reportId + ChatColor.GREEN + " to staff when required."));
        return true;
    }
}
