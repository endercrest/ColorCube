package com.endercrest.colorcube.commands.staff;

import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.entity.Player;

public class ForceStart implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        int id = -1;
        int time = 10;
        if(args.length == 2){
            time = Integer.parseInt(args[1]);
        }
        if(args.length >= 1){
            id = Integer.parseInt(args[0]);
        }else{
            id = GameManager.getInstance().getActivePlayerGameID(p);
        }

        if(id == -1){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p);
            return true;
        }
        if(GameManager.getInstance().getGame(id).getActivePlayers().size() < 2){
            MessageManager.getInstance().sendFMessage("error.notenoughplayers", p);
            return true;
        }

        Game g = GameManager.getInstance().getGame(id);
        if(g.getStatus() == Game.Status.INGAME){
            MessageManager.getInstance().sendFMessage("error.alreadyingame", p);
            return true;
        }

        g.countdown(time);

        MessageManager.getInstance().sendFMessage("game.started", p, "arena-" + id);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc forcestart <id> <time> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.forcestart");
    }

    @Override
    public String permission() {
        return "cc.admin.forcestart";
    }
}
