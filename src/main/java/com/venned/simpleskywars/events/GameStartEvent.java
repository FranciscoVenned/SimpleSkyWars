package com.venned.simpleskywars.events;

import com.venned.simpleskywars.utils.AbstractGameManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class GameStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractGameManager gameManager;

    public GameStartEvent(AbstractGameManager gameManager) {
        this.gameManager = gameManager;
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
