package com.endercrest.colorcube.events;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.utils.WorldBorderUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private ColorCube plugin;

    public PlayerRespawnListener(ColorCube plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player p = event.getPlayer();
        if(GameManager.getInstance().isPlayerActive(p)){
            final Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p));
            if(game.getStatus() == Game.Status.INGAME){
                event.setRespawnLocation(game.getSpawn(p));
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if(game.isBorder() && !game.isBorderSpectatorOnly())
                            WorldBorderUtil.setWorldBorder(p, game.getArena().getCentre(), game.getArena().getRadius()*2+game.getBorderExtension());
                    }
                }, 1);
            }else if(game.getStatus() == Game.Status.LOBBY || game.getStatus() == Game.Status.STARTING){
                event.setRespawnLocation(game.getLobbySpawn());
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if(game.isBorder() && !game.isBorderSpectatorOnly())
                            WorldBorderUtil.setWorldBorder(p, game.getLobby().getCentre(), game.getLobby().getRadius()*2+game.getBorderExtension());
                    }
                }, 1);
                }
        }else if(GameManager.getInstance().isPlayerSpectator(p)){
            final Game game = GameManager.getInstance().getGame(GameManager.getInstance().getSpectatePlayerId(p));
            event.setRespawnLocation(game.getTeamSpawns().values().iterator().next());
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    if(game.isBorder())
                        WorldBorderUtil.setWorldBorder(p, game.getArena().getCentre(), game.getArena().getRadius()*2+game.getBorderExtension());
                }
            }, 1);
        }
    }
}
