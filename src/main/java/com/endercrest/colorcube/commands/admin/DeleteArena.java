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
            return true;
        }
        if(args.length != 1){
            MessageManager.getInstance().sendFMessage("error.notspecified", p, "input-Arena");
            return true;
        }

        FileConfiguration system = SettingsManager.getInstance().getSystemConfig();
        int arena = Integer.parseInt(args[0]);
        Game game = GameManager.getInstance().getGame(arena);

        if(game == null){
            MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-" + arena);
            return true;
        }

        game.disable();
        system.set("arenas." + arena + ".enabled", false);
        system.set("arena_next_id",system.getInt("arena_next_id") + 1);
        MessageManager.getInstance().sendFMessage("info.delete", p, "arena-" + arena);
        SettingsManager.getInstance().saveSystemConfig();
        GameManager.getInstance().removeArena(arena);
        return false;
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
