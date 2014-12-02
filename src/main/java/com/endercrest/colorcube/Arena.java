package com.endercrest.colorcube;

import org.bukkit.Location;

public class Arena {

    private Location pos1;//MAX
    private Location pos2;//MIN

    public Arena(Location pos1, Location pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    /**
     * Maximum point
     * @return The Maximum point(pos1)
     */
    public Location getPos1(){
        return pos1;
    }

    /**
     * Minimum Point
     * @return The Minimum point(pos2)
     */
    public Location getPos2(){
        return pos2;
    }

    public boolean containsBlock(Location pos){
        if(pos.getWorld() != pos2.getWorld())
            return false;
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        MessageManager.getInstance().debugConsole("X:" + x + " Y:" + y + " Z:" + z + " World:" + pos.getWorld().getName());
        return x >= pos2.getBlockX() && x < pos1.getBlockX() + 1 && y >= pos2.getBlockY() && y < pos1.getBlockY() + 1 && z >= pos2.getBlockZ() && z < pos1.getBlockZ() + 1;
    }
}
