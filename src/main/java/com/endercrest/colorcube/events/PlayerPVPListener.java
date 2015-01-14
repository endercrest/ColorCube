package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerPVPListener implements Listener {

    @EventHandler
    public void onPlayerPVP(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if (GameManager.getInstance().isPlayerActive(attacker)) {
                event.setDamage(0);
            } else if (GameManager.getInstance().isPlayerSpectator(attacker)) {
                event.setCancelled(true);
            }
        }
    }
}
