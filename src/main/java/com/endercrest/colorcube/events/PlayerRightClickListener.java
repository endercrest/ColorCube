package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.PowerupManager;
import com.endercrest.colorcube.powerups.SubPowerup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerRightClickListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(GameManager.getInstance().isPlayerActive(player)){
                try {
                    SubPowerup powerup = PowerupManager.getInstance().getPowerup(player.getItemInHand());
                    powerup.onRightClick(player, GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameID(player)));
                    event.setCancelled(true);
                    player.updateInventory();
                }catch(NullPointerException e){}
            }
        }
    }
}
