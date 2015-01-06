package com.endercrest.colorcube.game;

import org.bukkit.Location;

public class LobbyWall {

    private Location pos1; //MAX
    private Location pos2; //MIN

    public LobbyWall(Location pos1, Location pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }
}
