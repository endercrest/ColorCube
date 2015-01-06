package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPickupListener implements Listener {

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        if(GameManager.getInstance().isPlayerActive(player)){
            for(int i = 0; i < 9; i++){
                if(player.getInventory().getItem(i) == null){
                    if(event.getItem().getItemStack().getAmount() == 1) {
                        player.getInventory().setItem(i, event.getItem().getItemStack());
                        event.getItem().remove();
                    }else{
                        ItemStack item = event.getItem().getItemStack().clone();
                        item.setAmount(1);
                        player.getInventory().setItem(i, item);
                        event.getItem().getItemStack().setAmount(event.getItem().getItemStack().getAmount() - 1);
                    }
                    break;
                }
            }
            event.setCancelled(true);
        }else if(GameManager.getInstance().isPlayerSpectator(player)){
            event.setCancelled(true);
        }
    }
}
