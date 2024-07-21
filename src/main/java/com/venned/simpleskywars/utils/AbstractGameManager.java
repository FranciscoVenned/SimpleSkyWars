package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.enums.GameState;
import com.venned.simpleskywars.events.GameWonEvent;
import com.venned.simpleskywars.face.IGameManager;
import com.venned.simpleskywars.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;
import java.util.Map;

public abstract class AbstractGameManager implements IGameManager {

    protected GameState gameState = GameState.WAITING;
    protected int maxPlayers;
    protected int minPlayers;
    protected Location[] spawnLocations;
    protected int gameTimeLimit;
    protected Map<Location, Integer> chestLocations;
    public World world;
    protected String name_map;
    protected List<PlayerManager> playerManagers;



    public AbstractGameManager(String name_map, World world, int maxPlayers, int minPlayers, Location[] spawnLocations, int gameTimeLimit, Map<Location, Integer> chestLocations) {
        this.name_map = name_map;
        this.world = world;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.spawnLocations = spawnLocations;
        this.gameTimeLimit = gameTimeLimit;
        this.chestLocations = chestLocations;

    }

    public AbstractGameManager(AbstractGameManager existingManager) {
        this.name_map = existingManager.name_map;
        this.world = existingManager.world;
        this.maxPlayers = existingManager.maxPlayers;
        this.minPlayers = existingManager.minPlayers;
        this.spawnLocations = existingManager.spawnLocations;
        this.gameTimeLimit = existingManager.gameTimeLimit;
        this.chestLocations = existingManager.chestLocations;
        this.playerManagers = existingManager.playerManagers;
        this.gameState = existingManager.gameState;
    }

    @Override
    public String getName_Map(){
        return name_map;
    }

    @Override
    public World getWorld(){
        return this.world;
    }



    @Override
    public void addPlayer(PlayerManager manager) {
        if (playerManagers.size() < maxPlayers) {
            playerManagers.add(manager);
            Location spawnLocation = spawnLocations[playerManagers.size() - 1];

            spawnLocation.setWorld(world);

            manager.getPlayer().teleport(spawnLocation);
        }
    }

    @Override
    public void checkGameStatus() {
        if (gameState == GameState.IN_PROGRESS) {
            if (playerManagers.size() <= 1) {
                PlayerManager winner = playerManagers.isEmpty() ? null : playerManagers.get(0);
                if (winner != null) {
                    GameWonEvent gameWon = new GameWonEvent(this, winner.player);
                    Bukkit.getServer().getPluginManager().callEvent(gameWon);
                }
                endGame();
            }
        }
    }

    @Override
    public void setup(Location loc) {
        World world = this.world;

        int[][] offsets = {
                {0, -1, 0},
                {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1},
                {1, 1, 0}, {-1, 1, 0}, {0, 1, 1}, {0, 1, -1},
                {1, 2, 0}, {-1, 2, 0}, {0, 2, 1}, {0, 2, -1},
                {0, 3, 0}
        };

        for (int[] offset : offsets) {
            Location blockLocation = loc.clone().add(offset[0], offset[1], offset[2]);
            world.getBlockAt(blockLocation).setType(Material.GLASS);
        }
    }

    public int getGameTimeLimit() {
        return gameTimeLimit;
    }
    @Override
    public void removePlayer(PlayerManager player) {
        playerManagers.remove(player);
    }

    @Override
    public int getMinPlayers(){
        return minPlayers;
    }

    @Override
    public int getMaxPlayers(){
        return maxPlayers;
    }

    @Override
    public boolean isFull() {
        return playerManagers.size() >= maxPlayers;
    }

    @Override
    public boolean isGameActive() {
        return gameState == GameState.IN_PROGRESS;
    }

    @Override
    public boolean isEditing(){
        return gameState == GameState.EDITING;
    }

    @Override
    public boolean isEndedGame(){
        return gameState == GameState.ENDED;
    }

    @Override
    public boolean isEndingGame(){
        return gameState == GameState.ENDING;
    }

    protected void setGameState(GameState newState) {
        this.gameState = newState;
    }

    @Override
    public abstract void startGame();

    @Override
    public Map<Location, Integer> getChestLocations() {
        return chestLocations;
    }

    @Override
    public GameState getGameState(){
        return gameState;
    }

    @Override
    public abstract void endGame();

    @Override
    public List<PlayerManager> getPlayersManager() {
        return playerManagers;
    }
}
