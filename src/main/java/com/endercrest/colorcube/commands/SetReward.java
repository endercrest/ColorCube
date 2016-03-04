package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.entity.Player;

public class SetReward implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        if(args.length < 1){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Arena");
        }else if(args.length < 2){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Reward");
        }else{
            try {
                int id = Integer.parseInt(args[0]);
                double amount = Integer.parseInt(args[1]);
                if(GameManager.getInstance().getGame(id) != null) {
                    SettingsManager.getInstance().setReward(id, amount);
                    MessageManager.getInstance().sendFMessage("info.setreward", p);
                }else{
                    MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + args[0]);
                }
            }catch(NumberFormatException e){
                MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-" + args[0]);
            }
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc setreward (id) - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.setspawn", "Set spawn points in an arena.");
    }

    @Override
    public String permission() {
        return "cc.arena.setreward";
    }
}
