package com.endercrest.colorcube.menu;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

public class Menu implements InventoryHolder {

    private List<Inventory> inventories = new ArrayList<Inventory>();

    public Menu(){
        int pagesNum = GameManager.getInstance().getGames().size() / 27;
        if(pagesNum > 1) {
            for (int i = 0; i < pagesNum; i++) {

            }
        }else{
            inventories.add(0, Bukkit.createInventory(this, 3 * 9, ChatColor.GOLD + "Game Selector"));
            for(Game game: GameManager.getInstance().getGames()){

            }
        }
    }

    public Inventory getInventory(int page){
        if(page >= 1) {
            return inventories.get(page - 1);
        }
        MessageManager.getInstance().debugConsole("Trying to open a page that does not exist.");
        return null;
    }

    @Override
    public Inventory getInventory() {

        return null;
    }
}
