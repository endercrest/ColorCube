package com.endercrest.colorcube.api;

import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveArenaEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Game game;
    private boolean logout;

    public PlayerLeaveArenaEvent(Player player, Game game, boolean logout){
        this.player = player;
        this.game = game;
        this.logout = logout;
    }

    public Game getGame(){
        return game;
    }

    public Player getPlayer(){
        return player;
    }

    public boolean isLogout(){
        return logout;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
