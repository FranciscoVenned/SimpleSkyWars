package com.venned.simpleskywars.face;

import com.venned.simpleskywars.enums.GameState;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import org.bukkit.entity.Player;

public interface PlayerManager {
    String getName();
    String getName_Map();
    SkyWarsGameManager getGame();
    int getCurrentPlayer();
}
