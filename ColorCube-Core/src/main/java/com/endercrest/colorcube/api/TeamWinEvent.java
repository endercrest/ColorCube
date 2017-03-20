package com.endercrest.colorcube.api;

import com.endercrest.colorcube.game.Game;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.UUID;

public class TeamWinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Set<OfflinePlayer> players;
    private Game.CCTeam team;
    private double reward;

    public TeamWinEvent(Set<OfflinePlayer> players, Game.CCTeam team, double reward){
        this.players = players;
        this.team = team;
        this.reward = reward;
    }

    /**
     * Get players from the winning team
     * @return List of players
     */
    public Set<OfflinePlayer> getPlayers(){
        return players;
    }

    /**
     * Get a player with the name
     * @param name Name of the player
     * @return The Player
     */
    public OfflinePlayer getPlayer(String name){
        for(OfflinePlayer player: players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    /**
     * Get a player with the UUID
     * @param uuid UUID of the player
     * @return The Player
     */
    public OfflinePlayer getPlayer(UUID uuid){
        for(OfflinePlayer player: players){
            if(player.getUniqueId().equals(uuid)){
                return player;
            }
        }
        return null;
    }

    public double getReward(){
        return reward;
    }

    public Game.CCTeam getTeam(){
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
