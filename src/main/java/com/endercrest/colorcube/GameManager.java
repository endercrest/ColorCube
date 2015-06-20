package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        FileConfiguration system = settingsManager.getSystemConfig();
        games.clear();
        int arenaID = settingsManager.getNextArenaID();
        int arena = 1;
        for(int loaded = 0; loaded < arenaID; loaded++){
            if(system.isSet("arenas." + arena + ".x1")){
                if(system.isSet("arenas." + arena + ".enabled")){
                    msg.debugConsole("Loading arena:" + arena);
                    games.add(new Game(arena, plugin));
                }
            }
            arena++;
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
                return g.getGameID();
            }
        }
        return -1;
    }

    public int getBlockGameIdLobby(Location v) {
        for (Game g: games) {
            if(g.isLobbySet()) {
                if (g.isBlockInLobby(v)) {
                    return g.getGameID();
                }
            }
        }
        return -1;
    }

    public void createArenaFromSelection(Player p){
        FileConfiguration system = settingsManager.getSystemConfig();
        WorldEditPlugin we = plugin.getWorldEdit();
        Selection selection = we.getSelection(p);

        if(selection == null){
            msg.sendFMessage("error.noselection", p);
            return;
        }
        Location pos1 = selection.getMaximumPoint();
        Location pos2 = selection.getMinimumPoint();

        int id = settingsManager.getNextArenaID() + 1;
        system.set("arena_next_id", id);
        if(games.size() == 0 || games.isEmpty()){
            id = 1;
        }
        system.set(("spawns." + id), null);
        system.set("arenas." + id + ".world", pos1.getWorld().getName());
        system.set("arenas." + id + ".x1", pos1.getBlockX());
        system.set("arenas." + id + ".y1", pos1.getBlockY());
        system.set("arenas." + id + ".z1", pos1.getBlockZ());
        system.set("arenas." + id + ".x2", pos2.getBlockX());
        system.set("arenas." + id + ".y2", pos2.getBlockY());
        system.set("arenas." + id + ".z2", pos2.getBlockZ());
        system.set("arenas." + id + ".pvp", false);
        system.set("arenas." + id + ".enabled", true);
        system.set("arenas." + id + ".reward", 0.0);

        settingsManager.saveSystemConfig();
        addArena(id);
        msg.sendFMessage("info.create", p, "arena-" + id);
    }

    public int getGameCount(){
        return games.size();
    }

    public Game.Status getStatus(int a) {
        for (Game g: games) {
            if (g.getGameID() == a) {
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
    }

    /**
     * Remove arena
     * @param id The ID of the arena
     */
    public void removeArena(int id){
        for(Game game: games){
            if(game.getGameID() == id){
                games.remove(game);
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
            if(game.getGameID() == id){
                return game;
            }
        }
        return null;
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
                return game.getGameID();
            }
        }
        return -1;
    }

    public int getSpectatePlayerId(Player p) {
        for (Game g: games) {
            if (g.isSpectator(p)) {
                return g.getGameID();
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
