package com.venned.simpleskywars.data;

import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardScore;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardStorage {
    public static final Map<Player, Scoreboard> playerScoreboards = new ConcurrentHashMap<>();
    public static final Map<Player, Map<Integer, ScoreboardScore>> playerScores = new HashMap<>();
    public static Map<String, List<String>> worldScoreboards;
    public static Map<String, String> worldTitles = new HashMap<>();
}