package com.endercrest.colorcube.utils;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Thomas Cordua-von Specht on 12/23/2016.
 *
 * Utility to help with setting the world border for an individual player.
 */
public class WorldBorderUtil {

    /**
     * This will reset the player world border back to the players current world border properties.
     * @param player The player to be reset back to normal.
     */
    public static void resetWorldBorder(Player player){
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.WorldBorderUtil.resetWorldBorder(player);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.WorldBorderUtil.resetWorldBorder(player);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.WorldBorderUtil.resetWorldBorder(player);
                break;
        }
    }

    /**
     * This will set the players world border to the specified parameters.
     * @param player The player the worldborder is being set for.
     * @param origin The new origin in which the world border will take.
     * @param radius The new radius of the world border.
     */
    public static void setWorldBorder(Player player, Location origin, double radius){
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.WorldBorderUtil.setWorldBorder(player, origin, radius);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.WorldBorderUtil.setWorldBorder(player, origin, radius);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.WorldBorderUtil.setWorldBorder(player, origin, radius);
                break;
        }
    }
}
