package com.endercrest.colorcube.game;

import com.endercrest.colorcube.PowerupManager;
import com.endercrest.colorcube.powerups.SubPowerup;
import org.bukkit.Location;

public class Powerup {

    private Location location;
    private SubPowerup type;

    public Powerup(Location location){
        this.location = location;
        type = PowerupManager.getInstance().getRandomPowerup();
    }

    public Powerup(Location location, int type){
        this.type = PowerupManager.getInstance().getPowerup(type);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public SubPowerup getType() {
        return type;
    }
}
