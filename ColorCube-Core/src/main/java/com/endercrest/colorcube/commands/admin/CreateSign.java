package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.LobbyManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;

public class CreateSign implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        if(args.length < 1){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Arena");
            return true;
        }

        try{
            int id = Integer.parseInt(args[0]);
            Game game = GameManager.getInstance().getGame(id);
            if(game == null){
                MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-Arena " + id);
                return true;
            }
            LobbyManager.getInstance().createLobbySign(p, game);
        }catch(NumberFormatException e){
            MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-" + args[0]);
        }
        return false;
    }

    @Override
    public String helpInfo() {
        return "/cc createsign <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.createsign");
    }

    @Override
    public String permission() {
        return "cc.lobby.sign";
    }
}
