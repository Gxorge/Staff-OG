package uk.hotten.staffog.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Message {

    public static String format(String message) {
        return "" + ChatColor.GRAY + "[" + ChatColor.YELLOW + "Staff-OG" + ChatColor.GRAY + "] " + ChatColor.RESET + message;
    }

    public static void broadcast(String message, String permission) {
        Bukkit.getServer().getOnlinePlayers().forEach(p -> {
            if (p.hasPermission(permission)) p.sendMessage(message);
        });
    }

}
