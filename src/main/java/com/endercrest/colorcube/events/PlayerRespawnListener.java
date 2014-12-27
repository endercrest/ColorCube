package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player p = event.getPlayer();
        if (GameManager.getInstance().isPlayerActive(p)) {
            int id = GameManager.getInstance().getPlayerGameID(p);
            Game game = GameManager.getInstance().getGame(id);
            for (int spawnID : game.getSpawns().keySet()) {
                if (game.getSpawns().get(spawnID).equals(p)) {
                    if (game.getStatus() == Game.Status.INGAME) {
                        event.setRespawnLocation(SettingsManager.getInstance().getSpawnPoint(id, spawnID));
                        return;
                    } else if (game.getStatus() == Game.Status.LOBBY || game.getStatus() == Game.Status.STARTING) {
                        event.setRespawnLocation(GameManager.getInstance().getGame(id).getLobbySpawn());
                        return;
                    }
                }
            }
        }
    }
}
