package com.promc.announce.manager;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final Map<String, Object> configMap = new HashMap<>();

    public void setup(JavaPlugin plugin) {
        plugin.getConfig().getValues(true).forEach((k, v) -> {
            //Bukkit.getLogger().info(k + " " + v);
            configMap.put(k, v == null ? k : v);
        });
    }

    public void reload(JavaPlugin plugin) {
        plugin.reloadConfig();
    }

    /**
     * 获取配置
     *
     * @param node 配置文件节点
     * @param def 默认值
     * @return 配置
     */
    @NotNull
    public Object getConfig(@NotNull String node, Object def) {
        return configMap.getOrDefault(node, def);
    }
}
