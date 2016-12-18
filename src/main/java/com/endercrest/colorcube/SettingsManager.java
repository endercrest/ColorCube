package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsManager {

    static SettingsManager instance = new SettingsManager();

    private ColorCube plugin = null;
    private FileConfiguration messages;

    private static final int MESSAGE_VERSION = 0;

    private File messageFile;

    //folders
    private File arenaFolder;
    private File signFolder;
    private File arenaArchiveFolder;
    private File signArchiveFolder;

    private HashMap<Integer, File> arenaFiles;
    private HashMap<Integer, YamlConfiguration> arenaConfigs;
    private File arenaGlobalFile;
    private YamlConfiguration arenaGlobalConfig;

    private HashMap<Integer, File> signFiles;
    private HashMap<Integer, YamlConfiguration> signConfigs;
    private File signGlobalFile;
    private YamlConfiguration signGlobalConfig;



    public static SettingsManager getInstance(){
        return instance;
    }

    public SettingsManager(){

    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;

        loadPluginDefaults();

        messageFile = new File(plugin.getDataFolder(), "messages.yml");

        arenaFolder = new File(plugin.getDataFolder(), "Arena");
        signFolder = new File(plugin.getDataFolder(), "Sign");
        arenaArchiveFolder = new File(arenaFolder, "Archive");
        signArchiveFolder = new File(signFolder, "Archive");

        arenaFolder.mkdirs();
        signFolder.mkdirs();
        arenaArchiveFolder.mkdirs();
        signArchiveFolder.mkdirs();

        loadArenaConfigs();
        loadSignConfigs();

        try {
            if (!messageFile.exists())
                plugin.saveResource("messages.yml", false);
        }catch(Exception e){
            e.printStackTrace();
        }
        reloadMessages();

        reloadConfig();
        MessageManager.getInstance().debugConsole("&eSettings Manager Set up");
    }

    /**
     * Loads the arena configs for the first time.
     */
    private void loadArenaConfigs(){
        MessageManager.getInstance().debugConsole("Loading Arena Configs");
        if(arenaFolder != null && arenaFolder.listFiles() != null) {
            arenaFiles = new HashMap<>();
            arenaConfigs = new HashMap<>();
            for (File file : arenaFolder.listFiles()) {
                if (file.isFile()) {
                    if (file.getName().equals("global.yml")) {
                        arenaGlobalFile = file;
                        arenaGlobalConfig = YamlConfiguration.loadConfiguration(file);
                        continue;
                    }
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    int id = config.getInt("id");
                    arenaFiles.put(id, file);
                    arenaConfigs.put(id, config);
                }
            }
        }
        MessageManager.getInstance().debugConsole("Successfully Loaded Arena Configs");
    }

    /**
     * Reload all arena configs
     */
    public void reloadArenaConfigs(){
        MessageManager.getInstance().debugConsole("Reloading Arena Configs");
        for(int key: arenaFiles.keySet()){
            File aFile = arenaFiles.get(key);
            arenaConfigs.put(key, YamlConfiguration.loadConfiguration(aFile));
        }
        reloadArenaGlobalConfig();
        saveArenaConfigs();
    }

    /**
     * Save all arena configs.
     */
    public void saveArenaConfigs(){
        MessageManager.getInstance().debugConsole("Saving Arena Configs");

        for(int key: arenaConfigs.keySet()){
            try {
                arenaConfigs.get(key).save(arenaFiles.get(key));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveArenaGlobalConfig();
    }

    /**
     * Get the global arena config contains global lobby information as well as next game id.
     * @return {@link YamlConfiguration}
     */
    public YamlConfiguration getArenaGlobalConfig(){
        return arenaGlobalConfig;
    }

    /**
     * Reload the global arena configuration file.
     */
    public void reloadArenaGlobalConfig(){
        MessageManager.getInstance().debugConsole("Reloading global arena config.");
        arenaGlobalConfig = YamlConfiguration.loadConfiguration(arenaGlobalFile);
        saveArenaGlobalConfig();
    }

    /**
     * Save the global arena configuration file.
     */
    public void saveArenaGlobalConfig(){
        MessageManager.getInstance().debugConsole("Saving global arena config.");

        try {
            arenaGlobalConfig.save(arenaGlobalFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the specific arena config.
     * @param id The arena id.
     */
    public void saveArenaConfig(int id){
        MessageManager.getInstance().debugConsole(String.format("Saving arena %s config", id));

        YamlConfiguration config = getArenaConfig(id);
        if(config != null){
            try {
                config.save(arenaFiles.get(id));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get a specific arena configuration that contains all the arena information.
     * @param id The id of the arena.
     * @return {@link YamlConfiguration}
     */
    public YamlConfiguration getArenaConfig(int id){
        return arenaConfigs.get(id);
    }

    public HashMap<Integer, YamlConfiguration> getArenaConfigs(){
        return arenaConfigs;
    }

    /**
     * Creates a new arena configuration file.
     * @param id The id of the new arena.
     * @return Returns the YamlConfiguration or returns null if it already exists.
     */
    public YamlConfiguration createArenaConfig(int id, Location pos1, Location pos2){
        File file = new File(arenaFolder, "arena"+id+".yml");

        if(arenaFiles.get(id) == null && file.exists()){
            return null;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        arenaFiles.put(id, file);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("spawns", null);
        config.set("lobby", null);
        config.set("loc.world", pos1.getWorld().getName());
        config.set("loc.pos1.x", pos1.getX());
        config.set("loc.pos1.y", pos1.getY());
        config.set("loc.pos1.z", pos1.getZ());
        config.set("loc.pos2.x", pos2.getX());
        config.set("loc.pos2.y", pos2.getY());
        config.set("loc.pos2.z", pos2.getZ());
        config.set("options.pvp", false);
        config.set("enabled", true);
        config.set("options.reward", 0.0);


        arenaConfigs.put(id, config);

        saveArenaConfig(id);
        return config;
    }

    /**
     * Removes arena file and moves to the archive folder.
     * @param id The id.
     */
    public boolean archiveArena(int id){
        File file = arenaFiles.get(id);
        try {
            Files.move(file.toPath(), new File(arenaArchiveFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            MessageManager.getInstance().log(String.format("&cFailed to archive arena %s.", id));
            MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
            return false;
        }
        return true;
    }

    /**
     * Load the sign configs for the first time.
     */
    private void loadSignConfigs(){
        MessageManager.getInstance().debugConsole("Loading Sign Configs");
        if(signFolder != null && signFolder.listFiles() != null) {
            signFiles = new HashMap<>();
            signConfigs = new HashMap<>();
            for (File file : signFolder.listFiles()) {
                if (file.isFile()) {
                    if (file.getName().equals("global.yml")) {
                        signGlobalFile = file;
                        signGlobalConfig = YamlConfiguration.loadConfiguration(file);
                        continue;
                    }
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    int id = config.getInt("id");
                    signFiles.put(id, file);
                    signConfigs.put(id, config);
                }
            }
        }
    }

    /**
     * Reload all sign configurations.
     */
    public void reloadSignConfigs(){
        MessageManager.getInstance().debugConsole("Reloading Sign Configs");
        for(int key: signFiles.keySet()){
            File aFile = signFiles.get(key);
            signConfigs.put(key, YamlConfiguration.loadConfiguration(aFile));
        }
        reloadArenaGlobalConfig();
        saveSignConfigs();
    }

    /**
     * Save all sign configurations
     */
    public void saveSignConfigs(){
        MessageManager.getInstance().debugConsole("Saving Sign Configs");

        for(int key: signConfigs.keySet()){
            try {
                signConfigs.get(key).save(signFiles.get(key));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveSignGlobalConfig();
    }

    /**
     * Ge the global sign config that contains the next game id.
     * @return {@link YamlConfiguration}
     */
    public YamlConfiguration getSignGlobalConfig(){
        return signGlobalConfig;
    }

    /**
     * Reload the global arena configuration file.
     */
    public void reloadSignGlobalConfig(){
        MessageManager.getInstance().debugConsole("Reloading global sign config.");
        signGlobalConfig = YamlConfiguration.loadConfiguration(signGlobalFile);
        saveSignGlobalConfig();
    }

    /**
     * Save the global arena configuration file.
     */
    public void saveSignGlobalConfig(){
        MessageManager.getInstance().debugConsole("Saving global sign config.");

        try {
            signGlobalConfig.save(signGlobalFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the specific sign config.
     * @param id The sign id.
     */
    public void saveSignConfig(int id){
        MessageManager.getInstance().debugConsole(String.format("Saving sign %s config", id));

        YamlConfiguration config = getSignConfig(id);
        if(config != null){
            try {
                config.save(signFiles.get(id));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<Integer, YamlConfiguration> getSignConfigs(){
        return signConfigs;
    }

    /**
     * Get a specific sign configuration that contains all the sign information.
     * @param id The id of the sign.
     * @return {@link YamlConfiguration}
     */
    public YamlConfiguration getSignConfig(int id){
        return signConfigs.get(id);
    }

    /**
     * Create a new sign config.
     * @param id The id of the new sign
     * @param loc The location of the sign.
     * @return
     */
    public YamlConfiguration createSignConfig(int id, int gameId, Location loc){
        File file = new File(signFolder, "sign"+id+".yml");

        if(signFiles.get(id) == null && file.exists()){
            return null;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        signFiles.put(id, file);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("id", id);
        config.set("loc.x", loc.getBlockX());
        config.set("loc.y", loc.getBlockY());
        config.set("loc.z", loc.getBlockZ());
        config.set("loc.world", loc.getWorld().getName());
        config.set("gameId", gameId);
        config.set("enabled", true);

        signConfigs.put(id, config);

        saveSignConfig(id);
        return config;
    }

    /**
     * Removes sign file and moves to the archive folder.
     * @param id The id.
     */
    public boolean archiveSign(int id){
        File file = signFiles.get(id);
        try {
            Files.move(file.toPath(), new File(signArchiveFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            MessageManager.getInstance().log(String.format("&cFailed to archive sign %s.", id));
            MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
            return false;
        }
        return true;
    }

    /**
     * Reload message config
     */
    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messageFile);
        if(messages.getInt("version", 0) != MESSAGE_VERSION){
            moveFile(messageFile);
            plugin.saveResource("messages.yml", true);
        }
        messages.set("version", MESSAGE_VERSION);
        saveMessagesConfig();
    }

    public void reloadConfig(){
        plugin.reloadConfig();

        //Set the message prefix.
        MessageManager.getInstance().setPrefix(getPluginConfig().getString("prefix", "&f[&6ColorCube&f]"));
    }

    /**
     * Saves message config
     */
    public void saveMessagesConfig() {
        if(getPluginConfig().getBoolean("debug", false)) {
            MessageManager.getInstance().log("&eSaving Message Config!");
        }
        try {
            messages.save(messageFile);
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
        if(!plugin.getConfig().contains("vote-start")){
            plugin.getConfig().addDefault("vote-start", 0.5);
        }
        if(!plugin.getConfig().contains("paintable-blocks")){
            List<String> blocks = new ArrayList<String>();
            blocks.add(Material.STAINED_CLAY.toString());
            plugin.getConfig().addDefault("paintable-blocks", blocks);
        }
        if(!plugin.getConfig().contains("sign.line1")){
            plugin.getConfig().addDefault("sign.line1", "&f[&6ColorCube&f]");
        }
        if(!plugin.getConfig().contains("sign.line2")){
            plugin.getConfig().addDefault("sign.line2", "&f[&6{$arenaname}&f]");
        }
        if(!plugin.getConfig().contains("sign.line3")){
            plugin.getConfig().addDefault("sign.line3", "{$players} / {$maxplayers}");
        }
        if(!plugin.getConfig().contains("sign.line4")){
            plugin.getConfig().addDefault("sign.line4", "{$status}");
        }
        if(!plugin.getConfig().contains("prefix")){
            plugin.getConfig().addDefault("prefix", "&f[&6ColorCube&f]");
        }
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getNextArenaID(){
        return getArenaGlobalConfig().getInt("nextId", 0);
    }

    public void incrementNextArenaId(){
        getArenaGlobalConfig().set("nextId", getNextArenaID() + 1);
    }

    public int getNextSignID(){
        return getArenaGlobalConfig().getInt("nextId", 0);
    }

    public void incrementNextSignId(){
        getSignGlobalConfig().set("nextId", getNextSignID() + 1);
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
     * Get Main Lobby Spawn
     * @return Location of main spawn
     */
    public Location getGlobalLobbySpawn() {
        try{
            return new Location(Bukkit.getWorld(getArenaGlobalConfig().getString("lobby.world")),
                    getArenaGlobalConfig().getDouble("lobby.x"),
                    getArenaGlobalConfig().getDouble("lobby.y"),
                    getArenaGlobalConfig().getDouble("lobby.z"),
                    (float) getArenaGlobalConfig().getDouble("lobby.yaw"),
                    (float) getArenaGlobalConfig().getDouble("lobby.pitch"));
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Set Main Lobby Spawn
     * @param l The Location
     */
    public void setGlobalLobbySpawn(Location l) {
        getArenaGlobalConfig().set("lobby.world", l.getWorld().getName());
        getArenaGlobalConfig().set("lobby.x", l.getX());
        getArenaGlobalConfig().set("lobby.y", l.getY());
        getArenaGlobalConfig().set("lobby.z", l.getZ());
        getArenaGlobalConfig().set("lobby.yaw", l.getYaw());
        getArenaGlobalConfig().set("lobby.pitch", l.getPitch());
        saveArenaGlobalConfig();
    }

    /**
     * Set Spawn of the Arena
     * @param gameid The Game ID
     * @param team The team the spawn is being set for.
     * @param loc The location
     */
    public void setSpawn(int gameid, Game.CCTeam team, Location loc) {
        YamlConfiguration arenaConfig = getArenaConfig(gameid);

        arenaConfig.set("spawns." + team.name().toLowerCase() + ".x", loc.getX());
        arenaConfig.set("spawns." + team.name().toLowerCase() + ".y", loc.getY());
        arenaConfig.set("spawns." + team.name().toLowerCase() + ".z", loc.getZ());
        arenaConfig.set("spawns." + team.name().toLowerCase() + ".yaw", loc.getYaw());
        arenaConfig.set("spawns." + team.name().toLowerCase() + ".pitch", loc.getPitch());

        saveArenaConfig(gameid);
        GameManager.getInstance().getGame(gameid).addSpawn(team);
    }

    /**
     * Set the reward of the desired arena
     * @param gameid The Game ID
     * @param reward The reward amount
     */
    public void setReward(int gameid, double reward){
        YamlConfiguration arenaConfig = getArenaConfig(gameid);
        arenaConfig.set("options.reward", reward);
        saveArenaConfig(gameid);
        GameManager.getInstance().getGame(gameid).setReward(reward);
    }

    public World getGameWorld(int game) {
        YamlConfiguration arenaConfig = getArenaConfig(game);
        if (!arenaConfig.isSet("loc.world")) {
            return null;
        }
        return plugin.getServer().getWorld(arenaConfig.getString("loc.world"));
    }

    public World getLobbyWorld(int game){
        YamlConfiguration arenaConfig = getArenaConfig(game);
        if(arenaConfig.isSet("lobby.world")){
            return null;
        }
        return plugin.getServer().getWorld(arenaConfig.getString("lobby.world"));
    }
}
