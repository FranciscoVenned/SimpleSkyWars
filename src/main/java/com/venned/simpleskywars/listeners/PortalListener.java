package com.venned.simpleskywars.listeners;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.utils.Cuboid;
import com.venned.simpleskywars.utils.PortalUtils;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalListener implements Listener {

    private final Main plugin;
    private final Utils utils;
    private final PortalUtils portalUtils;

    public PortalListener(Main plugin, PortalUtils portalUtils) {
        this.plugin = plugin;
        this.utils = new Utils(plugin);
        this.portalUtils = portalUtils;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        if (portalUtils.getPortalCuboid().isInCuboid(to)) {
            SkyWarsGameManager map = plugin.getLoader().getSuitableArena();
            if (map != null) {
                map.joinArena(player);
            } else {
                player.sendMessage("§c§l(!) §cThere are no arenas available, please wait a moment.");
                Location lobby = utils.getLobby(plugin);
                player.teleport(lobby);
            }
        }
    }
}
