package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.utils.NMSUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class V1_9_R1TitleHandler implements TitleHandler {
    @Override
    public void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut) {
        try {
            sendTiming(player, fadeIn, stay, fadeOut);

            Class<?> chatBaseComponent = NMSUtil.getNmsClass("IChatBaseComponent");
            Class<?> chatSerializer = NMSUtil.getNmsClass("IChatBaseComponent$ChatSerializer");
            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants();

            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction"), chatBaseComponent);

            Object serializedTitle = chatSerializer.getMethod("a", String.class)
                    .invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', msg) + "\",\"color\":\"" + ChatColor.WHITE.name().toLowerCase() + "\"}");

            Object packet = packetTitleConstructor.newInstance(enums[0], serializedTitle);
            NMSUtil.sendPacket(player, packet);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTitle(String msg, Player player) {
        sendTitle(msg, player, 10, 70, 20);
    }

    @Override
    public void resetTitle(Player player) {
        try {
            Class<?> chatBaseComponent = NMSUtil.getNmsClass("IChatBaseComponent");
            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants();

            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction"), chatBaseComponent);

            Object packet = packetTitleConstructor.newInstance(enums[4], null);
            NMSUtil.sendPacket(player, packet);
        }catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    @Override
    public void clearTitle(Player player) {
        try {
            Class<?> chatBaseComponenet = NMSUtil.getNmsClass("IChatBaseComponent");
            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants();

            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction"), chatBaseComponenet);

            Object packet = packetTitleConstructor.newInstance(enums[3], null);
            NMSUtil.sendPacket(player, packet);
        }catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendSubtitle(String msg, Player player, int fadeIn, int stay, int fadeOut) {
        try {
            sendTiming(player, fadeIn, stay, fadeOut);

            Class<?> chatBaseComponent = NMSUtil.getNmsClass("IChatBaseComponent");
            Class<?> chatSerializer = NMSUtil.getNmsClass("IChatBaseComponent$ChatSerializer");
            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants();

            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction"), chatBaseComponent);

            Object serializedTitle = chatSerializer.getMethod("a", String.class)
                    .invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', msg) + "\",\"color\":\"" + ChatColor.WHITE.name().toLowerCase() + "\"}");

            Object packet = packetTitleConstructor.newInstance(enums[1], serializedTitle);
            NMSUtil.sendPacket(player, packet);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendSubtitle(String msg, Player player) {
        sendSubtitle(msg, player, 10, 70, 20);
    }

    @Override
    public void sendActionbar(String msg, Player player, int fadeIn, int stay, int fadeOut) {
        try {
            sendTiming(player, fadeIn, stay, fadeOut);

            Class<?> chatBaseComponent = NMSUtil.getNmsClass("IChatBaseComponent");
            Class<?> chatSerializer = NMSUtil.getNmsClass("IChatBaseComponent$ChatSerializer");
            Object[] enums = NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants();

            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NMSUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction"), chatBaseComponent);

            Object serializedTitle = chatSerializer.getMethod("a", String.class)
                    .invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', msg) + "\",\"color\":\"" + ChatColor.WHITE.name().toLowerCase() + "\"}");

            Object packet = packetTitleConstructor.newInstance(enums[2], serializedTitle);
            NMSUtil.sendPacket(player, packet);
        } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendActionbar(String msg, Player player) {
        sendActionbar(msg, player, 10, 70, 20);
    }

    private static void sendTiming(Player player, int fadeIn, int stay, int fadeOut){
        try {
            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(int.class, int.class, int.class);

            Object packet = packetTitleConstructor.newInstance(fadeIn, stay, fadeOut);
            NMSUtil.sendPacket(player, packet);
        }catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }
}
