package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerPVPListener implements Listener {

    @EventHandler
    public void onPlayerPVP(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            if (GameManager.getInstance().isPlayerActive(attacker)) {
                Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID((Player) event.getDamager()));
                if(game.isPvp()){
                    event.setDamage(0);
                }else{
                    event.setCancelled(true);
                }
            } else if (GameManager.getInstance().isPlayerSpectator(attacker)) {
                event.setCancelled(true);
            }
        }
    }
}
