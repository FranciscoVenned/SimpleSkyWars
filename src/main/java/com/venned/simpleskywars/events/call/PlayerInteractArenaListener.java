package com.venned.simpleskywars.events.call;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.events.PlayerInteractArenaEvent;
import com.venned.simpleskywars.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractArenaListener implements Listener {

    private final Main plugin;

    public PlayerInteractArenaListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerManager = plugin.getPlayerManager(player);
        if(playerManager == null) return;
        PlayerInteractArenaEvent event_interact = new PlayerInteractArenaEvent(playerManager);
        Bukkit.getPluginManager().callEvent(event_interact);
        if(event_interact.isCancelled()){
            event.setCancelled(true);
        }
    }
}
