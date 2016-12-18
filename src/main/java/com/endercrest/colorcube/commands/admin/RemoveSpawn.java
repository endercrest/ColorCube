package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.LobbyManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 12/17/2016.
 */
public class RemoveSpawn implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        if(args.length == 0){
            MessageManager.getInstance().sendFMessage("info.removespawnusage", p);
        }else if(args.length == 1){
            int gameId = GameManager.getInstance().getBlockGameId(p.getLocation());
            if(gameId == -1){
                MessageManager.getInstance().sendFMessage("error.notinarena", p);
                return true;
            }

            try{
                Game.CCTeam team = Game.CCTeam.valueOf(args[0].toUpperCase());
                SettingsManager.getInstance().removeSpawn(gameId, team);
                MessageManager.getInstance().sendFMessage("info.spawnremove", p, "team-" + Game.getTeamNameLocalized(team), "arena-" + gameId);
            }catch (IllegalArgumentException ex){
                MessageManager.getInstance().sendFMessage("error.badinput", p);
            }
        }else{
            try{
                int id = Integer.parseInt(args[1]);
                Game game = GameManager.getInstance().getGame(id);
                if(game == null){
                    MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-Arena " + id);
                    return true;
                }

                Game.CCTeam team = Game.CCTeam.valueOf(args[0].toUpperCase());
                SettingsManager.getInstance().removeSpawn(id, team);
                MessageManager.getInstance().sendFMessage("info.spawnremove", p, "team-" + Game.getTeamNameLocalized(team), "arena-" + id);

            }catch(NumberFormatException e){
                MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-" + args[1]);
            }catch (IllegalArgumentException ex){
                MessageManager.getInstance().sendFMessage("error.badinput", p);
            }
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc removespawn - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.removespawn", "Remove the spawn in an arena.");

    }

    @Override
    public String permission() {
        return "cc.arena.removespawn";
    }
}
