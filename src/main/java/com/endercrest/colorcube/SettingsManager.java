package com.endercrest.colorcube;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;

public class SettingsManager {

    static SettingsManager instance = new SettingsManager();

    private ColorCube plugin = null;
    private FileConfiguration messages;
    private FileConfiguration system;

    private static final int MESSAGE_VERSION = 0;
    private static final int SYSTEM_VERSION = 0;

    //Systems file
    private File file1;
    //Messages file
    private File file2;

    public static SettingsManager getInstance(){
        return instance;
    }

    public SettingsManager(){

    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;

        loadPluginDefaults();

        file1 = new File(plugin.getDataFolder(), "system.yml");
        file2 = new File(plugin.getDataFolder(), "messages.yml");

        try {
            if (!file1.exists())
                file1.createNewFile();
            if (!file2.exists())
                plugin.saveResource("messages.yml", false);
        }catch(Exception e){
            e.printStackTrace();
        }
        reloadMessages();
        reloadSystem();
        MessageManager.getInstance().debugConsole("&eSettings Manager Set up");
    }

    public World getGameWorld(int game) {
        if (SettingsManager.getInstance().getSystemConfig().getString("arenas." + game + ".world") == null) {
            //LobbyManager.getInstance().error(true);
            return null;

        }
        return plugin.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("arenas." + game + ".world"));
    }

    public World getLobbyWorld(int game){
        if(getSystemConfig().getString("arenas." + game + ".lworld") == null){
            return null;
        }
        return plugin.getServer().getWorld(getSystemConfig().getString("arenas." + game + ".lworld"));
    }

    public int getSpawnCount(int game){
        return getSystemConfig().getInt("spawns." + game + ".count", 0);

    }

    /**
     * Reloads System Config
     */
    public void reloadSystem() {
        system = YamlConfiguration.loadConfiguration(file1);
        if(system.getInt("version", 0) != SYSTEM_VERSION){
            moveFile(file1);
            reloadSystem();
        }
        system.set("version", SYSTEM_VERSION);
        saveSystemConfig();
    }

    /**
     * Saves system config
     */
    public void saveSystemConfig() {
        if(getPluginConfig().getBoolean("debug", false)) {
            MessageManager.getInstance().log("&eSaving System Config!");
        }
        try {
            system.save(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getSystemConfig() {
        return system;
    }

    /**
     * Reload message config
     */
    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(file2);
        if(messages.getInt("version", 0) != MESSAGE_VERSION){
            moveFile(file2);
            plugin.saveResource("messages.yml", true);
        }
        messages.set("version", MESSAGE_VERSION);
        saveMessagesConfig();
    }

    public void reloadConfig(){
        plugin.reloadConfig();
    }

    /**
     * Saves message config
     */
    public void saveMessagesConfig() {
        if(getPluginConfig().getBoolean("debug", false)) {
            MessageManager.getInstance().log("&eSaving Message Config!");
        }
        try {
            messages.save(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessagesConfig() {
        return messages;
    }

    /**
     * Move file location
     * @param ff The file to be moved
     * @return True/False
     */
    public boolean moveFile(File ff){
        MessageManager.getInstance().log("Moving outdated config file.");
        String name = ff.getName();
        File ff2 = new File(plugin.getDataFolder(), getNextName(name, 0));
        return ff.renameTo(ff2);
    }

    /**
     * Get the next available name for moving
     * @param name The orignal name
     * @param n The index
     * @return The new name
     */
    public String getNextName(String name, int n){
        File ff = new File(plugin.getDataFolder(), name+".old"+n);
        if(!ff.exists()){
            return ff.getName();
        }
        else{
            return getNextName(name, n+1);
        }
    }

    /**
     * Sets the plugin defaults into the plugin config
     */
    private void loadPluginDefaults(){
        if(!plugin.getConfig().contains("color-logs")){
            plugin.getConfig().addDefault("color-logs", true);
        }
        if(!plugin.getConfig().contains("debug")){
            plugin.getConfig().addDefault("debug", false);
        }
        if(!plugin.getConfig().contains("rollback.per-tick")){
            plugin.getConfig().addDefault("rollback.per-tick", 100);
        }
        if(!plugin.getConfig().contains("game-length")){
            plugin.getConfig().addDefault("game-length", 600);
        }
        if(!plugin.getConfig().contains("update-checker")){
            plugin.getConfig().addDefault("update-checker", true);
        }
        if(!plugin.getConfig().contains("powerup-freq")){
            plugin.getConfig().addDefault("powerup-freq", 15);
        }
        if(!plugin.getConfig().contains("command-whitelist")){
            plugin.getConfig().addDefault("command-whitelist", new ArrayList<String>());
        }
        if(!plugin.getConfig().contains("auto-start")){
            plugin.getConfig().addDefault("auto-start", 0.75);
        }
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getNextArenaID(){
        return getSystemConfig().getInt("arena_next_id", 0);
    }

    public FileConfiguration getPluginConfig(){
        return plugin.getConfig();
    }

    public void savePluginConfig(){
        if(getPluginConfig().getBoolean("debug", false)) {
            MessageManager.getInstance().log("&eSaving Plugin Config!");
        }
        plugin.saveConfig();
    }

    /**
     * Get Spawn Point for Arena
     * @param gameid The Game ID
     * @param spawnid The Spawn ID
     * @return location of point
     */
    public Location getSpawnPoint(int gameid, int spawnid) {
        return new Location(getGameWorld(gameid),
                system.getInt("spawns." + gameid + "." + spawnid + ".x"),
                system.getInt("spawns." + gameid + "." + spawnid + ".y"),
                system.getInt("spawns." + gameid + "." + spawnid + ".z"),
                (float)system.getDouble("spawns." + gameid + "." + spawnid + ".yaw"),
                (float)system.getDouble("spawns." + gameid + "." + spawnid + ".pitch")).add(0.5, 0, 0.5);
    }

    /**
     * Get Main Lobby Spawn
     * @return Location of main spawn
     */
    public Location getGlobalLobbySpawn() {
        try{
            return new Location(Bukkit.getWorld(system.getString("lobby.global.world")),
                    system.getInt("lobby.global.x"),
                    system.getInt("lobby.global.y"),
                    system.getInt("lobby.global.z"),
                    system.getInt("lobby.global.yaw"),
                    system.getInt("lobby.global.pitch")).add(0.5, 0, 0.5);
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Set Main Lobby Spawn
     * @param l The Location
     */
    public void setGlobalLobbySpawn(Location l) {
        system.set("lobby.global.world", l.getWorld().getName());
        system.set("lobby.global.x", l.getBlockX());
        system.set("lobby.global.y", l.getBlockY());
        system.set("lobby.global.z", l.getBlockZ());
        system.set("lobby.global.yaw", l.getYaw());
        system.set("lobby.global.pitch", l.getPitch());

    }

    /**
     *
     * @param gameid
     * @param spawnid
     * @param v
     */
    public void setSpawn(int gameid, int spawnid, Location v) {
        system.set("spawns." + gameid + "." + spawnid + ".x", v.getBlockX());
        system.set("spawns." + gameid + "." + spawnid + ".y", v.getBlockY());
        system.set("spawns." + gameid + "." + spawnid + ".z", v.getBlockZ());
        system.set("spawns." + gameid + "." + spawnid + ".yaw", v.getYaw());
        system.set("spawns." + gameid + "." + spawnid + ".pitch", v.getPitch());

        if (spawnid > system.getInt("spawns." + gameid + ".count")) {
            system.set("spawns." + gameid + ".count", spawnid);
        }
        saveSystemConfig();
        GameManager.getInstance().getGame(gameid).addSpawn();

    }
}
