package com.promc.announce;

import com.promc.announce.manager.TaskManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    private final JavaPlugin plugin;
    private final ConfigurationSection messagesConfig;

    public Commands(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messagesConfig = plugin.getConfig().getConfigurationSection("messages");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tannouncer.reload")) {

                Announce.getConfigManager().reload(plugin);
                TaskManager.stopAll();
                TaskManager.loadTimer(plugin);
                TaskManager.loadSchedule(plugin);

                String reloadMessage = messagesConfig.getString("reload_config", "");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
            } else {
                String noPermissionMessage = messagesConfig.getString("no_permission", "");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
            }
            return true;
        }
        return false;
    }
}
