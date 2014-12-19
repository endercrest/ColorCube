package com.endercrest.colorcube.api;

import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPowerupEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private Game game;
    private int powerupTypeID;

    public PlayerPowerupEvent(Player player, Game game, int powerupTypeID){
        this.player = player;
        this.game = game;
        this.powerupTypeID = powerupTypeID;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public int getPowerupTypeID() {
        return powerupTypeID;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
