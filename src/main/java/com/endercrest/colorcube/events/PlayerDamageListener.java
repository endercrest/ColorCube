package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player)event.getEntity();
            if(GameManager.getInstance().isPlayerActive(player)) {
                EntityDamageEvent.DamageCause cause = event.getCause();
                if(cause.equals(EntityDamageEvent.DamageCause.FALL)){
                    event.setCancelled(true);
                }else if(cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){
                    event.setDamage(0);
                }
            }
        }
    }
}
