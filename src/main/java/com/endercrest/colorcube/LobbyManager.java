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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LobbyManager {

    public static LobbyManager instance = new LobbyManager();

    private ColorCube plugin;
    private List<LobbySign> lobbySigns;

    public static LobbyManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;
        lobbySigns = new ArrayList<>();
        loadSigns();
        updateAll();
        MessageManager.getInstance().debugConsole("&eLobby Manager Set up");
    }

    public void createLobbyFromSelection(Player p, Game game){
        WorldEditPlugin worldEdit = plugin.getWorldEdit();
        Selection selection = worldEdit.getSelection(p);

        if(selection == null){
            MessageManager.getInstance().sendFMessage("error.noselection", p);
            return;
        }
        Location pos1 = selection.getMaximumPoint();//Max
        Location pos2 = selection.getMinimumPoint();//Min

        game.setLobby(new Lobby(pos1, pos2));

        YamlConfiguration config = SettingsManager.getInstance().getArenaConfig(game.getId());

        config.set("lobby.world", pos1.getWorld().getName());
        config.set("lobby.pos1.x", pos1.getBlockX());
        config.set("lobby.pos1.y", pos1.getBlockY());
        config.set("lobby.pos1.z", pos1.getBlockZ());
        config.set("lobby.pos2.x", pos2.getBlockX());
        config.set("lobby.pos2.y", pos2.getBlockY());
        config.set("lobby.pos2.z", pos2.getBlockZ());
        SettingsManager.getInstance().saveArenaConfig(game.getId());
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

        int id = SettingsManager.getInstance().getNextSignID();
        SettingsManager.getInstance().incrementNextSignId();

        Location loc = p.getTargetBlock((Set)null, 10).getLocation();

        if(SettingsManager.getInstance().createSignConfig(id, game.getId(), loc) == null){
            MessageManager.getInstance().sendFMessage("error.nextid", p,
                    "type-"+MessageManager.getInstance().getFValue("words.sign"));
            return;
        }

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
            SettingsManager.getInstance().getSignConfig(lobbySign.getSignID()).set("enabled", false);
            SettingsManager.getInstance().saveSignConfig(lobbySign.getSignID());
            SettingsManager.getInstance().archiveSign(lobbySign.getSignID());
            lobbySigns.remove(lobbySign);
            lobbySign.clear();
            MessageManager.getInstance().debugConsole("Deleting Sign:" + lobbySign.getSignID());
            MessageManager.getInstance().sendFMessage("lobby.deleted", p);
        }else{
            MessageManager.getInstance().sendFMessage("error.nolobbysign", p);
        }
    }

    private void loadSigns(){
        lobbySigns.clear();
        HashMap<Integer, YamlConfiguration> signConfigs = SettingsManager.getInstance().getSignConfigs();

        for(int id: signConfigs.keySet()){
            YamlConfiguration config = signConfigs.get(id);
            if(isConfigured(config)){
                if(config.getBoolean("enabled", false)){
                    MessageManager.getInstance().debugConsole("Loading Sign: " + id);
                    int x = config.getInt("loc.x");
                    int y = config.getInt("loc.y");
                    int z = config.getInt("loc.z");
                    World world = Bukkit.getWorld(config.getString("loc.world"));
                    int gameId = config.getInt("gameId");
                    Game game = GameManager.getInstance().getGame(gameId);
                    if(game != null) {
                        Location loc = new Location(world, x, y, z);
                        if (loc.getBlock().getType().equals(Material.WALL_SIGN) || loc.getBlock().getType().equals(Material.SIGN_POST)) {
                            LobbySign lobbySign = new LobbySign(loc, game, id);
                            lobbySigns.add(lobbySign);
                        } else {
                            MessageManager.getInstance().debugConsole(String.format("No sign at set location. Aborting loading of sign %s", id));
                            return;
                        }
                    }else{
                        MessageManager.getInstance().debugConsole(String.format("Invalid game id set for sign %s.", id));
                    }
                }else{
                    MessageManager.getInstance().debugConsole(String.format("Sign %s disabled, ignoring it.", id));
                }
            }else{
                MessageManager.getInstance().debugConsole(String.format("Sign %s is not configured correctly, skipping this sign.", id));
            }
        }
    }

    private boolean isConfigured(YamlConfiguration config){
        return config.isSet("id") && config.isSet("loc.x") && config.isSet("loc.y") && config.isSet("loc.z") &&
                config.isSet("loc.world") && config.isSet("gameId") && config.isSet("enabled");
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
