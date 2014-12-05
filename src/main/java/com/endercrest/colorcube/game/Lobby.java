package com.endercrest.colorcube.game;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

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
        MessageManager.getInstance().debugConsole("X:" + x + " Y:" + y + " Z:" + z + " World:" + pos.getWorld().getName());
        return x >= pos2.getBlockX() && x < pos1.getBlockX() + 1 && y >= pos2.getBlockY() && y < pos1.getBlockY() + 1 && z >= pos2.getBlockZ() && z < pos1.getBlockZ() + 1;
    }

    public void loadSpawn(int id){
        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        spawn = new Location(Bukkit.getWorld(system.getString("lobby." + id + ".world")),
                system.getInt("lobby." + id + ".x"),
                system.getInt("lobby." + id + ".y"),
                system.getInt("lobby." + id + ".z"),
                system.getInt("lobby." + id + ".yaw"),
                system.getInt("lobby." + id + ".pitch"));
    }

    public void setSpawn(int id, Location location){
        SettingsManager.getInstance().getSystemConfig().set("lobby." + id + ".world", location.getWorld().getName());
        SettingsManager.getInstance().getSystemConfig().set("lobby." + id + ".x", location.getBlockX());
        SettingsManager.getInstance().getSystemConfig().set("lobby." + id + ".y", location.getBlockY());
        SettingsManager.getInstance().getSystemConfig().set("lobby." + id + ".z", location.getBlockZ());
        SettingsManager.getInstance().getSystemConfig().set("lobby." + id + ".yaw", location.getYaw());
        SettingsManager.getInstance().getSystemConfig().set("lobby." + id + ".pitch", location.getPitch());
        SettingsManager.getInstance().saveSystemConfig();
        spawn = location;
    }

    public Location getSpawn(){
        return spawn;
    }

    public boolean isSpawnSet(){
        return spawn != null;
    }
}
