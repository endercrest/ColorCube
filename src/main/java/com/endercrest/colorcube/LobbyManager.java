package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.game.Lobby;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LobbyManager {

    public static LobbyManager instance = new LobbyManager();

    private ColorCube plugin;

    public static LobbyManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;
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
        GameManager.getInstance().getGame(id).setLobbySpawn(id, loc);
    }
}
