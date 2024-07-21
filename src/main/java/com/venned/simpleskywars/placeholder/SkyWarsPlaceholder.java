package com.venned.simpleskywars.placeholder;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkyWarsPlaceholder extends PlaceholderExpansion {

    private final Main plugin;

    public SkyWarsPlaceholder(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "skywars";
    }

    @Override
    public String getAuthor() {
        return "Francisco";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }



    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        PlayerManager playerManager = plugin.getPlayerManager(player);


        if (params.equals("gamestate")) {
            if(playerManager == null){
                return "No Game";
            }
            return playerManager.getGame().getGameState().name();
        }

        if (params.equals("map")){
            if(playerManager == null){
                return "No Map";
            }
                return playerManager.getGame().getName_Map();
        }

        if (params.equals("min_players")){
            if(playerManager == null){
                return "No Map";
            }
                return String.valueOf(playerManager.getGame().getMinPlayers());
        }

        if (params.equals("current_players")){
            if(playerManager == null){
                return "No Map";
            }
            return String.valueOf(playerManager.getGame().getPlayersManager().size());
        }

        if (params.equals("max_players")){
            if(playerManager == null){
                return "No Map";
            }
                return String.valueOf(playerManager.getGame().getMaxPlayers());
        }


        return null;
    }
}
