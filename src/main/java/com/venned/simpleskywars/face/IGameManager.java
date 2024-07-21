package com.venned.simpleskywars.face;

import com.venned.simpleskywars.enums.GameState;
import com.venned.simpleskywars.manager.PlayerManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface IGameManager {
    String getName_Map();
    void setup(Location location);
    void startGame();
    GameState getGameState();
    void endGame();
    boolean isFull();
    boolean isEndedGame();
    boolean isEndingGame();
    boolean isEditing();
    int getMinPlayers();
    int getMaxPlayers();
    void addPlayer(PlayerManager player);
    void removePlayer(PlayerManager player);
    void checkGameStatus();
    List<PlayerManager> getPlayersManager();
    boolean isGameActive();
    World getWorld();
    Map<Location, Integer> getChestLocations();
}