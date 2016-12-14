package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.entity.Player;

public class ResetSpawns implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        if(args.length != 1){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-id");
            return true;
        }
        try{
            SettingsManager.getInstance().getArenaConfig(Integer.parseInt(args[0])).set("spawns", null);
            SettingsManager.getInstance().saveArenaConfig(Integer.parseInt(args[0]));
        }catch(NumberFormatException e){
            MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-" + args[0]);
        }catch(NullPointerException e){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + args[0]);
        }
        MessageManager.getInstance().sendFMessage("error.resetspawns", p);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc resetspawns <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.resetspawns", "Reset spawns in the arena");
    }

    @Override
    public String permission() {
        return "cc.arena.resetspawns";
    }
}
