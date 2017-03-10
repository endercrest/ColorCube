package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.entity.Player;

public class CreateArena implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        GameManager.getInstance().createArenaFromSelection(p);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc createarena <id> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.createarena", "Create a new arena");
    }

    @Override
    public String permission() {
        return "cc.admin.arena.create";
    }
}
