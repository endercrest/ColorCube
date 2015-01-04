package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player p = (Player)event.getWhoClicked();
            int id = GameManager.getInstance().getActivePlayerGameID(p);
            if(id != -1){
                if(GameManager.getInstance().getGame(id).getStatus() == Game.Status.INGAME) {
                    if(p.getGameMode() != GameMode.CREATIVE) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
