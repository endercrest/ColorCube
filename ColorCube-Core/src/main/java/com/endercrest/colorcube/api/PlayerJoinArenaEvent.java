package com.endercrest.colorcube.api;

import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinArenaEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private Game game;

    public PlayerJoinArenaEvent(Player player, Game game){
        this.player = player;
        this.game = game;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public Game getGame(){
        return game;
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
