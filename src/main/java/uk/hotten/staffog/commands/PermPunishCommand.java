package uk.hotten.staffog.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PermPunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args == null || args.length < 2) {
            sender.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /" + label + " <player> <reason>"));
            return true;
        }

        PunishType commandType;
        if (label.toLowerCase().contains("permban"))
            commandType = PunishType.BAN;
        else if (label.toLowerCase().contains("permmute"))
            commandType = PunishType.MUTE;
        else {
            sender.sendMessage(Message.format(ChatColor.RED + "Unsupported type for command."));
            return true;
        }

        UUID uuid = PunishManager.getInstance().getUUIDFromName(args[0]);
        if (uuid == null) {
            sender.sendMessage(Message.format(ChatColor.RED + args[0] + " has never joined the server!"));
            return true;
        }

        PunishEntry isPunished = PunishManager.getInstance().checkActivePunishment(commandType, uuid);
        if (isPunished != null) {
            sender.sendMessage(Message.format(ChatColor.RED + args[0] + " is already " + commandType.getBroadcastMessage() + " for " + TimeUtils.formatMillisecondTime(isPunished.calculateRemaining())) + ".");
            return true;
        }

        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);

        PunishEntry entry = new PunishEntry(commandType);

        entry.setUuid(uuid);
        entry.setName(player.getName());
        if (player.isOnline())
            entry.setPlayer(player.getPlayer());
        entry.setByUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "NCP");
        entry.setByName((sender instanceof Player) ? ((Player) sender).getName() : "NCP");
        entry.setTime(System.currentTimeMillis());
        entry.setUntil(-1);
        entry.setActive(true);

        ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
        preReason.remove(0);
        String reason = String.join(" ", preReason);
        entry.setReason(reason);

        PunishManager.getInstance().newPunishment(entry);
        sender.sendMessage(Message.format(ChatColor.GRAY + "You have " + commandType.getBroadcastMessage() + " " + entry.getName() + " for permanent."));
        return true;
    }
}
