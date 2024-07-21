package com.venned.simpleskywars.face;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface IScoreboardManager {
    void loadConfig(JavaPlugin plugin);
    void setScoreboard(Player player);
    void updateScore(Player player);
    void changeWorld(Player player);
}
