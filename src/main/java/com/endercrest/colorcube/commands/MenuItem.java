package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.MenuManager;
import com.endercrest.colorcube.MessageManager;
import org.bukkit.entity.Player;

public class MenuItem implements SubCommand {


    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        if(p.getInventory().contains(MenuManager.getInstance().getMenuItemStack())){
            MessageManager.getInstance().sendFMessage("error.alreadyhave", p, "input-a menu item");
        }else {
            p.getInventory().addItem(MenuManager.getInstance().getMenuItemStack());
            MessageManager.getInstance().sendFMessage("menu.added", p);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc menuitem - Spawns a menu item into your inventory.";
    }

    @Override
    public String permission() {
        return "cc.lobby.menu";
    }
}
