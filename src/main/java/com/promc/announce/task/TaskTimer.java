package com.promc.announce.task;

import com.promc.announce.Announce;
import com.promc.announce.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class TaskTimer implements Task {
    private BukkitTask task;
    private final ConfigurationSection config;
    private int index = 0;

    public TaskTimer(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public void start() {

        //Bukkit.getLogger().info("Start Timer");

        List<String> messages = config.getStringList("text");
        String[] message = !messages.isEmpty() ? messages.get(index).split("\n") : new String[0];
        List<String> commands = config.getStringList("commands");
        int interval = config.getInt("time", 300);

        //Bukkit.getLogger().info(Arrays.toString(messages) + " commands" + commands + " interval" + interval);
        // 执行命令无法异步触发 改为Bukkit相关处理方法

        task = Bukkit.getScheduler().runTaskLater(Announce.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                        for (String msg : message) {
                            player.sendMessage(PlaceholderAPI.setPlaceholders(player, ColorUtil.colorize(msg)));
                        }
                    });
            commands.forEach(command ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(null, command)));
            start();
        }, interval * 20L);

        if (++index > messages.size() - 1) index = 0;

    }

    @Override
    public void stop() {
        task.cancel();
    }
}
