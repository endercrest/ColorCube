package com.endercrest.colorcube.game;

import com.endercrest.colorcube.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lobby extends Region {

    private Location spawn;

    public Lobby(Location max, Location min){
        super(max, min);
    }

    /**
     * Load the spawn
     * @param id
     */
    public void loadSpawn(int id){
        YamlConfiguration config = SettingsManager.getInstance().getArenaConfig(id);
        spawn = new Location(Bukkit.getWorld(config.getString("lobby.spawn.world")),
                config.getDouble("lobby.spawn.x"),
                config.getDouble("lobby.spawn.y"),
                config.getDouble("lobby.spawn.z"),
                (float) config.getDouble("lobby.spawn.yaw"),
                (float) config.getDouble("lobby.spawn.pitch"));
    }

    public void setSpawn(int id, Location location){
        YamlConfiguration config = SettingsManager.getInstance().getArenaConfig(id);
        config.set("lobby.spawn.world", location.getWorld().getName());
        config.set("lobby.spawn.x", location.getX());
        config.set("lobby.spawn.y", location.getY());
        config.set("lobby.spawn.z", location.getZ());
        config.set("lobby.spawn.yaw", location.getYaw());
        config.set("lobby.spawn.pitch", location.getPitch());
        SettingsManager.getInstance().saveArenaConfig(id);
        spawn = location;
    }

    public Location getSpawn(){
        return spawn;
    }

    public boolean isSpawnSet(){
        return spawn != null;
    }
}
