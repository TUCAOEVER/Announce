package com.promc.announce;

import com.promc.announce.manager.MessageManager;
import com.promc.announce.manager.TaskManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {

    private final JavaPlugin plugin;

    public Commands(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tannouncer.reload")) {

                MessageManager.reload(plugin);

                TaskManager.stopAll();
                TaskManager.loadTimer(plugin);
                TaskManager.loadSchedule(plugin);

                sender.sendMessage(MessageManager.getMessage("reload-config"));
            } else {
                sender.sendMessage(MessageManager.getMessage("no-perm"));
            }
            return true;
        }
        return false;
    }
}
