package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.face.PlayerManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import org.bukkit.entity.Player;

public abstract class AbstractPlayer implements PlayerManager {

    protected Player player;
    protected String name;
    protected String name_map;
    protected SkyWarsGameManager game;

    public AbstractPlayer(Player player, SkyWarsGameManager game) {
        this.player = player;
        this.name = player.getName();
        this.name_map = game.name_map;
        this.game = game;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SkyWarsGameManager getGame() {
        return game;
    }

    @Override
    public String getName_Map() {
        return name_map;
    }


    @Override
    public int getCurrentPlayer() {
        return game.playerManagers.size();
    }
}
