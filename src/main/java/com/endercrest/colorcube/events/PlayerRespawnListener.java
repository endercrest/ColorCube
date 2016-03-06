package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if(GameManager.getInstance().isPlayerActive(p)){
            Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p));
            if(game.getStatus() == Game.Status.INGAME){
                for(int id: game.getSpawns().keySet()){
                    Player check = game.getSpawns().get(id);
                    if(check != null) {
                        if (check.getUniqueId().equals(p.getUniqueId())) {
                            event.setRespawnLocation(SettingsManager.getInstance().getSpawnPoint(game.getId(), id));
                        }
                    }
                }
            }else if(game.getStatus() == Game.Status.LOBBY || game.getStatus() == Game.Status.STARTING){
                event.setRespawnLocation(game.getLobbySpawn());
            }
        }
    }
}
