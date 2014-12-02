package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallListener implements Listener {

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event){
        EntityDamageEvent.DamageCause cause = event.getCause();
        if(cause == EntityDamageEvent.DamageCause.FALL){
            if(event.getEntity() instanceof Player) {
                if (GameManager.getInstance().isPlayerActive((Player) event.getEntity())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
