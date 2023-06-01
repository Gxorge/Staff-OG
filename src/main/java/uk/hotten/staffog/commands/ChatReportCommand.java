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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ChatReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Message.format(ChatColor.RED + "You cannot run this command!"));
            return true;
        }

        if (args == null || args.length < 2) {
            player.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /chatreport <player> <reason>"));
            return true;
        }

        PunishManager pm = PunishManager.getInstance();
        if (!pm.isIncludedInCurrentChatReport(args[0])) {
            player.sendMessage(Message.format(ChatColor.RED + args[0] + " has not used chat in the past two minutes."));
            return true;
        }

        UUID reported = pm.getUUIDFromChatReport(args[0]);

        ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
        preReason.remove(0);
        String reason = String.join(" ", preReason);

        String chatReportId = pm.exportChatReport(player.getUniqueId(), reported);
        if (chatReportId == null) {
            player.sendMessage(Message.format(ChatColor.RED + "Failed to generate chat report. Please try again."));
            return true;
        }

        String staffReportId = pm.createReportFromCR(player.getUniqueId(), reported, Integer.parseInt(chatReportId), reason);
        if (staffReportId == null) {
            player.sendMessage(Message.format(ChatColor.RED + "Failed to create a report to staff, however a chat log was successfully exported with ID " +
                    ChatColor.WHITE + "#" + chatReportId + ChatColor.RED + ". Please create a manual report quoting this Chat Report ID on " +
                    ChatColor.WHITE + StaffOGPlugin.getReportWebAddress()));
        } else {
            player.sendMessage(Message.format(ChatColor.GREEN + "Thanks for your report! Your report reference is " +
                    ChatColor.WHITE + "#" + staffReportId + ChatColor.GREEN + ". Your chat report ID is " +
                    ChatColor.WHITE + "#" + chatReportId + ChatColor.GREEN + " and has been included in your report."));
            player.sendMessage(Message.format(ChatColor.DARK_GREEN + "You can check the status of your report at " +
                    ChatColor.WHITE + StaffOGPlugin.getReportWebAddress()));
        }

        return true;
    }
}
