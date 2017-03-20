package com.endercrest.colorcube.commands.player;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.entity.Player;

public class Vote implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())) {
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        if(!GameManager.getInstance().isPlayerActive(p)){
            MessageManager.getInstance().sendFMessage("error.notinarena", p);
            return true;
        }

        GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p)).vote(p);
        return false;
    }

    @Override
    public String helpInfo() {
        return "/cc vote - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.vote");
    }

    @Override
    public String permission() {
        return "cc.arena.vote";
    }
}
