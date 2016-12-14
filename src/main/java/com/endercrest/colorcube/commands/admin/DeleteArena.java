package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DeleteArena implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return false;
        }
        if(args.length != 1){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Arena");
            return false;
        }

        int arenaId = Integer.parseInt(args[0]);
        Game game = GameManager.getInstance().getGame(arenaId);

        if(game == null){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + arenaId);
            return false;
        }

        game.disable();

        SettingsManager.getInstance().getArenaConfig(arenaId).set("enabled", false);
        SettingsManager.getInstance().saveArenaConfig(arenaId);

        if(!SettingsManager.getInstance().archiveArena(arenaId)){
            MessageManager.getInstance().sendFMessage("error.archive", p, "file-arena"+arenaId+".yml");
            return false;
        }
        MessageManager.getInstance().sendFMessage("info.delete", p, "arena-" + arenaId);
        GameManager.getInstance().removeArena(arenaId);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc deleteArena <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.delete", "Deletes a arena!");
    }

    @Override
    public String permission() {
        return "cc.arena.delete";
    }
}
