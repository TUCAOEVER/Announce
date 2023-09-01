package com.promc.announce.task;

import com.promc.announce.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskSchedule implements Task {

    private ScheduledFuture<?> scheduledFuture;
    private ConfigurationSection config;


    public TaskSchedule(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public void start() {

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        LocalTime refreshTime = LocalTime.parse(config.getString("time", "00:00"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = LocalDateTime.of(currentDate, refreshTime);

        if (now.isAfter(future)) future = future.plusDays(1);
        // 计算时间间隔
        long iniDelay = Duration.between(now, future).toMillis();

        String[] messages = config.getString("text", "").split("\n");

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = executorService.scheduleWithFixedDelay(() -> Bukkit.getOnlinePlayers().forEach(player -> {
                    for (String message : messages) {
                        player.sendMessage(PlaceholderAPI.setPlaceholders(player, ColorUtil.colorize(message)));
                    }
                }),
                iniDelay,
                TimeUnit.DAYS.toMillis(1),
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        scheduledFuture.cancel(true);
    }
}
