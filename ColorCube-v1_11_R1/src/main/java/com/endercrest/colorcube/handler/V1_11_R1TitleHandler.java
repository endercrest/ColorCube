package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.utils.NMSUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class V1_11_R1TitleHandler implements TitleHandler {

    public void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut) {
        player.sendTitle(msg, "", fadeIn, stay, fadeOut);
    }

    public void sendTitle(String msg, Player player) {
        player.sendTitle(msg, "", 10, 70, 20);
    }

    public void resetTitle(Player player) {
        player.resetTitle();
    }

    public void clearTitle(Player player) {
        player.resetTitle();
    }

    public void sendSubtitle(String msg, Player player, int fadeIn, int stay, int fadeOut) {
        player.sendTitle("", msg, fadeIn, stay, fadeOut);
    }

    public void sendSubtitle(String msg, Player player) {
        player.sendTitle("", msg, 10, 70, 20);
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

    public void sendActionbar(String msg, Player player) {
        sendActionbar(msg, player, 10, 70, 20);
    }
}
