package com.venned.simpleskywars.manager;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimeProperty;
import com.grinderwolf.swm.plugin.SWMPlugin;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import com.grinderwolf.swm.plugin.config.WorldsConfig;
import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.enums.GameState;
import com.venned.simpleskywars.events.GameStartEvent;
import com.venned.simpleskywars.events.PlayerJoinArenaEvent;
import com.venned.simpleskywars.face.TierItem;
import com.venned.simpleskywars.loader.GameManagerLoader;
import com.venned.simpleskywars.task.CountdownTask;
import com.venned.simpleskywars.utils.AbstractGameManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SkyWarsGameManager extends AbstractGameManager {

    public JavaPlugin plugin;
    private GameManagerLoader loader;
    private BukkitRunnable gameStatusTask;
    private SpectatorManager spectatorManager;
    private Utils utils;

    public SkyWarsGameManager(JavaPlugin plugin,String name_map, World world, int maxPlayers, int minPlayers, Location[] spawnLocations, int gameTimeLimit,  Map<Location, Integer> chestLocations, GameManagerLoader loader) {
        super(name_map, world, maxPlayers, minPlayers, spawnLocations, gameTimeLimit, chestLocations);
        this.plugin = plugin;
        this.world = world;
        this.playerManagers = new ArrayList<>();
        this.loader = loader;
        this.utils = new Utils(plugin);
        this.spectatorManager = new SpectatorManager(this);
        for (Location spawnLocation : spawnLocations) {
            setup(spawnLocation);
        }
    }

    public SkyWarsGameManager(AbstractGameManager gameManager){
        super(gameManager);
    }

    @Override
    public void startGame() {
        if (gameState == GameState.STARTING || playerManagers.size() < minPlayers) {
            return;
        }

        setGameState(GameState.STARTING);

        new CountdownTask(plugin, 10, this, () -> {
            setGameState(GameState.IN_PROGRESS);
            GameStartEvent gameStartEvent = new GameStartEvent(this);
            Bukkit.getServer().getPluginManager().callEvent(gameStartEvent);
            fillChests();
            startGameStatusTask();
        }, () -> {
            setGameState(GameState.WAITING);
            playerManagers.forEach(player -> {
                player.getPlayer().sendMessage(utils.getMessage("insufficient-players"));
            });
        }).start();
    }

    @Override
    public void endGame() {
        setGameState(GameState.ENDING);
        if (gameStatusTask != null) {
            gameStatusTask.cancel();
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            setGameState(GameState.ENDED);
            playerManagers.forEach(player -> {
                player.getPlayer().getInventory().clear();
            });
            spectatorManager.playerTeleportLobby(plugin);
            playerManagers.clear();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {Bukkit.unloadWorld(world, true);}, 20L * 5);
        }, 20L * 10);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            reloadWorld();
        }, 20L * 30);
    }

    private void reloadWorld() {
        WorldsConfig config = ConfigManager.getWorldConfig();
        WorldData worldData = config.getWorlds().get(world.getName());
        if (worldData != null) {
            try {
                SlimeLoader loader = SWMPlugin.getInstance().getLoader(worldData.getDataSource());
                SlimeWorld slimeWorld = ((Main) plugin).getSlimePlugin().loadWorld(loader, world.getName(), true, worldData.toPropertyMap());
                SWMPlugin.getInstance().generateWorld(slimeWorld);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    this.world = Bukkit.getWorld(slimeWorld.getName());
                    for (Location spawnLocation : spawnLocations) {
                        setup(spawnLocation);
                    }
                    setGameState(GameState.WAITING);
                }, 20L * 10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void startGameStatusTask() {
        gameStatusTask = new BukkitRunnable() {
            private int timeLeft = getGameTimeLimit();

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    endGame();
                    cancel();
                } else {
                    checkGameStatus();
                    timeLeft--;
                }
            }
        };
        gameStatusTask.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }


    private void fillChests() {
        Map<Location, Integer> chestLocations = getChestLocations();

        for (Map.Entry<Location, Integer> entry : chestLocations.entrySet()) {
            Location loc = entry.getKey();
            loc.setWorld(world);
            int tierKey = entry.getValue();

            List<TierItem> tierItems = loader.getTierItems(tierKey);

            if (tierItems == null) {
                Bukkit.getLogger().info("Not Found TierItems");
                continue;
            }

            Block block = loc.getBlock();
            if (block.getType() == Material.AIR) {
                block.setType(Material.CHEST);
                Chest chest = (Chest) block.getState();
                Inventory chestInventory = chest.getInventory();

                for (TierItem item : tierItems) {
                    if (Math.random() < (item.getChance() / 100.0)) {
                        ItemStack itemStack = new ItemStack(item.getMaterial(), item.getAmount());
                        for (Enchantment enchantment : item.getEnchantments()) {
                            itemStack.addEnchantment(enchantment, item.getEnchantmentLevel(enchantment));
                        }
                        chestInventory.addItem(itemStack);
                    //    System.out.println("Added item: " + itemStack.getType() + " x" + itemStack.getAmount() + " to chest at " + loc);
                    }
                }
            } else {
                System.out.println("Could not place chest at location " + loc + ". Block is not air.");
            }
        }
    }

    public void joinArena(Player player) {
        if (gameState == GameState.WAITING || gameState == GameState.STARTING) {
            PlayerManager playerManager = new PlayerManager(player, this);
            addPlayer(playerManager);

            playerManagers.forEach(players -> {
                players.getPlayer().sendMessage(utils.getMessage("player-join").replace("%current_players%", String.valueOf(playerManagers.size()))
                        .replace("%max_players%", String.valueOf(getMaxPlayers()))
                        .replace("%player_name%", player.getName()));
                players.getPlayer().playSound(players.getPlayer().getLocation(), Sound.VILLAGER_YES, 1.5f, 1.5f);
            });

            PlayerJoinArenaEvent playerJoinArenaEvent = new PlayerJoinArenaEvent(playerManager);
            Bukkit.getServer().getPluginManager().callEvent(playerJoinArenaEvent);

            if (gameState == GameState.WAITING && playerManagers.size() == minPlayers) {
                startGame();
            }
        } else {
            player.sendMessage(utils.getMessage("game-not-joinable"));
        }
    }

    public void setEditingMode(){
        this.gameState = GameState.EDITING;
    }

    public SpectatorManager getSpectatorManager() {
        return spectatorManager;
    }
}
