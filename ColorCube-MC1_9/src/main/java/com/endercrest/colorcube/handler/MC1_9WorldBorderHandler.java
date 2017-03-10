package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.utils.NMSUtil;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MC1_9WorldBorderHandler implements WorldBorderHandler {

    @Override
    public void resetWorldBorder(Player player) {
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

    @Override
    public void setWorldBorder(Player player, Location origin, double radius) {
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
