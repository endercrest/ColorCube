package com.endercrest.colorcube.events;

import com.endercrest.colorcube.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Thomas on 11/29/2014.
 */
public class PlayerPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        if(GameManager.getInstance().isPlayerActive(player)){
            event.setCancelled(true);
            return;
        }

        if(GameManager.getInstance().getBlockGameId(event.getBlock().getLocation()) != -1){
            if(GameManager.getInstance().getGame(GameManager.getInstance().getBlockGameId(event.getBlock().getLocation())).getStatus() == Game.Status.INGAME){
                event.setCancelled(true);
                MessageManager.getInstance().sendMessage("&cCan't break blocks when the game is in-game", player);
                return;
            }
        }
    }
}
