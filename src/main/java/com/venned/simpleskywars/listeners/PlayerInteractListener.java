package com.venned.simpleskywars.listeners;

import com.venned.simpleskywars.enums.GameState;
import com.venned.simpleskywars.events.PlayerInteractArenaEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;


public class PlayerInteractListener implements Listener {

    private final Plugin plugin;

    public PlayerInteractListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerInteract(PlayerInteractArenaEvent event){
        if(event.getPlayerManager().getGame().getGameState() == GameState.STARTING ||
            event.getPlayerManager().getGame().getGameState() == GameState.WAITING){
            event.setCancelled(true);
        }
    }

}
