package com.venned.simpleskywars.listeners;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class PlayerKillListener implements Listener {

    private final Main plugin;
    private final Utils utils;

    public PlayerKillListener(Main plugin) {
        this.plugin = plugin;
        this.utils = new Utils(plugin);
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        PlayerManager playerManager = plugin.getPlayerManager(e.getEntity());
        if (playerManager == null) return;
        SkyWarsGameManager skyWarsGameManager = playerManager.getGame();
        if (skyWarsGameManager == null) return;

        Player deceased = e.getEntity();
        Player killer = deceased.getKiller();

        for (PlayerManager playerManagers : skyWarsGameManager.getPlayersManager()) {
            Player player = playerManagers.getPlayer();
            if (killer != null) {
                player.playSound(player.getLocation(), Sound.CAT_HISS, 1.5f, 1.5f);
                player.sendMessage(utils.getMessage("player-kill").replace("%player_name%", deceased.getName())
                                .replace("%killer_name%", killer.getName()));
            } else {
                player.sendMessage("§6§l(!) §ePlayer §c" + deceased.getName() + " §ewas killed.");
            }
        }
    }
}
