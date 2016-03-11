package com.leontg77.noenchantpreview;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.leontg77.noenchantpreview.commands.EnchPreviewCommand;
import com.leontg77.noenchantpreview.protocol.EnchantPreview;

/**
 * Main class of the plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
	public static final String PREFIX = "§dEnchant Preview §8» §7";

	@Override
	public void onDisable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " has been disabled.");
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " v" + file.getVersion() + " has been enabled.");
		getLogger().info("The plugin is made by LeonTG77.");
		
		EnchantPreview ench = new EnchantPreview(this);
		EnchPreviewCommand command = new EnchPreviewCommand(this, ench);
		
		// register command.
		getCommand("enchpreview").setExecutor(command);
		getCommand("enchpreview").setTabCompleter(command);
	}
	
	/**
	 * Broadcasts a message to everyone online.
	 * 
	 * @param message the message.
	 */
	public void broadcast(String message) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.sendMessage(message);
		}
		
		Bukkit.getLogger().info(message);
	}
}