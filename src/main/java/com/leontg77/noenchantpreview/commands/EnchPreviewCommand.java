/*
 * Project: NoEnchantPreview
 * Class: com.leontg77.noenchantpreview.commands.EnchPreviewCommand
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.leontg77.noenchantpreview.commands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Lists;
import com.leontg77.noenchantpreview.Main;
import com.leontg77.noenchantpreview.protocol.EnchantPreview;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.List;

/**
 * EnchPreview command class.
 *
 * @author LeonTG
 */
public class EnchPreviewCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "enchpreview.manage";

    private final EnchantPreview ench;
    private final Main plugin;

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    public EnchPreviewCommand(Main plugin, EnchantPreview ench) {
        this.plugin = plugin;
        this.ench = ench;
    }

    private boolean enabled = true;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.PREFIX + "Usage: /enchpreview <info/enable/disable/exempt> [player]");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG");
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
                sender.sendMessage(Main.PREFIX + "Enchant Preview is already enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Enchant Preview has been enabled.");
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
                sender.sendMessage(Main.PREFIX + "Enchant Preview is not enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Enchant Preview has been disabled.");
            enabled = false;

            manager.addPacketListener(ench);
            return true;
        }

        if (args[0].equalsIgnoreCase("exempt")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(Main.PREFIX + "Usage: /enchpreview exempt <player>");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (plugin.getExempted().contains(target.getUniqueId())) {
                plugin.getExempted().remove(target.getUniqueId());
                sender.sendMessage(Main.PREFIX + "'§a" + target.getName() + "§7' is no longer exempted from the no enchant preview.");
            } else {
                plugin.getExempted().add(target.getUniqueId());
                sender.sendMessage(Main.PREFIX + "'§a" + target.getName() + "§7' is now exempted from the no enchant preview.");
            }
            return true;
        }

        sender.sendMessage(Main.PREFIX + "Usage: /enchpreview <info/enable/disable/exempt> [player]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = Lists.newArrayList();

        if (args.length == 1) {
            list.add("info");

            if (sender.hasPermission(PERMISSION)) {
                list.add("enable");
                list.add("disable");
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], list, Lists.newArrayList());
    }
}