package com.venned.simpleskywars.events;

import com.venned.simpleskywars.utils.AbstractGameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameWonEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractGameManager gameManager;
    private final Player player;

    public GameWonEvent(AbstractGameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractGameManager getGameManager() {
        return gameManager;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
