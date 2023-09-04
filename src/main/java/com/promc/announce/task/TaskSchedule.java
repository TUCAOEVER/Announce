package com.promc.announce.task;

import com.promc.announce.Announce;
import com.promc.announce.ColorUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskSchedule implements Task {

    private final ConfigurationSection config;
    private BukkitTask task;

    public TaskSchedule(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    public void start() {
        try {
            //Bukkit.getLogger().info("Start Schedule " + config.getString("time", "00:00"));
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            LocalTime refreshTime = LocalTime.parse(config.getString("time", "00:00"));

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime future = LocalDateTime.of(currentDate, refreshTime);

            if (now.isAfter(future)) future = future.plusDays(1);
            // 计算时间间隔
            long iniDelay = Duration.between(now, future).toSeconds();

            List<String> messages = config.getStringList("text");
            String[] message = !messages.isEmpty() ? messages.get(new Random().nextInt(messages.size()) - 1).split("\n") : new String[0];

            List<String> commands = config.getStringList("commands");

            task = Bukkit.getScheduler().runTaskLater(Announce.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
                for (String msg : message) {
                    player.sendMessage(PlaceholderAPI.setPlaceholders(player, ColorUtil.colorize(msg)));
                }
                commands.forEach(command -> Bukkit
                        .dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(null, command)));
                start();
            }), iniDelay * 20L);
        } catch (DateTimeParseException exception) {
            Announce.getInstance().getLogger().warning("Schedule time can't be parsed. Check the time config first.");
        }


    }

    @Override
    public void stop() {
        task.cancel();
    }
}
