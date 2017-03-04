package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.LobbyManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.entity.Player;

public class DeleteSign implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        LobbyManager.getInstance().removeLobbySign(p);
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc deletesign - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.deletesign");
    }

    @Override
    public String permission() {
        return "cc.lobby.sign.delete";
    }
}
