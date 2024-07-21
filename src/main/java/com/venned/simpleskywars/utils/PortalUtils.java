package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PortalUtils {
    private final Main plugin;
    private Cuboid portalCuboid;

    public PortalUtils(Main plugin) {
        this.plugin = plugin;
        setPortalConfig();
    }

    public void reloadPortal(){
        setPortalConfig();
    }

    public void setPortalConfig(){
        String[] pos1 = plugin.getConfig().getString("lobby-data.location-portal.pos1").split(",");
        String[] pos2 = plugin.getConfig().getString("lobby-data.location-portal.pos2").split(",");
        Location loc1 = new Location(Bukkit.getWorld(plugin.getConfig().getString("lobby-data.world")),
                Double.parseDouble(pos1[0]), Double.parseDouble(pos1[1]), Double.parseDouble(pos1[2]));
        Location loc2 = new Location(Bukkit.getWorld(plugin.getConfig().getString("lobby-data.world")),
                Double.parseDouble(pos2[0]), Double.parseDouble(pos2[1]), Double.parseDouble(pos2[2]));
        portalCuboid = new Cuboid(loc1, loc2);
    }

    public Cuboid getPortalCuboid() {
        return portalCuboid;
    }
}
