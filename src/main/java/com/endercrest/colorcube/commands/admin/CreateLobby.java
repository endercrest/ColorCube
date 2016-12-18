package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.*;
import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;

public class CreateLobby implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        try {
            if (args.length == 1) {
                int id = Integer.parseInt(args[0]);
                Game game = GameManager.getInstance().getGame(id);
                LobbyManager.getInstance().createLobbyFromSelection(p, game);
            }else{
                MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-arena");
            }
        }catch(NumberFormatException e){
            MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-" + args[0]);
        }catch(NullPointerException e){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "input-Arena " + args[0]);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc createlobby <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.createlobby");
    }

    @Override
    public String permission() {
        return "cc.lobby.create";
    }
}
