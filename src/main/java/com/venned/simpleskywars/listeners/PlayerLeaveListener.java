package com.venned.simpleskywars.listeners;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final Main plugin;
    private final Utils utils;

    public PlayerLeaveListener(Main plugin) {
        this.plugin = plugin;
        utils = new Utils(plugin);
    }


    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Location lobby = utils.getLobby(plugin);
        PlayerManager playerManager = plugin.getPlayerManager(player);
        if(playerManager == null) return;
        player.getInventory().clear();
        player.teleport(lobby);
        playerManager.getGame().removePlayer(playerManager);
    }
}
