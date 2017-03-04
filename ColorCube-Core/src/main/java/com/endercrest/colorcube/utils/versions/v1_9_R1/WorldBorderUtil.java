package com.endercrest.colorcube.utils.versions.v1_9_R1;

import com.endercrest.colorcube.utils.NMSUtil;
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
        try {
            WorldBorder originalWorldBorder = player.getWorld().getWorldBorder();

            Constructor packetWorldBorderConstructor = NMSUtil.getNmsClass("PacketPlayOutWorldBorder").getConstructor(NMSUtil.getNmsClass("WorldBorder"), NMSUtil.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction"));

            Object worldBorder = NMSUtil.getNmsClass("WorldBorder").getConstructor().newInstance();
            worldBorder.getClass().getMethod("setCenter", double.class, double.class).invoke(worldBorder, originalWorldBorder.getCenter().getX(), originalWorldBorder.getCenter().getZ());
            worldBorder.getClass().getMethod("setSize", double.class).invoke(worldBorder, originalWorldBorder.getSize());
            worldBorder.getClass().getMethod("setDamageBuffer", double.class).invoke(worldBorder, originalWorldBorder.getDamageBuffer());
            worldBorder.getClass().getMethod("setDamageAmount", double.class).invoke(worldBorder, originalWorldBorder.getDamageAmount());
            worldBorder.getClass().getMethod("setWarningDistance", int.class).invoke(worldBorder, originalWorldBorder.getWarningDistance());
            worldBorder.getClass().getMethod("setWarningTime", int.class).invoke(worldBorder, originalWorldBorder.getWarningTime());


            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction").getEnumConstants();

            Object packet = packetWorldBorderConstructor.newInstance(worldBorder, enums[3]);
            NMSUtil.sendPacket(player, packet);

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will set the players world border to the specified parameters.
     * @param player The player the worldborder is being set for.
     * @param origin The new origin in which the world border will take.
     * @param radius The new radius of the world border.
     */
    public static void setWorldBorder(Player player, Location origin, double radius){
        try {
            Constructor packetWorldBorderConstructor = NMSUtil.getNmsClass("PacketPlayOutWorldBorder").getConstructor(NMSUtil.getNmsClass("WorldBorder"), NMSUtil.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction"));

            Object worldBorder = NMSUtil.getNmsClass("WorldBorder").getConstructor().newInstance();
            worldBorder.getClass().getMethod("setCenter", double.class, double.class).invoke(worldBorder, origin.getX(), origin.getZ());
            worldBorder.getClass().getMethod("setSize", double.class).invoke(worldBorder, radius);
            worldBorder.getClass().getMethod("setDamageBuffer", double.class).invoke(worldBorder, 0);
            worldBorder.getClass().getMethod("setDamageAmount", double.class).invoke(worldBorder, 0);
            worldBorder.getClass().getMethod("setWarningDistance", int.class).invoke(worldBorder, 0);
            worldBorder.getClass().getMethod("setWarningTime", int.class).invoke(worldBorder, 0);

            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction").getEnumConstants();

            Object packet = packetWorldBorderConstructor.newInstance(worldBorder, enums[3]);
            NMSUtil.sendPacket(player, packet);

        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
