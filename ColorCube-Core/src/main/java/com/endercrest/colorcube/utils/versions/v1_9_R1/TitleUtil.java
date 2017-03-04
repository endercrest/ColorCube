package com.endercrest.colorcube.utils.versions.v1_9_R1;

import com.endercrest.colorcube.utils.NMSUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Thomas Cordua-von Specht on 1/27/2017.
 *
 * Utility to use the title protocol.
 */
public class TitleUtil {

    /**
     * Sends a title to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    public static void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut, boolean reset){
        try {
            if(reset)
                resetTitle(player);
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

    /**
     * Send the timing to the client. This should be called before sending the display text so that it can
     * be rendered correctly.
     * @param player The player to have the timing set.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    private static void sendTiming(Player player, int fadeIn, int stay, int fadeOut){
        try {
            Constructor packetTitleConstructor = NMSUtil.getNmsClass("PacketPlayOutTitle").getConstructor(int.class, int.class, int.class);

            Object packet = packetTitleConstructor.newInstance(fadeIn, stay, fadeOut);
            NMSUtil.sendPacket(player, packet);
        }catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    /**
     * Reset the timing which should clear the message and any preset timings.
     * @param player The player the title should be reset for.
     */
    public static void resetTitle(Player player){
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

    /**
     * Clears the title from the player.
     * @param player The player.
     */
    public static void clearTitle(Player player){
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

    /**
     * Sends a subtitle to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    public static void sendSubtitle(String msg, Player player, int fadeIn, int stay, int fadeOut, boolean reset){
        try {
            if(reset)
                resetTitle(player);
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

    /**
     * Sends a action bar to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    public static void sendActionbar(String msg, Player player, int fadeIn, int stay, int fadeOut, boolean reset){
        try {
            if(reset)
                resetTitle(player);
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
}
