package com.promc.announce;

import com.promc.announce.manager.MessageManager;
import com.promc.announce.manager.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Announce extends JavaPlugin {

    private static Announce announce;

    @Override
    public void onEnable() {
        announce = this;
        saveDefaultConfig();

        MessageManager.setup(this);

        TaskManager.loadSchedule(this);
        TaskManager.loadTimer(this);

        getCommand("announce").setExecutor(new Commands(this));
        getLogger().info("Announce Enabled. Designed by TUCAOEVER");
    }

    @Override
    public void onDisable() {
        TaskManager.stopAll();
    }


    public static Announce getInstance() {
        return announce;
    }
}
