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

    HashMap<Integer, Integer> next = new HashMap<Integer, Integer>();

    public void loadNextSpawn(){
        for(Game g: GameManager.getInstance().getGames().toArray(new Game[0])){ //Avoid Concurrency problems
            next.put(g.getId(), SettingsManager.getInstance().getSpawnCount(g.getId())+1);
        }
    }

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        loadNextSpawn();
        MessageManager.getInstance().debugConsole("Loading spawns");
        Location loc = p.getLocation();
        int id = GameManager.getInstance().getBlockGameId(loc);
        if(id == -1){
            MessageManager.getInstance().sendFMessage("error.notinarena", p);
            return true;
        }
        if(args.length == 1) {
            int i = 0;
            if (args[0].equalsIgnoreCase("next")) {
                i = next.get(id);
                next.put(id, next.get(id));
            } else {
                try {
                    i = Integer.parseInt(args[0]);
                    if (i > next.get(id) + 1 || i < 1) {
                        MessageManager.getInstance().sendFMessage("error.between", p, "num-" + next.get(id));
                        return true;
                    }
                    if (i == next.get(id)) {
                        next.put(id, next.get(id) + 1);
                    }
                } catch (Exception e) {
                    MessageManager.getInstance().sendFMessage("error.badinput", p);
                    return true;
                }
            }
            SettingsManager.getInstance().setSpawn(id, i, loc);
            MessageManager.getInstance().sendFMessage("info.spawnset", p, "num-" + i, "arena-" + id);
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
