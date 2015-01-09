package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(GameManager.getInstance().isPlayerActive(player)){
            event.setCancelled(true);
        }else if(GameManager.getInstance().isPlayerSpectator(player)){
            event.setCancelled(true);
        }
    }
}
