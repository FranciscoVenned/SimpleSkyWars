package com.venned.simpleskywars.commands;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.commands.subcommand.EditCommand;
import com.venned.simpleskywars.commands.subcommand.PortalCommand;
import com.venned.simpleskywars.loader.GameManagerLoader;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.ScoreboardManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.utils.MapUtils;
import com.venned.simpleskywars.utils.PortalUtils;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final Main plugin;
    private final Utils utils;
    private final ScoreboardManager scoreboardManager;
    private final MapUtils mapUtils;
    private final PortalUtils portalUtils;

    private final EditCommand editCommand;
    private final PortalCommand portalCommand;

    public MainCommand(Main plugin, MapUtils mapUtils, PortalUtils portalUtils) {
        this.plugin = plugin;
        this.scoreboardManager = new ScoreboardManager();
        this.utils = new Utils(plugin);
        this.mapUtils = mapUtils;
        this.portalUtils = portalUtils;

        this.portalCommand = new PortalCommand(plugin);
        this.editCommand = new EditCommand(plugin, mapUtils);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length == 0) {
            player.sendMessage("§c§l(!) §7Sub-Commands: §8join, leave");
            return true;
        }
        if (args[0].equalsIgnoreCase("join")) {
            SkyWarsGameManager map = plugin.getLoader().getSuitableArena();
            if (map == null) return true;
            map.joinArena(player);
            return true;
        }

        if(args[0].equalsIgnoreCase("leave")){
            PlayerManager playerManager = plugin.getPlayerManager(player);
            if(playerManager == null) return true;
            playerManager.getGame().removePlayer(playerManager);
            Location lobby = utils.getLobby(plugin);
            player.teleport(lobby);
            player.sendMessage(utils.getMessage("player-leave"));
        }
        if(args[0].equalsIgnoreCase("reload")){
            GameManagerLoader loader = plugin.getLoader();
            utils.playersKickArenaAll(plugin);
            loader.reloadArenas();
            portalUtils.reloadPortal();
            scoreboardManager.reloadScoreboards(plugin);
            player.sendMessage(utils.getMessage("plugin-reload"));
        }
        if(args[0].equalsIgnoreCase("setportal")){
            if(args.length < 2){
                player.sendMessage("§c§l(!) §7Usage /skywars setportal setpos1 / setpos2");
                return true;
            }
           if(args[1].equalsIgnoreCase("setpos1")){
               portalCommand.setPos1(player);
               return true;
           }
           if(args[1].equalsIgnoreCase("setpos2")){
               portalCommand.setPos2(player);
               return true;
           }
        }
        if(args[0].equalsIgnoreCase("edit")){
           editCommand.EditCommands(player, args);
           return true;
        }
        return false;
    }

}