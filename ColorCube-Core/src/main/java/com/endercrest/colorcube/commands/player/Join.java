package com.endercrest.colorcube.commands.player;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.entity.Player;

public class Join implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if (args.length >= 1) {
            try {
                 int id = Integer.parseInt(args[0]);
                 if(GameManager.getInstance().getGame(id) != null)
                    GameManager.getInstance().addPlayer(p, id);
                 else{
                     String name = "";
                     for(int i = 0; i < args.length; i++){
                         if(i != 0)
                             name += " ";
                         name += args[i];
                     }
                     GameManager.getInstance().addPlayer(p, name);
                 }
            }catch(NumberFormatException e){
                String name = "";
                for(int i = 0; i < args.length; i++){
                    if(i != 0)
                        name += " ";
                    name += args[i];
                }
                GameManager.getInstance().addPlayer(p, name);
            }
        } else {
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Arena");
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc join <id/name> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.join");
    }

    @Override
    public String permission() {
        return "cc.arena.join";
    }
}
