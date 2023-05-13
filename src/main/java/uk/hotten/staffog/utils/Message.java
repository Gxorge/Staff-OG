package uk.hotten.staffog.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Message {

    public static String format(String message) {
        return "" + ChatColor.GRAY + "[" + ChatColor.YELLOW + "Staff-OG" + ChatColor.GRAY + "] " + ChatColor.RESET + message;
    }

    public static String formatNotification(String message) {
        return "" + ChatColor.RED + "STAFF | " + ChatColor.AQUA + "Notification " + ChatColor.DARK_GRAY + "> " + ChatColor.RESET + message;
    }

    public static void staffBroadcast(String message) {
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (p.hasPermission("staffog.seebroadcast")) p.sendMessage(message);
        });
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

}
