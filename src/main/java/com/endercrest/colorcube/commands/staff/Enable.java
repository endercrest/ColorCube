package com.endercrest.colorcube.commands.staff;

import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.entity.Player;

public class Enable implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        try {
            if (args.length == 0) {
                for(Game g: GameManager.getInstance().getGames()){
                    if(g.getStatus() == Game.Status.DISABLED) {
                        g.enable();
                    }
                }
                MessageManager.getInstance().sendFMessage("game.all", p, "state-enabled");
                return true;
            } else {
                Game game = GameManager.getInstance().getGame(Integer.parseInt(args[0]));
                game.enable();

                MessageManager.getInstance().sendFMessage("game.state", p, "state-enabled");
            }
        }catch(NumberFormatException e){
            MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-arena");
        }catch(NullPointerException e){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-Arena " + args[0]);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc enable <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.enable", "Enables arena");
    }

    @Override
    public String permission() {
        return "cc.arena.enable";
    }
}
