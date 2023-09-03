package com.promc.announce.task;

import com.promc.announce.Announce;
import com.promc.announce.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskTimer implements Task {
    private BukkitTask task;
    private ConfigurationSection config;
    public TaskTimer(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public void start() {

        //Bukkit.getLogger().info("Start Timer");

        String[] messages = config.getString("text", "").split("\n");
        List<String> commands = config.getStringList("commands");
        int interval = config.getInt("time", 300);

        //Bukkit.getLogger().info(Arrays.toString(messages) + " commands" + commands + " interval" + interval);
        // 执行命令无法异步触发 改为Bukkit相关处理方法

        task = Bukkit.getScheduler().runTaskLater(Announce.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                for (String message : messages) {
                    player.sendMessage(PlaceholderAPI.setPlaceholders(player, ColorUtil.colorize(message)));
                }
            });
            commands.forEach(command -> Bukkit
                    .dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(null, command)));
            start();
        }, interval * 20L);


    }

    @Override
    public void stop() {
        task.cancel();
    }
}
