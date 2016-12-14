package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {

    static GameManager instance = new GameManager();

    private ColorCube plugin;
    private List<Game> games = new ArrayList<Game>();
    private MessageManager msg = MessageManager.getInstance();
    private SettingsManager settingsManager = SettingsManager.getInstance();

    public static GameManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;
        loadGames();
        MessageManager.getInstance().debugConsole("&eGame Manager Set up");
    }

    /**
     * Reload all the games
     */
    public void reloadGames(){
        loadGames();
    }

    /**
     * Load all games into the system
     */
    private void loadGames(){
        games.clear();
        HashMap<Integer, YamlConfiguration> configs = SettingsManager.getInstance().getArenaConfigs();

        for(int id: configs.keySet()) {
            YamlConfiguration config = configs.get(id);

            if (Bukkit.getWorld(config.getString("loc.world")) != null) {
                if (config.getBoolean("enabled")) {
                    msg.debugConsole(String.format("Loading Arena %s", id));
                    games.add(new Game(id, plugin));
                } else {
                    msg.debugConsole(String.format("Ignoring arena %s, it is disabled.", id));
                    //TODO Move disabled arena to archive folder.
                }
            } else {
                msg.debugConsole(String.format("Arena %s is in a world that is not loaded.", id));
            }
        }
    }

    public ColorCube getPlugin(){
        return plugin;
    }

    public void removePlayer(Player p, boolean b){
        getGame(getActivePlayerGameID(p)).removePlayer(p, b);
    }

    public void removeSpectator(Player p, boolean b){
        getGame(getSpectatePlayerId(p)).removeSpectator(p, b);
    }

    public int getBlockGameId(Location v) {
        for (Game g: games) {
            if (g.isBlockInArena(v)) {
                return g.getId();
            }
        }
        return -1;
    }

    public int getBlockGameIdLobby(Location v) {
        for (Game g: games) {
            if(g.isLobbySet()) {
                if (g.isBlockInLobby(v)) {
                    return g.getId();
                }
            }
        }
        return -1;
    }

    public void createArenaFromSelection(Player p){
        WorldEditPlugin we = plugin.getWorldEdit();
        Selection selection = we.getSelection(p);

        if(selection == null){
            msg.sendFMessage("error.noselection", p);
            return;
        }
        Location pos1 = selection.getMaximumPoint();
        Location pos2 = selection.getMinimumPoint();

        int id = settingsManager.getNextArenaID();
        YamlConfiguration config = SettingsManager.getInstance().createArenaConfig(id, pos1, pos2);
        if(config == null){
            MessageManager.getInstance().sendFMessage("error.nextid", p,
                    "type-"+MessageManager.getInstance().getFValue("words.arena"));
            return;
        }
        SettingsManager.getInstance().incrementNextArenaId();
        addArena(id);
        msg.sendFMessage("info.create", p, "arena-" + id);
    }

    public int getGameCount(){
        return games.size();
    }

    public Game.Status getStatus(int a) {
        for (Game g: games) {
            if (g.getId() == a) {
                return g.getStatus();
            }
        }
        return null;
    }

    /**
     * Creates a new arena
     * @param id The ID of the arena
     */
    public void addArena(int id){
        Game game = new Game(id, plugin);
        games.add(game);
        MenuManager.getInstance().addGame();
    }

    /**
     * Remove arena
     * @param id The ID of the arena
     */
    public void removeArena(int id){
        for(Game game: games){
            if(game.getId() == id){
                games.remove(game);
                MenuManager.getInstance().removeGame();
                return;
            }
        }
    }

    /**
     * Get game
     * @param id The ID of the arena
     * @return game
     */
    public Game getGame(int id){
        for(Game game: games){
            if(game.getId() == id){
                return game;
            }
        }
        return null;
    }

    public int getIndexOfGame(int id){
        for(int i = 0; i < games.size(); i++){
            if(games.get(id).getId() == id){
                return i;
            }
        }
        return -1;
    }

    /**
     * Gives the list of games
     * @return Games
     */
    public List<Game> getGames(){
        return games;
    }

    public int getActivePlayerGameID(Player p){
        for(Game game: games){
            if(game.isPlayerActive(p)){
                return game.getId();
            }
        }
        return -1;
    }

    public int getSpectatePlayerId(Player p) {
        for (Game g: games) {
            if (g.isSpectator(p)) {
                return g.getId();
            }
        }
        return -1;
    }

    public boolean isPlayerSpectator(Player p){
        for(Game g: games){
            if(g.isSpectator(p)){
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerActive(Player player) {
        for (Game g: games) {
            if (g.isPlayerActive(player)) {
                return true;
            }
        }
        return false;
    }

    public void addPlayer(Player p, int id){
        Game game = getGame(id);
        if(game == null){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + id);
            return;
        }
        game.addPlayer(p);
    }

    public void addSpectator(Player p, int id){
        Game game = getGame(id);
        if(game == null){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + id);
            return;
        }
        game.addSpectator(p);
    }

    public int getPlayerTeamID(Player player){
        for(Game game: games){
            if(game.isPlayerActive(player)){
                return game.getTeamID(player);
            }
        }
        return -1;
    }
}
