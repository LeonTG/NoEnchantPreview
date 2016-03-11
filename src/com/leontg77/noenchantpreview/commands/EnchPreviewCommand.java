package com.leontg77.noenchantpreview.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.leontg77.noenchantpreview.Main;
import com.leontg77.noenchantpreview.protocol.EnchantPreview;

/**
 * EnchPreview command class.
 * 
 * @author LeonTG77
 */
public class EnchPreviewCommand implements CommandExecutor, TabCompleter {
	private static final String PERMISSION = "enchpreview.manage";
	
	private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

	private final EnchantPreview ench;
	private final Main plugin;
	
	/**
	 * EnchPreview command class constructor.
	 * 
	 * @param plugin The main class.
	 * @param ench The enchantment preview protocol.
	 */
	public EnchPreviewCommand(Main plugin, EnchantPreview ench) {
		this.plugin = plugin;
		this.ench = ench;
	}
	
	private boolean enabled = true;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Main.PREFIX + "Usage: /enchpreview <info|enable|disable>");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("info")) {
			sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG77");
			sender.sendMessage(Main.PREFIX + "Version: §av" + plugin.getDescription().getVersion());
			sender.sendMessage(Main.PREFIX + "Description:");
			sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("enable")) {
			if (!sender.hasPermission(PERMISSION)) {
				sender.sendMessage(ChatColor.RED + "You don't have permission.");
				return true;
			}
			
			if (enabled) {
				sender.sendMessage(Main.PREFIX + "EnchantPreview is already enabled.");
				return true;
			}
			
			plugin.broadcast(Main.PREFIX + "EnchantPreview has been enabled.");
			enabled = true;
			
			manager.removePacketListener(ench);
			return true;
		}

		if (args[0].equalsIgnoreCase("disable")) {
			if (!sender.hasPermission(PERMISSION)) {
				sender.sendMessage(ChatColor.RED + "You don't have permission.");
				return true;
			}
			
			if (!enabled) {
				sender.sendMessage(Main.PREFIX + "EnchantPreview is not enabled.");
				return true;
			}

			plugin.broadcast(Main.PREFIX + "EnchantPreview has been disabled.");
			enabled = false;

			manager.addPacketListener(ench);
			return true;
		}
		
		sender.sendMessage(Main.PREFIX + "Usage: /enchpreview <info|enable|disable>");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> toReturn = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		
		if (args.length != 1) {
			return toReturn;
		}
		
		list.add("info");
		
		if (sender.hasPermission(PERMISSION)) {
			list.add("enable");
			list.add("disable");
		}

		// make sure to only tab complete what starts with what they 
		// typed or everything if they didn't type anything
		for (String str : list) {
			if (args[0].isEmpty() || str.startsWith(args[0].toLowerCase())) {
				toReturn.add(str);
			}
		}
		
		return toReturn;
	}
}