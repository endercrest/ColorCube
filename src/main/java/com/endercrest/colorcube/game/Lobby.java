package com.endercrest.colorcube.game;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Lobby {

    private Location pos1; //Max
    private Location pos2; //Min

    private Location spawn;

    public Lobby(Location pos1, Location pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    /**
     * Get Pos1(Max)
     * @return pos1
     */
    public Location getPos1() {
        return pos1;
    }

    /**
     * Get Pos2(Min)
     * @return pos2
     */
    public Location getPos2() {
        return pos2;
    }

    public boolean containsBlock(Location pos){
        if(pos.getWorld() != pos2.getWorld())
            return false;
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        return x >= pos2.getBlockX() && x < pos1.getBlockX() + 1 && y >= pos2.getBlockY() && y < pos1.getBlockY() + 1 && z >= pos2.getBlockZ() && z < pos1.getBlockZ() + 1;
    }

    /**
     * Load the spawn
     * @param id
     */
    public void loadSpawn(int id){
        YamlConfiguration config = SettingsManager.getInstance().getArenaConfig(id);
        spawn = new Location(Bukkit.getWorld(config.getString("lobby.world")),
                config.getDouble("lobby.x"),
                config.getDouble("lobby.y"),
                config.getDouble("lobby.z"),
                (float) config.getDouble("lobby.yaw"),
                (float) config.getDouble("lobby.pitch"));
    }

    public void setSpawn(int id, Location location){
        YamlConfiguration config = SettingsManager.getInstance().getArenaConfig(id);
        config.set("lobby." + id + ".world", location.getWorld().getName());
        config.set("lobby." + id + ".x", location.getX());
        config.set("lobby." + id + ".y", location.getY());
        config.set("lobby." + id + ".z", location.getZ());
        config.set("lobby." + id + ".yaw", location.getYaw());
        config.set("lobby." + id + ".pitch", location.getPitch());
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
