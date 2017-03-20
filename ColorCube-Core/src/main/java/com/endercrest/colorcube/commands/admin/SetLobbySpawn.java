package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.LobbyManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetLobbySpawn implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        Location loc = p.getLocation();
        int id = GameManager.getInstance().getBlockGameIdLobby(loc);
        if(id == -1){
            MessageManager.getInstance().sendFMessage("error.notinlobby", p);
            return true;
        }
        LobbyManager.getInstance().setLobbySpawn(id, loc);
        MessageManager.getInstance().sendFMessage("info.lobbyspawn", p, "arena-" + id);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc setlobbyspawn - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.setlobbyspawn", "Set lobby spawn for arena");
    }

    @Override
    public String permission() {
        return "cc.lobby.setlobbyspawn";
    }
}
