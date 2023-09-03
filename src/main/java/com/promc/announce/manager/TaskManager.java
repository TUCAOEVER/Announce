package com.promc.announce.manager;

import com.promc.announce.task.Task;
import com.promc.announce.task.TaskSchedule;
import com.promc.announce.task.TaskTimer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final static List<Task> scheduleTasks = new ArrayList<>();
    private final static List<Task> timerTasks = new ArrayList<>();

    /**
     * 加载定时任务
     *
     * @param plugin 插件对象
     */
    public static void loadSchedule(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("schedule");
        if (section == null) return;
        section.getKeys(false).forEach(key -> {
            Task task = new TaskSchedule(section.getConfigurationSection(key));
            task.start();
            scheduleTasks.add(task);
        });
    }

    /**
     * 停止所有定时任务
     */
    public static void stopAllSchedule() {
        scheduleTasks.forEach(Task::stop);
        scheduleTasks.clear();
    }

    /**
     * 加载计时任务
     *
     * @param plugin 插件对象
     */
    public static void loadTimer(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("timer");
        if (section == null) return;
        section.getKeys(false).forEach(key -> {
            Task task = new TaskTimer(section.getConfigurationSection(key));
            task.start();
            timerTasks.add(task);
        });
    }

    /**
     * 停止所有计时任务
     */
    public static void stopAllTimer() {
        timerTasks.forEach(Task::stop);
        timerTasks.clear();
    }


    /**
     * 停止所有任务并清除缓存（包括定时任务和计时任务）
     */
    public static void stopAll() {
        stopAllSchedule();
        stopAllTimer();
    }
}
