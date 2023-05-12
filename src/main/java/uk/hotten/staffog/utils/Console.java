package uk.hotten.staffog.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Console {

    public static void info(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.WHITE + "[Staff-OG] " + message); }

    public static void warn(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.YELLOW + "[Staff-OG: WARN] " + message); }

    public static void error(String message) { Bukkit.getServer().getConsoleSender().sendMessage("" + ChatColor.RED + "[Staff-OG: ERROR] " + message); }

}
