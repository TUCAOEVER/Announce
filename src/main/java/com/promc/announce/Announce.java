package com.promc.announce;

import com.promc.announce.manager.ConfigManager;
import com.promc.announce.manager.TaskManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Announce extends JavaPlugin {

    @Getter
    private static ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        configManager = new ConfigManager();
        configManager.setup(this);

        TaskManager.loadSchedule(this);
        TaskManager.loadTimer(this);

        getCommand("announce").setExecutor(new Commands(this));
        getLogger().info(ColorUtil.colorize("Announce Enabled"));
    }

    @Override
    public void onDisable() {
        TaskManager.stopAll();
    }

}
