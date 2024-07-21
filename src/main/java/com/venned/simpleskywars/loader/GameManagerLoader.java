package com.venned.simpleskywars.loader;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.face.TierItem;
import com.venned.simpleskywars.impl.TierItemImpl;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.utils.Utils;
import com.venned.simpleskywars.utils.UtilsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameManagerLoader {

    private final JavaPlugin plugin;
    private final List<SkyWarsGameManager> arenas;
    private final Map<Integer, List<TierItem>> tiers;
    private final Utils utils;
    private final UtilsConfig utilsConfig;

    public GameManagerLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        this.arenas = new ArrayList<>();
        this.tiers = new HashMap<>();
        this.utils = new Utils(plugin);
        this.utilsConfig = new UtilsConfig(plugin);

        loadTiers();
        loadArenas();
    }

    public void loadArenas() {
        File mapsDirectory = new File(plugin.getDataFolder(), "maps");

        if (!mapsDirectory.exists() || !mapsDirectory.isDirectory()) {
            mapsDirectory.mkdir();
            plugin.getLogger().warning("The maps directory does not exist or is not a directory!");
            utils.createExampleMapConfig(mapsDirectory, plugin);
        }

        File[] files = mapsDirectory.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return;
        }

        for (File file : files) {

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            String map_name = file.getName().replace(".yml", "");
            int maxPlayers = config.getInt("maxPlayers");
            int minPlayers = config.getInt("minPlayers");
            int gameTimeLimit = config.getInt("gameTimeLimit");
            World world = Bukkit.getWorld(config.getString("world"));

            List<Location> spawnLocations = new ArrayList<>();
            config.getMapList("spawnLocations").forEach(map -> {
                spawnLocations.add(mapToLocation(world, (Map<String, Object>) map));
            });

            Map<Location, Integer> chestLocations = new HashMap<>();
            config.getMapList("chestLocations").forEach(map -> {
                Location loc = mapToLocation(world, (Map<String, Object>) map);
                int tier = (Integer) map.get("tier");
                chestLocations.put(loc, tier);
            });

            SkyWarsGameManager gameManager = new SkyWarsGameManager(plugin, map_name, world, maxPlayers, minPlayers,
                    spawnLocations.toArray(new Location[0]), gameTimeLimit, chestLocations, this);

            arenas.add(gameManager);
        }
    }

    private void loadTiers() {
        File tiersFile = new File(plugin.getDataFolder(), "tiers.yml");
        if (!tiersFile.exists()) {
            utils.createExampleTiersConfig(tiersFile, plugin);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(tiersFile);
        for (String tierKey : config.getConfigurationSection("tiers").getKeys(false)) {
            List<TierItem> tierItems = new ArrayList<>();
            int tier = Integer.parseInt(tierKey);
            for (String itemKey : config.getConfigurationSection("tiers." + tierKey + ".contents").getKeys(false)) {
                String path = "tiers." + tierKey + ".contents." + itemKey;
                Material material = Material.valueOf(config.getString(path + ".Material"));
                int amount = config.getInt(path + ".amount");
                int chance = config.getInt(path + ".chance");

                List<Enchantment> enchantments = new ArrayList<>();
                int level = 0;
                if (config.contains(path + ".enchant")) {
                    for (String enchant : config.getStringList(path + ".enchant")) {
                        String[] enchantParts = enchant.split(":");
                        Enchantment enchantment = Enchantment.getByName(enchantParts[0]);
                        level = Integer.parseInt(enchantParts[1]);
                        enchantments.add(enchantment);
                    }
                }

                TierItem item = new TierItemImpl(material, amount, enchantments, chance, level);
                tierItems.add(item);
            }
            tiers.put(tier, tierItems);
        }
    }

    public void setSpawnPoint(String arenaName, int index, Location spawnLocation) {
        FileConfiguration config = utilsConfig.getArenaConfig(arenaName);
        if (config == null) return;

        List<Map<?, ?>> spawnLocations = config.getMapList("spawnLocations");
        List<Map<String, Object>> typedSpawnLocations = new ArrayList<>();

        for (Map<?, ?> spawnMap : spawnLocations) {
            if (spawnMap instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> typedSpawnMap = (Map<String, Object>) spawnMap;
                typedSpawnLocations.add(typedSpawnMap);
            }
        }

        if (index >= 0 && index < typedSpawnLocations.size()) {
            // Update existing spawn point
            typedSpawnLocations.set(index, locationToMap(spawnLocation));
        } else {
            // Add new spawn point
            typedSpawnLocations.add(locationToMap(spawnLocation));
        }

        config.set("spawnLocations", typedSpawnLocations);
        utilsConfig.saveArenaConfig(config, arenaName);
    }

    public void setChestLocation(String arenaName, int index, Location chestLocation, int tier) {
        FileConfiguration config = utilsConfig.getArenaConfig(arenaName);
        if (config == null) return;

        List<Map<?, ?>> chestLocations = config.getMapList("chestLocations");
        List<Map<String, Object>> typedChestLocations = new ArrayList<>();

        for (Map<?, ?> chestMap : chestLocations) {
            if (chestMap instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> typedChestMap = (Map<String, Object>) chestMap;
                typedChestLocations.add(typedChestMap);
            }
        }

        Map<String, Object> chestData = new HashMap<>();
        chestData.put("x", chestLocation.getX());
        chestData.put("y", chestLocation.getY());
        chestData.put("z", chestLocation.getZ());
        chestData.put("tier", tier);

        if (index >= 0 && index < typedChestLocations.size()) {
            typedChestLocations.set(index, chestData);
        } else {
            typedChestLocations.add(index, chestData);
        }

        config.set("chestLocations", typedChestLocations);
        utilsConfig.saveArenaConfig(config, arenaName);
    }


    public void reloadArenas() {
        arenas.clear();
        tiers.clear();

        loadTiers();
        loadArenas();
    }

    private Map<String, Object> locationToMap(Location location) {
        Map<String, Object> map = new HashMap<>();
        map.put("x", location.getX());
        map.put("y", location.getY());
        map.put("z", location.getZ());
        return map;
    }

    private Location mapToLocation(World world, Map<String, Object> map) {
        double x = (double) map.get("x");
        double y = (double) map.get("y");
        double z = (double) map.get("z");
        return new Location(world, x, y, z);
    }

    public List<SkyWarsGameManager> getArenas(){
        return arenas;
    }

    public SkyWarsGameManager getAvailableArena() {
        for (SkyWarsGameManager arena : arenas) {
            if (!arena.isFull() && !arena.isGameActive()) {
                return arena;
            }
        }
        return null;
    }

    public SkyWarsGameManager getMap(String mapName) {
        for (SkyWarsGameManager arena : arenas) {
            if(arena.getName_Map().equalsIgnoreCase(mapName)){
                if(!arena.isFull() && !arena.isGameActive() && !arena.isEndingGame() || !arena.isEndedGame()
                        || !arena.isEditing()){
                    return arena;
                }
            }
        }
        return null;
    }

    public SkyWarsGameManager getSuitableArena() {
        List<SkyWarsGameManager> arenasWithPlayers = new ArrayList<>();
        List<SkyWarsGameManager> emptyArenas = new ArrayList<>();

        for (SkyWarsGameManager arena : ((Main) plugin).getLoader().getArenas()) {
            if (!arena.isFull() && !arena.isGameActive() && !arena.isEndingGame() && !arena.isEndedGame() && !arena.isEditing()) {
                if (!arena.getPlayersManager().isEmpty()) {
                    arenasWithPlayers.add(arena);
                } else {
                    emptyArenas.add(arena);
                }
            }
        }

        if (!arenasWithPlayers.isEmpty()) {
            Collections.shuffle(arenasWithPlayers); // Mezcla las arenas con jugadores activos para una selección aleatoria
            return arenasWithPlayers.get(0);
        } else if (!emptyArenas.isEmpty()) {
            Collections.shuffle(emptyArenas); // Mezcla las arenas vacías para una selección aleatoria
            return emptyArenas.get(0);
        }

        return null;
    }


    public Map<Integer, List<TierItem>> getTiers() {
        return tiers;
    }

    public List<TierItem> getTierItems(int key){
        return tiers.get(key);
    }
}
