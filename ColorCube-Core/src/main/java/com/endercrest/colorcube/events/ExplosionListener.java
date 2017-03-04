package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class ExplosionListener implements Listener {

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent event){
        int id = GameManager.getInstance().getBlockGameId(event.getEntity().getLocation());
        if(id != -1){
            event.setCancelled(true);
        }
    }
}
