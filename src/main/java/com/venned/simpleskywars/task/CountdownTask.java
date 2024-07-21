package com.venned.simpleskywars.task;

import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CountdownTask {
    private final Plugin plugin;
    private final int countdownTime;
    private final Runnable onFinish;
    private final Runnable onCancel;
    private final SkyWarsGameManager gameManager;
    private final Utils utils;

    public CountdownTask(Plugin plugin, int countdownTime, SkyWarsGameManager gameManager, Runnable onFinish, Runnable onCancel) {
        this.plugin = plugin;
        this.countdownTime = countdownTime;
        this.gameManager = gameManager;
        this.onFinish = onFinish;
        this.onCancel = onCancel;
        this.utils = new Utils(plugin);
    }

    public void start() {
        new BukkitRunnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    gameManager.getPlayersManager().forEach(playerManager -> {
                        playerManager.getPlayer().sendMessage(utils.getMessage("game-starting").replace(
                                "%time_left%", String.valueOf(timeLeft)
                        ));
                        playerManager.getPlayer().playSound(playerManager.getPlayer().getLocation(),
                                Sound.NOTE_BASS, 1.5f, 1.5f);
                    });
                    if (onCancel != null && gameManager.getPlayersManager().size() < gameManager.getMinPlayers()) {
                        onCancel.run();
                        cancel();
                        return;
                    }
                    timeLeft--;
                } else {
                    onFinish.run();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20L = 1 segundo
    }
}