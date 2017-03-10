package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(GameManager.getInstance().isPlayerActive(player)){
            GameManager.getInstance().removePlayer(player, true);
        }else if(GameManager.getInstance().isPlayerSpectator(player)){
            GameManager.getInstance().removeSpectator(player, true);
        }
    }
}
