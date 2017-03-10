package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.LobbyManager;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.game.LobbySign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickListener implements Listener {

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(LobbyManager.getInstance().isLobbySign(event.getClickedBlock().getLocation())){
                LobbySign lobbySign = LobbyManager.getInstance().getLobbySign(event.getClickedBlock().getLocation());
                Game game= GameManager.getInstance().getGame(lobbySign.getSignGameID());

                game.addPlayer(event.getPlayer());
            }
        }
    }

}
