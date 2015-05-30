package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.game.Game;
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
                    Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(player));
                    if(game.getStatus() == Game.Status.LOBBY){
                        event.setDamage(0);
                    }else{
                        if(!game.isPvp()){
                            event.setDamage(0);
                            MessageManager.getInstance().debug("Setting Damage to 0.", player);
                        }else{
                            MessageManager.getInstance().debug("Hitting Player", player);
                            event.setDamage(event.getOriginalDamage(EntityDamageEvent.DamageModifier.BASE));
                        }
                    }
                }
            }
        }
    }
}
