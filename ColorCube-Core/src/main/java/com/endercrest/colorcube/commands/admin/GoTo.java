package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 12/18/2016.
 *
 * GoTo command class that handles the permissions, helpinfo and onCommand execution.
 */
public class GoTo implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        if(args.length != 1){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Arena");
            return false;
        }

        int arenaId = Integer.parseInt(args[0]);
        Game game = GameManager.getInstance().getGame(arenaId);

        if(game == null){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-Arena " + arenaId);
            return false;
        }

        if(game.getLobbySpawn() == null){
            MessageManager.getInstance().sendFMessage("error.nolobbyspawn", p, "arena-Arena " + arenaId);
            return false;
        }
        p.teleport(game.getLobbySpawn());
        MessageManager.getInstance().sendFMessage("info.teleported", p, "arena-Arena " + arenaId);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc goto <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.goto");
    }

    @Override
    public String permission() {
        return "cc.arena.goto";
    }
}
