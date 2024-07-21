package com.venned.simpleskywars.listeners;


import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final Utils utils;
    protected Main plugin;

    public PlayerDeathListener(Main plugin){
        this.plugin = plugin;
        utils = new Utils(plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerManager playerManager = plugin.getPlayerManager(p.getPlayer());
        if(playerManager == null) return;
        playerManager.getGame().removePlayer(playerManager);
        Location lobby = utils.getLobby(plugin);
        if (lobby != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                p.spigot().respawn();
                p.teleport(lobby);
                p.getInventory().clear();
            }, 30L);
        }
    }
}
