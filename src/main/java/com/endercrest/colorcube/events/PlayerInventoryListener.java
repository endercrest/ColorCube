package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.menu.GameItem;
import com.endercrest.colorcube.menu.Page;
import com.endercrest.colorcube.menu.PageItem;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player p = (Player)event.getWhoClicked();
            if(GameManager.getInstance().isPlayerActive(p)) {
                int id = GameManager.getInstance().getActivePlayerGameID(p);
                if (GameManager.getInstance().getGame(id).getStatus() == Game.Status.INGAME) {
                    if (p.getGameMode() != GameMode.CREATIVE) {
                        event.setCancelled(true);
                    }
                }
            }

            if(GameManager.getInstance().isPlayerSpectator(p)){
                event.setCancelled(true);
            }

            if(!event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
                if (event.getInventory().getHolder() instanceof Page) {
                    if (event.getClickedInventory().getHolder() instanceof Page) {
                        Page page = (Page) event.getClickedInventory().getHolder();
                        PageItem pageItem = page.getPageItem(event.getSlot());
                        if (pageItem != null) {
                            pageItem.onClick(p);
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}
