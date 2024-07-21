package com.venned.simpleskywars.listeners;

import com.venned.simpleskywars.events.GameStartEvent;
import com.venned.simpleskywars.manager.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class GameStartListener implements Listener {

    @EventHandler
    public void gameStart(GameStartEvent event){
        List<PlayerManager> players = event.getGameManager().getPlayersManager();
        players.forEach(playerManager -> {
            Player player = playerManager.getPlayer();
            Location playerLocation = player.getLocation();

            Location blockBelowLocation = playerLocation.clone().add(0, -1, 0);

            if (blockBelowLocation.getBlock().getType() == Material.GLASS) {
                blockBelowLocation.getBlock().setType(Material.AIR);
            }
        });
    }
}
