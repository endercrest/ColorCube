package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SetSpawn implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        Location loc = p.getLocation();
        int gameId = GameManager.getInstance().getBlockGameId(loc);
        if(gameId == -1){
            MessageManager.getInstance().sendFMessage("error.notinarena", p);
            return true;
        }

        if(args.length == 1){
            try{
                Game.CCTeam team = Game.CCTeam.valueOf(args[0].toUpperCase());
                SettingsManager.getInstance().setSpawn(gameId, team, loc);
                MessageManager.getInstance().sendFMessage("info.spawnset", p, "team-" + Game.getTeamNameLocalized(team), "arena-" + gameId);
            }catch (IllegalArgumentException ex){
                MessageManager.getInstance().sendFMessage("error.badinput", p);
            }
        }else{
            MessageManager.getInstance().sendFMessage("info.setspawnusage", p);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc setspawn - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.setspawn", "Set spawn points in an arena.");
    }

    @Override
    public String permission() {
        return "cc.arena.setspawn";
    }
}
