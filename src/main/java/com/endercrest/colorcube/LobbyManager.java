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
import java.util.Set;

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

        system.set("arenas." + game.getId() + ".lworld", pos1.getWorld().getName());
        system.set("arenas." + game.getId() + ".lx1", pos1.getBlockX());
        system.set("arenas." + game.getId() + ".ly1", pos1.getBlockY());
        system.set("arenas." + game.getId() + ".lz1", pos1.getBlockZ());
        system.set("arenas." + game.getId() + ".lx2", pos2.getBlockX());
        system.set("arenas." + game.getId() + ".ly2", pos2.getBlockY());
        system.set("arenas." + game.getId() + ".lz2", pos2.getBlockZ());
        SettingsManager.getInstance().saveSystemConfig();
        MessageManager.getInstance().sendFMessage("info.createlobby", p, "arena-" + game.getId());
    }

    public void setLobbySpawn(int id, Location loc){
        GameManager.getInstance().getGame(id).setLobbySpawn(id, loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public void createLobbySign(Player p, Game game) {
        if(!p.getTargetBlock((Set)null, 10).getType().equals(Material.WALL_SIGN)){
            MessageManager.getInstance().sendFMessage("error.notsign", p);
            return;
        }

        for(LobbySign sign: lobbySigns){
            if(sign.getLocation().equals(p.getTargetBlock((Set)null, 10).getLocation())){
                MessageManager.getInstance().sendFMessage("error.alreadysign", p);
                return;
            }
        }

        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();

        int id = SettingsManager.getInstance().getNextSignID() + 1;
        system.set("sign_next_id", id);
        if(id == 0 || lobbySigns.isEmpty()){
            id = 1;
        }
        Location loc = p.getTargetBlock((Set)null, 10).getLocation();

        system.set("signs." + id, null);
        system.set("signs." + id + ".x", loc.getBlockX());
        system.set("signs." + id + ".y", loc.getBlockY());
        system.set("signs." + id + ".z", loc.getBlockZ());
        system.set("signs." + id + ".world", loc.getWorld().getName());
        system.set("signs." + id + ".gameID", game.getId());
        system.set("signs." + id + ".enabled", true);
        SettingsManager.getInstance().saveSystemConfig();

        LobbySign sign = new LobbySign(loc, game, id);
        lobbySigns.add(sign);
        sign.update();
        MessageManager.getInstance().debugConsole("Created LobbySign: " + id);
        MessageManager.getInstance().sendFMessage("lobby.created", p);
    }

    public void removeLobbySign(Player p){
        if(!p.getTargetBlock((Set)null, 10).getType().equals(Material.WALL_SIGN) && !p.getTargetBlock((Set)null, 10).getType().equals(Material.SIGN_POST)){
            MessageManager.getInstance().sendFMessage("error.notsign", p);
            return;
        }
        LobbySign lobbySign = getLobbySign(p.getTargetBlock((Set)null, 10).getLocation());
        if(lobbySign != null) {
            //SettingsManager.getInstance().getSystemConfig().set("sign_next_id", SettingsManager.getInstance().getSystemConfig().getInt("sign_next_id") + 1);
            SettingsManager.getInstance().getSystemConfig().set("signs." + lobbySign.getSignID() + ".enabled", false);
            SettingsManager.getInstance().saveSystemConfig();
            lobbySigns.remove(lobbySign);
            lobbySign.clear();
            MessageManager.getInstance().debugConsole("Deleting Sign:" + lobbySign.getSignID());
            MessageManager.getInstance().sendFMessage("lobby.deleted", p);
        }else{
            MessageManager.getInstance().sendFMessage("error.nolobbysign", p);
        }
    }

    private void loadSigns(){
        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        lobbySigns.clear();
        for(String key: system.getConfigurationSection("signs").getKeys(false)){
            if(isConfigured(key)){
                if(system.getBoolean("signs."+key+".enabled", false)){
                    MessageManager.getInstance().debugConsole("Loading Sign: " + key);
                    int x = system.getInt("signs."+key+".x");
                    int y = system.getInt("signs."+key+".y");
                    int z = system.getInt("signs."+key+".z");
                    World world = Bukkit.getWorld(system.getString("signs."+key+".world"));
                    int gameId = system.getInt("signs."+key+".gameID");
                    Game game = GameManager.getInstance().getGame(gameId);
                    Location loc = new Location(world, x, y, z);
                    if(loc.getBlock().getType().equals(Material.WALL_SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST)){
                        try {
                            lobbySigns.add(new LobbySign(loc, game, Integer.parseInt(key)));
                        }catch (NumberFormatException ex){
                            MessageManager.getInstance().debugConsole(String.format("&cExpected an integer id for \"%s\" but found a different type!", key));
                        }
                    }else{
                        MessageManager.getInstance().debugConsole(String.format("No sign at set location. Aborting loading of sign %s", key));
                        return;
                    }
                }else{
                    MessageManager.getInstance().debugConsole(String.format("Sign %s disabled, ignoring it.", key));
                }
            }else{
                MessageManager.getInstance().debugConsole(String.format("Sign %s is not configured correctly, skipping this sign.", key));
            }
        }
    }

    private boolean isConfigured(String key){
        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        return system.isSet("signs."+key+".x") && system.isSet("signs."+key+".y") && system.isSet("signs."+key+".z") &&
                system.isSet("signs."+key+".world") && system.isSet("signs."+key+".gameID") && system.isSet("signs."+key+".enabled");
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

    public boolean isLobbySign(Location loc){
        for(LobbySign sign: lobbySigns){
            if(sign.getLocation().equals(loc)){
                return true;
            }
        }
        return false;
    }
}
