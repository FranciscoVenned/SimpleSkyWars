package com.venned.simpleskywars.listeners;

import com.venned.simpleskywars.events.GameWonEvent;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameWonListener implements Listener {

    private final Plugin plugin;
    private final Utils utils;

    public GameWonListener(Plugin plugin) {
        this.plugin = plugin;
        this.utils = new Utils(plugin);
    }

    @EventHandler
    public void wonGame(GameWonEvent event){
        event.getGameManager().getPlayersManager().forEach(player -> {
            player.getPlayer().sendMessage(utils.getMessage("game-won").replace(
                    "%player_name%", event.getPlayer().getName()
            ));
        });
        Player player = event.getPlayer();
        launchFireworks(player);
    }

    private void launchFireworks(Player player) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= 25) { // 3 seconds, 5 fireworks per second
                    this.cancel();
                    return;
                }
                spawnFirework(player.getLocation());
                count++;
            }
        }.runTaskTimer(plugin, 0L, 4L); // 4L ticks = 0.2 seconds
    }

    private void spawnFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.RED, Color.BLUE, Color.GREEN)
                .with(FireworkEffect.Type.BALL)
                .trail(true)
                .flicker(true)
                .build();
        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);
    }
}
