package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.PowerupManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerPickupListener implements Listener {

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        if(GameManager.getInstance().isPlayerSpectator(player)){
            event.setCancelled(true);
        }
    }
}
