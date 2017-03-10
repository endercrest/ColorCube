package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.MessageManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 3/3/2017.
 *
 * Handler for WorldBorder manipulation.
 */
public interface WorldBorderHandler {

    /**
     * This will reset the player world border back to the players current world border properties.
     * @param player The player to be reset back to normal.
     */
    void resetWorldBorder(Player player);

    /**
     * This will set the players world border to the specified parameters.
     * @param player The player the worldborder is being set for.
     * @param origin The new origin in which the world border will take.
     * @param radius The new radius of the world border.
     */
    void setWorldBorder(Player player, Location origin, double radius);

    class NullWorldBorderHandler implements WorldBorderHandler{

        @Override
        public void resetWorldBorder(Player player) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void setWorldBorder(Player player, Location origin, double radius) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }
    }
}
