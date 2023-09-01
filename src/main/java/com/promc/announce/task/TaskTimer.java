package com.promc.announce.task;

import com.promc.announce.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskTimer implements Task {
    private ScheduledFuture<?> scheduledFuture;
    private ConfigurationSection config;

    public TaskTimer(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public void start() {

        String[] messages = config.getString("text", "").split("\n");
        List<String> commands = config.getStringList("commands");
        int interval = config.getInt("time", 300);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = executorService.scheduleWithFixedDelay(() -> {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        for (String message : messages) {
                            player.sendMessage(PlaceholderAPI.setPlaceholders(player, ColorUtil.colorize(message)));
                        }
                    });
                    commands.forEach(command -> Bukkit
                            .dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(null, command)));
                },
                interval,
                interval,
                TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        scheduledFuture.cancel(true);
    }
}
