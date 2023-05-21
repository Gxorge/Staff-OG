package uk.hotten.staffog.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.KickPunishEntry;
import uk.hotten.staffog.utils.Message;
import uk.hotten.staffog.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class KickCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args == null || args.length < 1) {
            sender.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /kick <player> [reason]"));
            return true;
        }

        Player toKick = Bukkit.getServer().getPlayer(args[0]);
        if (toKick == null) {
            sender.sendMessage(Message.format(ChatColor.RED + args[0] + " is not online!"));
            return true;
        }

        KickPunishEntry entry = new KickPunishEntry();
        entry.setUuid(toKick.getUniqueId());
        entry.setName(toKick.getName());
        entry.setPlayer(toKick);
        entry.setByUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "NCP");
        entry.setByName((sender instanceof Player) ? ((Player) sender).getName() : "NCP");
        entry.setTime(System.currentTimeMillis());

        if (args.length >= 2) {
            ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
            preReason.remove(0);
            String reason = String.join(" ", preReason);
            entry.setReason(reason);
        } else {
            entry.setReason("No reason specified");
        }

        PunishManager.getInstance().newKickPunishment(entry);
        sender.sendMessage(Message.format(ChatColor.GRAY + "You have kicked " + entry.getName() + "."));
        return true;
    }
}
