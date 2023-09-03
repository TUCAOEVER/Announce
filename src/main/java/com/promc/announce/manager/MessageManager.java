package com.promc.announce.manager;

import com.promc.announce.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MessageManager {
    private static final Map<String, String> msgMap = new HashMap<>();

    public static void setup(JavaPlugin plugin) {
        plugin.getConfig()
                .getConfigurationSection("messages")
                .getValues(true).forEach((k, v) -> {
                    Bukkit.getLogger().info(k + " " + v);
                    msgMap.put(k, v == null ? k : ColorUtil.colorize((String) v));
                });
    }

    public static void reload(JavaPlugin plugin) {
        plugin.reloadConfig();
    }

    /**
     * 获取配置
     *
     * @param node 配置文件节点
     * @return 配置
     */
    @NotNull
    public static String getMessage(@NotNull String node) {
        return msgMap.getOrDefault(node, "");
    }
}
