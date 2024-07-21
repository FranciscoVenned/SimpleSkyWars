package com.venned.simpleskywars.commands.subcommand;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PortalCommand {

    private final Plugin plugin;

    public PortalCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setPos1(Player player) {
        String locString = locationToString(player.getLocation());
        plugin.getConfig().set("lobby-data.location-portal.pos1", locString);
        plugin.saveConfig();
        player.sendMessage("§c§l(!) §7Portal Pos1 set to: " + locString);
    }

    public void setPos2(Player player) {
        String locString = locationToString(player.getLocation());
        plugin.getConfig().set("lobby-data.location-portal.pos2", locString);
        plugin.saveConfig();
        player.sendMessage("§c§l(!) §7Portal Pos2 set to: " + locString);
    }

    public String locationToString(Location loc) {
        return loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }
}
