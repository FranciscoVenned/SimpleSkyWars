package com.venned.simpleskywars.events;

import com.venned.simpleskywars.manager.PlayerManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinArenaEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final PlayerManager player;

    public PlayerJoinArenaEvent(PlayerManager player) {
        this.player = player;
    }

    public PlayerManager getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
