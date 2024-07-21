package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.manager.SkyWarsGameManager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapUtils {

    public Map<UUID, SkyWarsGameManager> player_editing = new ConcurrentHashMap<>();

    public Map<UUID, SkyWarsGameManager> getPlayer_editing() {
        return player_editing;
    }

    public void setPlayer_editing(UUID player_editing, SkyWarsGameManager gameManager) {
        this.player_editing.put(player_editing, gameManager);
    }
}
