package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.game.Lobby;
import com.endercrest.colorcube.game.LobbySign;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LobbyManager {

    public static LobbyManager instance = new LobbyManager();

    private ColorCube plugin;
    private List<LobbySign> lobbySigns = new ArrayList<LobbySign>();

    public static LobbyManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;
        loadSigns();
        updateAll();
        MessageManager.getInstance().debugConsole("&eLobby Manager Set up");
    }

    public void createLobbyFromSelection(Player p, Game game){
        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        WorldEditPlugin worldEdit = plugin.getWorldEdit();
        Selection selection = worldEdit.getSelection(p);

        if(selection == null){
            MessageManager.getInstance().sendFMessage("error.noselection", p);
            return;
        }
        Location pos1 = selection.getMaximumPoint();//Max
        Location pos2 = selection.getMinimumPoint();//Min

        game.setLobby(new Lobby(pos1, pos2));

        system.set("arenas." + game.getGameID() + ".lworld", pos1.getWorld().getName());
        system.set("arenas." + game.getGameID() + ".lx1", pos1.getBlockX());
        system.set("arenas." + game.getGameID() + ".ly1", pos1.getBlockY());
        system.set("arenas." + game.getGameID() + ".lz1", pos1.getBlockZ());
        system.set("arenas." + game.getGameID() + ".lx2", pos2.getBlockX());
        system.set("arenas." + game.getGameID() + ".ly2", pos2.getBlockY());
        system.set("arenas." + game.getGameID() + ".lz2", pos2.getBlockZ());
        SettingsManager.getInstance().saveSystemConfig();
        MessageManager.getInstance().sendFMessage("info.createlobby", p, "arena-" + game.getGameID());
    }

    public void setLobbySpawn(int id, Location loc){
        GameManager.getInstance().getGame(id).setLobbySpawn(id, loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public void createLobbySign(Player p, Game game) {
        if(!p.getTargetBlock(null, 10).getType().equals(Material.WALL_SIGN)){
            MessageManager.getInstance().sendFMessage("error.notsign", p);
            return;
        }

        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();

        int id = SettingsManager.getInstance().getNextSignID() + 1;
        system.set("sign_next_id", id);
        if(id == 0 || lobbySigns.isEmpty()){
            id = 1;
        }
        Location loc = p.getTargetBlock(null, 10).getLocation();

        system.set("signs." + id, null);
        system.set("signs." + id + ".x", loc.getBlockX());
        system.set("signs." + id + ".y", loc.getBlockY());
        system.set("signs." + id + ".z", loc.getBlockZ());
        system.set("signs." + id + ".world", loc.getWorld().getName());
        system.set("signs." + id + ".gameID", game.getGameID());
        system.set("signs." + id + ".enabled", true);
        SettingsManager.getInstance().saveSystemConfig();

        LobbySign sign = new LobbySign(loc, game, id);
        lobbySigns.add(sign);
        sign.update();
        MessageManager.getInstance().debugConsole("Created LobbySign: " + id);
    }

    public void removeLobbySign(Player p){
        if(!p.getTargetBlock(null, 10).getType().equals(Material.WALL_SIGN)){
            MessageManager.getInstance().sendFMessage("error.notsign", p);
            return;
        }
        LobbySign lobbySign = getLobbySign(p.getTargetBlock(null, 10).getLocation());
        if(lobbySign != null) {
            SettingsManager.getInstance().getSystemConfig().set("sign_next_id", SettingsManager.getInstance().getSystemConfig().getInt("sign_next_id") + 1);
            SettingsManager.getInstance().getSystemConfig().set("signs." + lobbySign.getSignID() + ".enabled", false);
            SettingsManager.getInstance().saveSystemConfig();
            lobbySigns.remove(lobbySign);
            MessageManager.getInstance().debugConsole("Deleting Sign:" + lobbySign.getSignID());
        }else{
            MessageManager.getInstance().sendFMessage("error.nolobbysign", p);
        }
    }

    public void loadSigns(){
        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        lobbySigns.clear();
        int signID = SettingsManager.getInstance().getNextSignID();
        int sign = 1;
        for(int loaded = 0; loaded < signID; loaded++){
            if(system.isSet("signs." + sign + ".x")){
                if(system.isSet("signs." + sign + ".enabled")){
                    MessageManager.getInstance().debugConsole("Loading Sign: " + sign);
                    int x = system.getInt("signs." + sign + ".x");
                    int y = system.getInt("signs." + sign + ".y");
                    int z = system.getInt("signs." + sign + ".z");
                    World world = Bukkit.getWorld(system.getString("signs." + sign + ".world"));
                    int gameID = system.getInt("signs." + sign + ".gameID");
                    Game game = GameManager.getInstance().getGame(gameID);
                    Location loc = new Location(world, x, y, z);
                    if(system.getBoolean("signs." + sign + ".enabled")) {
                        lobbySigns.add(new LobbySign(loc, game, sign));
                    }
                }
            }
        }
    }

    public LobbySign getLobbySign(Location loc){
        for(LobbySign lobbySign: lobbySigns){
            if(lobbySign.getLocation().equals(loc)){
                return lobbySign;
            }
        }
        return null;
    }

    public void updateAll(){
        for(LobbySign sign: lobbySigns){
            sign.update();
        }
    }

    public void update(int id){
        for(LobbySign sign: lobbySigns){
            if(sign.getSignGameID() == id){
                sign.update();
            }
        }
    }
}
