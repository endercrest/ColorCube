package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.entity.Player;

public class SetGlobalLobbySpawn implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        SettingsManager.getInstance().setGlobalLobbySpawn(p.getLocation());
        MessageManager.getInstance().sendFMessage("info.lobbyspawn", p);
        return false;
    }

    @Override
    public String helpInfo() {
        return "/cc setgloballobbyspawn - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.setgloballobbyspawn", "Set global lobby spawn");
    }

    @Override
    public String permission() {
        return "cc.admin.setgloballobbyspawn";
    }
}
