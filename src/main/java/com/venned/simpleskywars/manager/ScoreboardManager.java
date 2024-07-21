package com.venned.simpleskywars.manager;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.data.ScoreboardStorage;
import com.venned.simpleskywars.utils.AbstractScoreboardManager;
import com.venned.simpleskywars.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.*;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

public class ScoreboardManager extends AbstractScoreboardManager {

    @Override
    public void loadConfig(JavaPlugin plugin) {
        ScoreboardStorage.worldScoreboards = new HashMap<>();

        Set<String> arenaWorlds = new HashSet<>();
        ((Main) plugin).getLoader().getArenas().forEach(arena -> {
            String worldName = arena.getWorld().getName();
            arenaWorlds.add(worldName.toLowerCase());
        });

        for (String world : arenaWorlds) {
            List<String> scoreboardLines = plugin.getConfig().getStringList("scoreboards." + "game-world" + ".lines");
            Collections.reverse(scoreboardLines);
            ScoreboardStorage.worldScoreboards.put(world.toLowerCase(), scoreboardLines);

            String title = plugin.getConfig().getString("scoreboards." + "game-world" + ".title", "§fexample.de");
            ScoreboardStorage.worldTitles.put(world.toLowerCase(), Utils.color(title));
        }
        String world_lobby = plugin.getConfig().getString("lobby-data.world");
        List<String> scoreboardLines = plugin.getConfig().getStringList("scoreboards." + "lobby" + ".lines");
        Collections.reverse(scoreboardLines);

        String title = plugin.getConfig().getString("scoreboards." + "lobby" + ".title", "§fexample.de");
        ScoreboardStorage.worldScoreboards.put(world_lobby.toLowerCase(), scoreboardLines);
        ScoreboardStorage.worldTitles.put(world_lobby.toLowerCase(), Utils.color(title));
    }

    @Override
    public void setScoreboard(Player player) {
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective obj = scoreboard.registerObjective("zagd", IScoreboardCriteria.b);
        String title = ScoreboardStorage.worldTitles.getOrDefault(player.getWorld().getName().toLowerCase(), "§fexample.de");
        obj.setDisplayName(title);
        PacketPlayOutScoreboardObjective createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);

        Map<Integer, ScoreboardScore> scores = new HashMap<>();
        List<String> scoreboardLines = ScoreboardStorage.worldScoreboards.getOrDefault(player.getWorld().getName().toLowerCase(), null);
        if (scoreboardLines == null) {
            scoreboardLines = ScoreboardStorage.worldScoreboards.getOrDefault("default", null);
        }

        sendPacket(createPacket, player);
        sendPacket(display, player);
        if (scoreboardLines != null) {
            for (int i = 0; i < scoreboardLines.size(); i++) {
                String line = scoreboardLines.get(i);
                line = Utils.color(PlaceholderAPI.setPlaceholders(player, line));

                ScoreboardScore score = new ScoreboardScore(scoreboard, obj, line);
                score.setScore(i);
                scores.put(i, score);

                PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(score);
                sendPacket(packet, player);
            }
        }
        ScoreboardStorage.playerScoreboards.put(player, scoreboard);
        ScoreboardStorage.playerScores.put(player, scores);
    }


    @Override
    public void updateScore(Player player) {
        Scoreboard scoreboard = ScoreboardStorage.playerScoreboards.get(player);
        Map<Integer, ScoreboardScore> scoresMap = ScoreboardStorage.playerScores.get(player);

        if (scoreboard != null && scoresMap != null) {
            ScoreboardObjective obj = scoreboard.getObjective("zagd");
            if (obj != null) {
                List<String> scoreboardLines = ScoreboardStorage.worldScoreboards.getOrDefault(player.getWorld().getName().toLowerCase(), null);

                if(scoreboardLines == null) return;
                for (int i = 0; i < scoreboardLines.size(); i++) {
                    String line = scoreboardLines.get(i);

                    if (line.contains("%ping%") || line.contains("%player_name%") || PlaceholderAPI.containsPlaceholders(line)) {
                        ScoreboardScore score = scoresMap.get(i);

                        try {
                            PacketPlayOutScoreboardScore removePacket = new PacketPlayOutScoreboardScore(score);
                            Field dField = removePacket.getClass().getDeclaredField("d");
                            dField.setAccessible(true);
                            dField.set(removePacket, PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE);

                            sendPacket(removePacket, player);
                            scoresMap.remove(i);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        line = Utils.color(PlaceholderAPI.setPlaceholders(player, line));

                        line = line.replace("%ping%", String.valueOf(((CraftPlayer) player).getHandle().ping));
                        line = line.replace("%player_name%", player.getName());

                        ScoreboardScore newScoreboardScore = new ScoreboardScore(scoreboard, obj, line);
                        newScoreboardScore.setScore(i);

                        scoresMap.put(i, newScoreboardScore);

                        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(newScoreboardScore);
                        sendPacket(packet, player);
                    }
                }
            } else {
                System.out.println("The objective 'zagd' does not exist in the scoreboard.");
            }
        } else {
            System.out.println("The player does not have an assigned scoreboard or their scores are not initialized.");
        }
    }

    @Override
    public void changeWorld(Player player) {
        removeScoreboard(player);
        if (ScoreboardStorage.playerScoreboards.containsKey(player)) {
            ScoreboardStorage.playerScoreboards.remove(player);
        }
        if (ScoreboardStorage.playerScores.containsKey(player)) {
            ScoreboardStorage.playerScores.remove(player);
        }
        setScoreboard(player);
    }

    public void reloadScoreboards(JavaPlugin plugin) {
        for(Player player : ScoreboardStorage.playerScoreboards.keySet()){
            removeScoreboard(player);
            ScoreboardStorage.playerScoreboards.remove(player);
            ScoreboardStorage.playerScores.remove(player);
        }
        ScoreboardStorage.worldTitles.clear();
        ScoreboardStorage.worldScoreboards.clear();
        plugin.reloadConfig();
        loadConfig(plugin);
        for(Player player : Bukkit.getOnlinePlayers()){
            setScoreboard(player);
        }
    }
}