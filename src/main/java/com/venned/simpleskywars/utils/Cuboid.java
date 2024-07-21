package com.venned.simpleskywars.utils;

import org.bukkit.Location;

public class Cuboid {

    private final Location pos1;
    private final Location pos2;

    public Cuboid(Location pos1, Location pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public boolean isInCuboid(Location loc) {
        int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        return loc.getBlockX() >= x1 && loc.getBlockX() <= x2
                && loc.getBlockY() >= y1 && loc.getBlockY() <= y2
                && loc.getBlockZ() >= z1 && loc.getBlockZ() <= z2;
    }

}
