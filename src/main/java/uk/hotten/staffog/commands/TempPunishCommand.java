package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Message;
import uk.hotten.staffog.utils.TimeUtils;

public class TempPunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args == null || args.length < 4) {

			Message.staffOGMessage((Player) sender, ("&6Correct Usage: &e/" + label + " <player> <unit> <amount> <reason>"));
			return true;

		}

		int amount;
		try {

			amount = Integer.parseInt(args[2]);

		}
		catch (Exception error) {

			Message.staffOGMessage((Player) sender, ("&cERROR: The amount of units must be a number!"));
			return true;

		}

		Date currentTime = new Date(System.currentTimeMillis());
		long untilTime;
		switch (args[1].toLowerCase()) {
			case "d": {
				untilTime = DateUtils.addDays(currentTime, amount).getTime();
				break;
			}
	
			case "w": {
				untilTime = DateUtils.addWeeks(currentTime, amount).getTime();
				break;
			}
	
			case "m": {
				untilTime = DateUtils.addMonths(currentTime, amount).getTime();
				break;
			}
	
			case "y": {
				untilTime = DateUtils.addYears(currentTime, amount).getTime();
				break;
			}
	
			default: {
	
				Message.staffOGMessage((Player) sender, ("&cERROR: Invalid time unit. &6Please supply: &e(d)ay, (w)eek, (m)onth or (y)ear&6."));
				return true;
	
			}
		}

		PunishType commandType;
		if (label.toLowerCase().contains("tempban")) {

			commandType = PunishType.BAN;

		}
		else if (label.toLowerCase().contains("tempmute")) {

			commandType = PunishType.MUTE;

		}
		else {

			Message.staffOGMessage((Player) sender, ("&cERROR: Unsupported type for command."));
			return true;

		}

		UUID uuid = PunishManager.getInstance().getUUIDFromName(args[0]);
		if (uuid == null) {

			Message.staffOGMessage((Player) sender, ("&c" + args[0] + " has never joined the server!"));
			return true;

		}

		PunishEntry isPunished = PunishManager.getInstance().checkActivePunishment(commandType, uuid);
		if (isPunished != null) {

			Message.staffOGMessage((Player) sender, ("&c" + args[0] + " is already " + commandType.getBroadcastMessage() + " for " + TimeUtils.formatMillisecondTime(isPunished.calculateRemaining())) + ".");
			return true;

		}

		OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);

		PunishEntry entry = new PunishEntry(commandType);

		entry.setUuid(uuid);
		entry.setName(player.getName());
		if (player.isOnline()) {

			entry.setPlayer(player.getPlayer());

		}
		entry.setByUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "NCP");
		entry.setByName((sender instanceof Player) ? ((Player) sender).getName() : "NCP");
		entry.setTime(currentTime.getTime());
		entry.setUntil(untilTime);
		entry.setActive(true);

		ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
		preReason.remove(0);
		preReason.remove(0);
		preReason.remove(0);
		String reason = String.join(" ", preReason);
		entry.setReason(reason);

		PunishManager.getInstance().newPunishment(entry);

		Message.staffOGMessage((Player) sender, ("&7" + "You have " + commandType.getBroadcastMessage() + " " + entry.getName() + " for " + TimeUtils.formatMillisecondTime(entry.calculateDuration()) + "."));
		return true;

	}

}