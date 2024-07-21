package com.venned.simpleskywars.events;


import com.venned.simpleskywars.utils.AbstractPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractArenaEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final AbstractPlayer playerManager;
    private boolean cancelled = false;

    public PlayerInteractArenaEvent(final AbstractPlayer playerManager) {
        this.playerManager = playerManager;
    }

    public AbstractPlayer getPlayerManager() {
        return playerManager;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
