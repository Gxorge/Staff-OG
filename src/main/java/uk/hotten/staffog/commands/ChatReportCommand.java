package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.utils.Message;

public class ChatReportCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (! (sender instanceof Player player)) {

			sender.sendMessage("ERROR: Only players can run this command!");
			return true;

		}

		if (args == null || args.length < 2) {

			Message.staffOGMessage(player, "&6Correct Usage: &e/chatreport <player> <reason>");
			return true;

		}

		PunishManager pm = PunishManager.getInstance();
		if (! pm.isIncludedInCurrentChatReport(args[0])) {

			Message.staffOGMessage(player, ("&c" + args[0] + " has not used chat in the past two minutes."));
			return true;

		}

		UUID reported = pm.getUUIDFromChatReport(args[0]);

		ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
		preReason.remove(0);
		String reason = String.join(" ", preReason);

		String chatReportId = pm.exportChatReport(player.getUniqueId(), reported);
		if (chatReportId == null) {

			Message.staffOGMessage(player, "&c Failed to generate chat report. Please try again.");
			return true;

		}

		String staffReportId = pm.createReportFromCR(player.getUniqueId(), reported, Integer.parseInt(chatReportId), reason);
		if (staffReportId == null) {

			Message.staffOGMessage(player, ("&cFailed to create a report to staff, however a chat log was successfully exported with ID " +
					"&f" + "#" + chatReportId + "&c" + ". Please create a manual report quoting this Chat Report ID on " +
					"&f" + StaffOGPlugin.getReportWebAddress()));

		}
		else {

			Message.staffOGMessage(player, ("&a Thanks for your report! Your report reference is " +
					"&f" + "#" + staffReportId + "&a" + ". Your chat report ID is " +
					"&f" + "#" + chatReportId + "&a" + " and has been included in your report."));

			Message.staffOGMessage(player, ("&2" + "You can check the status of your report at " +
					"&f" + StaffOGPlugin.getReportWebAddress()));

			Message.staffBroadcast(Message.formatNotification("REPORT", "New Mutable Offences report #" + staffReportId + " by " + player.getName() + " against " + args[0]));

		}

		return true;

	}

}