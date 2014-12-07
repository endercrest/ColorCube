package com.endercrest.colorcube.game;

import org.bukkit.Location;

import java.util.Random;

public class Powerup {

    public enum PType{
        SPLASH, SCATTER, FREEZE, SWAP
    }

    private Location location;
    private PType type;

    public Powerup(Location location){
        this.location = location;
        Random rand = new Random();
        int random = rand.nextInt((PType.values().length - 1) + 1) + 1;
        switch(random) {
            case 1:
                type = PType.SPLASH;
                break;
            case 2:
                type = PType.SCATTER;
                break;
            case 3:
                type = PType.FREEZE;
                break;
            case 4:
                type = PType.SWAP;
                break;
        }
    }

    public Location getLocation() {
        return location;
    }

    public PType getType() {
        return type;
    }
}
