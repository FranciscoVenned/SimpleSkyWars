package com.venned.simpleskywars.commands.subcommand;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.enums.GameState;
import com.venned.simpleskywars.loader.GameManagerLoader;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.utils.MapUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EditCommand {

    private final Main plugin;
    private final MapUtils mapUtils;

    public EditCommand(Main plugin, MapUtils mapUtils) {
        this.plugin = plugin;
        this.mapUtils = mapUtils;
    }

    public void EditCommands(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§c§l(!) §7Usage /skywars edit <arena>");
            return;
        }

        // Check for 'setspawnpoint' command
        if (args[1].equalsIgnoreCase("setspawnpoint")) {
            if (args.length < 3) {
                player.sendMessage("§c§l(!) §7Usage /skywars edit setspawnpoint <index>");
                return;
            }
            setSpawnPoint(player, args);
            return;
        }

        // Check for 'setchest' command
        if (args[1].equalsIgnoreCase("setchest")) {
            if (args.length < 4) {
                player.sendMessage("§c§l(!) §7Usage /skywars edit setchest <index> <tier>");
                return;
            }
            setChestPoint(player, args);
            return;
        }

        // Check for main 'edit' command
        if (args[0].equalsIgnoreCase("edit")) {
            activeEdit(player, args);
            return;
        }

        if (args[1].equalsIgnoreCase("saveall")){
            player.sendMessage("§c§l(!) §aSaving Changes");
            mapUtils.getPlayer_editing().remove(player.getUniqueId());
            return;
        }


        // Handle unknown subcommands or incorrect usage
        player.sendMessage("§c§l(!) §7Sub Command Unknow");
    }


    public void activeEdit(Player player, String[] args){
        if(args.length < 2){
            player.sendMessage("§c§l(!) §7Usage /skywars edit <arena>");
            return;
        }
        SkyWarsGameManager map = plugin.getLoader().getMap(args[1]);
        if (map == null) return;

        GameState gameState = map.getGameState();
        if (gameState == GameState.STARTING || gameState == GameState.IN_PROGRESS
                || map.isEndedGame() || map.isEndingGame()) {
            player.sendMessage("§c§l(!) §7You cannot edit an arena in Progress.");
            return;
        }
        mapUtils.setPlayer_editing(player.getUniqueId(), map);
        map.setEditingMode();
        Location location_world = map.getWorld().getSpawnLocation();
        player.teleport(location_world);
    }

    public void setSpawnPoint(Player player, String[] args){
            if (!mapUtils.getPlayer_editing().containsKey(player.getUniqueId())) {
                player.sendMessage("§c§l(!) §7You must select a map to edit before doing this.");
                return;
            }
            int index;
            try {
                index = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage("§c§l(!) §7Number Invalid");
                return;
            }
            Location location_spawn = player.getLocation();
            SkyWarsGameManager skyWarsGameManager = mapUtils.getPlayer_editing().get(player.getUniqueId());
            GameManagerLoader gameManagerLoader = plugin.getLoader();
            gameManagerLoader.setSpawnPoint(skyWarsGameManager.getName_Map(), index, location_spawn);
            player.sendMessage("§c§l(!) §7SetSpawn " + index + " established successfully!");
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.5f, 1.5f);

    }

    public void setChestPoint(Player player, String[] args){
            if (!mapUtils.getPlayer_editing().containsKey(player.getUniqueId())) {
                player.sendMessage("§c§l(!) §7You must select a map to edit before doing this.");
                return;
            }
            int index;
            int tier;
            try {
                index = Integer.parseInt(args[2]);
                tier = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                player.sendMessage("§c§l(!) §7Number Invalid");
                return;
            }
            Location location_spawn = player.getLocation();
            SkyWarsGameManager skyWarsGameManager = mapUtils.getPlayer_editing().get(player.getUniqueId());
            GameManagerLoader gameManagerLoader = plugin.getLoader();
            gameManagerLoader.setChestLocation(skyWarsGameManager.getName_Map(), index, location_spawn, tier);
        player.sendMessage("§c§l(!) §7SetChest " + index + " established successfully!");
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.5f, 1.5f);

    }
}
