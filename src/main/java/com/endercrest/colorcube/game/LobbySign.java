package com.endercrest.colorcube.game;

import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class LobbySign {

    private Location loc;
    private Game game;

    private int signGameID;

    public LobbySign(Location loc, Game game){
        this.loc = loc;
        this.game = game;
        this.signGameID = game.getGameID();
    }

    public void update(){
        Sign sign = (Sign)loc.getBlock();
        String line1 = SettingsManager.getInstance().getPluginConfig().getString("sign.line1");
        String line2 = SettingsManager.getInstance().getPluginConfig().getString("sign.line2");
        String line3 = SettingsManager.getInstance().getPluginConfig().getString("sign.line3");
        String line4 = SettingsManager.getInstance().getPluginConfig().getString("sign.line4");

        sign.setLine(0, MessageUtil.replaceVars(line1,getVars()));
        sign.setLine(1, MessageUtil.replaceVars(line2,getVars()));
        sign.setLine(2, MessageUtil.replaceVars(line3,getVars()));
        sign.setLine(3, MessageUtil.replaceVars(line4,getVars()));
    }

    public String[] getVars(){
        String[] vars = new String[0];
        vars[0] = "player-" + game.getActivePlayers().size();
        vars[1] = "maxplayers-" + game.getSpawnCount();
        vars[2] = "arenaName-Arena " + game.getGameID();
        vars[3] = "status-" + game.getStatus().toString();
        vars[4] = "spectators-" + game.getSpectators().size();
        return vars;
    }

    public int getSignGameID(){
        return signGameID;
    }
}
