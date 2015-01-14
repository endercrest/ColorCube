package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.entity.Player;

public class Leave implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(GameManager.getInstance().getActivePlayerGameID(p) != -1){
            GameManager.getInstance().removePlayer(p, false);
            return true;
        }else if(GameManager.getInstance().getSpectatePlayerId(p) != -1){
            GameManager.getInstance().removeSpectator(p, false);
            return true;
        }else{
            MessageManager.getInstance().sendFMessage("error.notinarena", p);
            return true;
        }
    }

    @Override
    public String helpInfo() {
        return "/cc leave - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.leave", "Leaves the game");
    }

    @Override
    public String permission() {
        return null;
    }
}
