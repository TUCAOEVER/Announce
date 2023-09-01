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
    private final static  List<Task> timerTasks = new ArrayList<>();

    public static void loadSchedule(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("schedule");
        if (section == null) return;
        section.getKeys(false).forEach(key -> {
            Task task = new TaskSchedule(section);
            task.start();
            scheduleTasks.add(task);
        });
    }
    public static void stopAllSchedule() {
        scheduleTasks.forEach(Task::stop);
    }

    public static void loadTimer(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("timer");
        if (section == null) return;
        section.getKeys(false).forEach(key -> {
            Task task = new TaskTimer(section);
            task.start();
            timerTasks.add(task);
        });
    }
    public static void stopAllTimer() {
        timerTasks.forEach(Task::stop);
    }


    public static void stopAll(){
        stopAllSchedule();
        stopAllTimer();
    }
}
