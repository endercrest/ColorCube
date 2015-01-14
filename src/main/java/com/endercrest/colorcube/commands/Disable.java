package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.entity.Player;

public class Disable implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        try {
            if (args.length == 0) {
                for (Game g : GameManager.getInstance().getGames()) {
                    g.disable();
                }
                MessageManager.getInstance().sendFMessage("game.all", p, "state-disabled");
                return true;
            } else {

                int arena = Integer.parseInt(args[0]);
                Game game = GameManager.getInstance().getGame(arena);

                game.disable();
                MessageManager.getInstance().sendFMessage("game.state", p, "arena-" + arena, "state-disabled");
            }
        }catch(NumberFormatException e){
            MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-arena");
        }catch(NullPointerException e2){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + args[0]);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc disable <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.info.disable", "Disable arena");
    }

    @Override
    public String permission() {
        return "cc.arena.disable";
    }
}
