package com.endercrest.colorcube.game;

import org.bukkit.Location;

/**
 * Created by Thomas Cordua-von Specht on 12/23/2016.
 *
 * A region defined by a maximum point and a minimum point.
 */
public class Region {

    private Location max;
    private Location min;

    public Region(Location max, Location min){
        this.max = max;
        this.min = min;
    }

    /**
     * Get the maximum location.
     * @return The max location.
     */
    public Location getMax() {
        return max;
    }

    /**
     * Get the minimum location.
     * @return The min location.
     */
    public Location getMin() {
        return min;
    }

    /**
     * Checks whether pos resides inside of the region.
     * @param pos The position to be checked.
     * @return Returns true if inside of region, returns false if outside or if in a different world.
     */
    public boolean containsBlock(Location pos){
        if(pos.getWorld() != min.getWorld())
            return false;
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        return x >= min.getBlockX() && x < max.getBlockX() + 1 && y >= min.getBlockY() && y < max.getBlockY() + 1 && z >= min.getBlockZ() && z < max.getBlockZ() + 1;
    }

    /**
     * Get the centre location of the region.
     * @return The centre location.
     */
    public Location getCentre(){
        double xRadius = (max.getBlockX() - min.getBlockX())/2;
        double yRadius = (max.getBlockY() - min.getBlockY())/2;
        double zRadius = (max.getBlockZ() - min.getBlockZ())/2;

        return new Location(min.getWorld(), min.getBlockX() + xRadius + 0.5, min.getBlockY() + yRadius, min.getBlockZ() + zRadius + 0.5);
    }

    /**
     * Gets the "radius" of the region. Since this shape might not be square, this radius will return the larger of the two.
     * @return The "radius"
     */
    public double getRadius(){
        return Math.max((max.getBlockX() - min.getBlockX())/2, (max.getBlockZ() - min.getBlockZ())/2);
    }


}
