package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.Message;

public class UnpunishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args == null || args.length < 2) {
			
			Message.staffOGMessage((Player) sender, ("&6Correct Usage: &e/" + label + " <player> <reason>"));
			return true;
			
		}

		PunishType commandType;
		if (label.toLowerCase().contains("unban")) {
			
			commandType = PunishType.BAN;
			
		}
		else if (label.toLowerCase().contains("unmute")) {
			
			commandType = PunishType.MUTE;

		}
		else {
			
			Message.staffOGMessage((Player) sender, ("&cERROR: Unsupported type for command."));
			return true;
			
		}

		UUID uuid = PunishManager.getInstance().getUUIDFromName(args[0]);
		if (uuid == null) {
			
			Message.staffOGMessage((Player) sender, ("&e" + args[0] + " has never joined the server!"));
			return true;
			
		}

		PunishEntry entry = PunishManager.getInstance().checkActivePunishment(commandType, uuid);
		if (entry == null) {
			
			Message.staffOGMessage((Player) sender, ("&c" + args[0] + " is not currently " + commandType.getBroadcastMessage() + "."));
			return true;
			
		}

		entry.setRemovedUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "Console");
		entry.setRemovedName((sender instanceof Player) ? ((Player) sender).getName() : "Console");

		ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
		preReason.remove(0);
		String reason = String.join(" ", preReason);
		entry.setRemovedReason(reason);

		PunishManager.getInstance().removePunishment(entry);
		Message.staffOGMessage((Player) sender, ("&7You have un" + commandType.getBroadcastMessage() + " " + entry.getName() + "."));

		return true;

	}

}