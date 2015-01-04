package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerPickupListener implements Listener {

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        if(GameManager.getInstance().isPlayerActive(player)){
            for(int i = 0; i < 9; i++){
                if(player.getInventory().getItem(i) == null){
                    player.getInventory().setItem(i, event.getItem().getItemStack());
                    event.getItem().remove();
                }
            }
            event.setCancelled(true);
        }else if(GameManager.getInstance().isPlayerSpectator(player)){
            event.setCancelled(true);
        }
    }
}
