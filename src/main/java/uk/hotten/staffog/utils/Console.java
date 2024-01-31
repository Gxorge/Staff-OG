package uk.hotten.staffog.utils;

import org.bukkit.Bukkit;

public class Console {

	public static void info(String message) {

		Bukkit.getServer().getConsoleSender().sendMessage("[Staff-OG] " + message);

	}

	public static void warn(String message) {

		Bukkit.getServer().getConsoleSender().sendMessage("[Staff-OG: WARNING] " + message);

	}

	public static void error(String message) {

		Bukkit.getServer().getConsoleSender().sendMessage("[Staff-OG: ERROR] " + message);

	}

}