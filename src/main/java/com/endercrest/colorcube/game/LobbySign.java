package com.endercrest.colorcube.game;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class LobbySign {

    private Location loc;
    private Game game;

    private int signID;
    private int signGameID;

    public LobbySign(Location loc, Game game, int signID){
        this.loc = loc;
        this.game = game;
        this.signGameID = game.getId();
        this.signID = signID;
    }

    public Location getLocation(){
        return loc;
    }

    public Sign getSign(){
        return (Sign) loc.getBlock();
    }

    public int getSignID(){
        return signID;
    }

    public void update(){
        try {
            Sign sign = (Sign) loc.getBlock().getState();
            String line1 = SettingsManager.getInstance().getPluginConfig().getString("sign.line1");
            String line2 = SettingsManager.getInstance().getPluginConfig().getString("sign.line2");
            String line3 = SettingsManager.getInstance().getPluginConfig().getString("sign.line3");
            String line4 = SettingsManager.getInstance().getPluginConfig().getString("sign.line4");

            sign.setLine(0, MessageManager.getInstance().colorize(MessageUtil.replaceVars(line1, getVars())));
            sign.setLine(1, MessageManager.getInstance().colorize(MessageUtil.replaceVars(line2, getVars())));
            sign.setLine(2, MessageManager.getInstance().colorize(MessageUtil.replaceVars(line3, getVars())));
            sign.setLine(3, MessageManager.getInstance().colorize(MessageUtil.replaceVars(line4, getVars())));
            sign.update();
            MessageManager.getInstance().debugConsole("Updating Sign " + signID);
        }catch(ClassCastException e){
            MessageManager.getInstance().log("&c[ERROR] No sign at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ". Aporting sign update. Please fix sign: " + signID);
        }
    }

    public void clear(){
        try {
            Sign sign = (Sign) loc.getBlock().getState();

            sign.setLine(0, "");
            sign.setLine(1, "");
            sign.setLine(2, "");
            sign.setLine(3, "");
            sign.update();
            MessageManager.getInstance().debugConsole("Clearing Sign " + signID);
        }catch(ClassCastException e){
            MessageManager.getInstance().log("&c[ERROR] No sign at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ". Aporting sign update. Please fix sign: " + signID);
        }
    }

    public String[] getVars(){
        String[] vars = new String[5];
        vars[0] = "players-" + game.getActivePlayers().size();
        vars[1] = "maxplayers-" + game.getTotalSlots();
        vars[2] = "arenaname-Arena " + game.getId();
        vars[3] = "status-" + game.getStatus().toString();
        vars[4] = "spectators-" + game.getSpectators().size();
        return vars;
    }

    public int getSignGameID(){
        return signGameID;
    }
}
