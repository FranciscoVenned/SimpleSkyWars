package com.venned.simpleskywars.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class UtilsConfig {

    private final Plugin plugin;

    public UtilsConfig(Plugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getArenaConfig(String arenaName) {
        File arenasDirectory = new File(plugin.getDataFolder(), "maps");
        File arenaFile = new File(arenasDirectory, arenaName + ".yml");

        if (!arenaFile.exists()) {
            return null;
        }

        return YamlConfiguration.loadConfiguration(arenaFile);
    }

    public void saveArenaConfig(FileConfiguration config, String arenaName) {
        File arenasDirectory = new File(plugin.getDataFolder(), "maps");
        File arenaFile = new File(arenasDirectory, arenaName + ".yml");

        try {
            config.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
