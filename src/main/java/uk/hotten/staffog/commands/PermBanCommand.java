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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PermBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args == null || args.length < 2) {
            sender.sendMessage(Message.format(ChatColor.RED + "Correct Usage: /permban <name> <reason>"));
            return true;
        }

        UUID uuid = PunishManager.getInstance().getUUIDFromName(args[0]);
        if (uuid == null) {
            sender.sendMessage(Message.format(args[0] + " has never joined the server!"));
            return true;
        }

        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);

        PunishEntry entry = new PunishEntry(PunishType.BAN);

        entry.setUuid(uuid);
        entry.setName(player.getName());
        if (player.isOnline())
            entry.setPlayer(player.getPlayer());
        entry.setByUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "Console");
        entry.setByName((sender instanceof Player) ? ((Player) sender).getName() : "Console");
        entry.setTime(System.currentTimeMillis());
        entry.setUntil(-1);
        entry.setActive(true);

        ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
        preReason.remove(0);
        String reason = String.join(" ", preReason);
        entry.setReason(reason);

        PunishManager.getInstance().newPunishment(entry);
        return true;
    }
}
