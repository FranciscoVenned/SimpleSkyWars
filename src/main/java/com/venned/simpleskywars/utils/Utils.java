package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utils {

    private final Plugin plugin;

    public Utils(Plugin plugin) {
        this.plugin = plugin;
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void createExampleTiersConfig(File tiersFile, Plugin plugin) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(tiersFile);

        config.createSection("tiers.1.contents.1");
        config.set("tiers.1.contents.1.Material", "APPLE");
        config.set("tiers.1.contents.1.amount", 5);
        config.set("tiers.1.contents.1.chance", 30);

        config.createSection("tiers.1.contents.2");
        config.set("tiers.1.contents.2.Material", "DIAMOND_SWORD");
        config.set("tiers.1.contents.2.amount", 1);
        config.set("tiers.1.contents.2.chance", 1);

        config.createSection("tiers.1.contents.3");
        config.set("tiers.1.contents.3.Material", "IRON_SWORD");
        config.set("tiers.1.contents.3.amount", 1);
        config.set("tiers.1.contents.3.chance", 15);
        List<String> enchants = new ArrayList<>();
        enchants.add("SHARPNESS:2");
        config.set("tiers.1.contents.3.enchant", enchants);

        config.createSection("tiers.2.contents.1");
        config.set("tiers.2.contents.1.Material", "GOLDEN_APPLE");
        config.set("tiers.2.contents.1.amount", 3);
        config.set("tiers.2.contents.1.chance", 20);

        config.createSection("tiers.2.contents.2");
        config.set("tiers.2.contents.2.Material", "BOW");
        config.set("tiers.2.contents.2.amount", 1);
        config.set("tiers.2.contents.2.chance", 5);
        enchants = new ArrayList<>();
        enchants.add("POWER:3");
        config.set("tiers.2.contents.2.enchant", enchants);

        config.createSection("tiers.2.contents.3");
        config.set("tiers.2.contents.3.Material", "ARROW");
        config.set("tiers.2.contents.3.amount", 10);
        config.set("tiers.2.contents.3.chance", 50);

        try {
            config.save(tiersFile);
            plugin.getLogger().info("Example tiers configuration created at " + tiersFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createExampleMapConfig(File mapsDirectory, Plugin plugin) {
        File exampleFile = new File(mapsDirectory, "example-game.yml");
        if (!exampleFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(exampleFile);

            config.set("maxPlayers", 12);
            config.set("minPlayers", 4);
            config.set("gameTimeLimit", 300);
            config.set("world", "world");

            List<Map<String, Object>> spawnLocations = new ArrayList<>();
            spawnLocations.add(locationToMap(new Location(Bukkit.getWorld("world"), 0.5, 65, 0.5)));
            spawnLocations.add(locationToMap(new Location(Bukkit.getWorld("world"), 10.5, 65, 0.5)));
            config.set("spawnLocations", spawnLocations);

            List<Map<String, Object>> chestLocations = new ArrayList<>();
            chestLocations.add(locationAndTierToMap(new Location(Bukkit.getWorld("world"), 5.5, 65, 5.5), "1"));
            chestLocations.add(locationAndTierToMap(new Location(Bukkit.getWorld("world"), -5.5, 65, -5.5), "2"));
            config.set("chestLocations", chestLocations);

            try {
                config.save(exampleFile);
                plugin.getLogger().info("Example game configuration created at " + exampleFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMessage(String path){
        return color(plugin.getConfig().getString("messages." + path));
    }


    private Map<String, Object> locationToMap(Location location) {
        Map<String, Object> map = new HashMap<>();
        map.put("x", location.getX());
        map.put("y", location.getY());
        map.put("z", location.getZ());
        return map;
    }

    public Location getLobby(Plugin plugin){
        World world = Bukkit.getWorld(plugin.getConfig().getString("lobby-data.world"));
        String[] location_parts = plugin.getConfig().getString("lobby-data.location").split(",");
        double x = Double.parseDouble(location_parts[0]);
        double y = Double.parseDouble(location_parts[1]);
        double z = Double.parseDouble(location_parts[2]);

        return new Location(world, x, y, z);
    }

    private Map<String, Object> locationAndTierToMap(Location location, String tier) {
        Map<String, Object> map = locationToMap(location);
        map.put("tier", tier);
        return map;
    }

    public void playersKickArenaAll(Plugin plugin){
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerManager manager = ((Main) plugin).getPlayerManager(player);
            if (manager != null && manager.getGame() != null) {
                manager.getGame().playerManagers.remove(manager);
                player.teleport(getLobby(plugin));
            }
        }
    }
}
