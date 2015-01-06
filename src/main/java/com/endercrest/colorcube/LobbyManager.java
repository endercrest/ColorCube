package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.game.Lobby;
import com.endercrest.colorcube.game.LobbyWall;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LobbyManager {

    public static LobbyManager instance = new LobbyManager();

    private ColorCube plugin;
    private HashMap<Integer, LobbyWall> lobbyWall = new HashMap<Integer, LobbyWall>();

    public static LobbyManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;
        MessageManager.getInstance().debugConsole("&eLobby Manager Set up");

        //TODO Lobby wall loading is done here
    }

    public void createLobbyWallFromSelection(Player p, int id){
        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        WorldEditPlugin worldEdit = plugin.getWorldEdit();
        Selection selection = worldEdit.getSelection(p);

        if(selection == null){
            MessageManager.getInstance().sendFMessage("error.noselection", p);
            return;
        }
        if(selection.getLength() > 1 && selection.getWidth() > 1){
            MessageManager.getInstance().sendFMessage("error.bigselection", p, "detail-Cannot be bigger than 1 wide");
            return;
        }
        if(selection.getHeight() > 1){
            MessageManager.getInstance().sendFMessage("error.bigselection", p, "detail-Cannot be bigger than 1 tall");
            return;
        }

        for(int x = selection.getMinimumPoint().getBlockX(); x < selection.getMaximumPoint().getBlockX(); x++){
            for(int z = selection.getMinimumPoint().getBlockZ();  z< selection.getMaximumPoint().getBlockZ(); z++){
                Location loc = new Location(selection.getWorld(), x, selection.getMinimumPoint().getBlockY(), z);
                if(loc.getBlock().getType() != Material.WALL_SIGN || loc.getBlock().getType() != Material.SIGN || loc.getBlock().getType() != Material.SIGN_POST){

                    return;
                }
            }
        }
    }

    public void updateWall(int gameID){

    }

    public void updateAllWalls(){

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
}
