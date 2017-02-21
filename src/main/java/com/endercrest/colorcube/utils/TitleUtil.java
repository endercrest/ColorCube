package com.endercrest.colorcube.utils;

import org.bukkit.entity.Player;

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
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.TitleUtil.sendTitle(msg, player, fadeIn, stay, fadeOut, reset);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.TitleUtil.sendTitle(msg, player, fadeIn, stay, fadeOut, reset);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.TitleUtil.sendTitle(msg, player, fadeIn, stay, fadeOut, reset);
                break;
        }
    }

    /**
     * Sends a title to the player. This will reset any already sent titles.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    public static void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut){
        sendTitle(msg, player, fadeIn, stay, fadeOut, true);
    }

    /**
     * Reset the timing which should clear the message and any preset timings.
     * @param player The player the title should be reset for.
     */
    public static void resetTitle(Player player){
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.TitleUtil.resetTitle(player);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.TitleUtil.resetTitle(player);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.TitleUtil.resetTitle(player);
                break;
        }
    }

    /**
     * Clears the title from the player.
     * @param player The player.
     */
    public static void clearTitle(Player player){
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.TitleUtil.clearTitle(player);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.TitleUtil.clearTitle(player);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.TitleUtil.clearTitle(player);
                break;
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
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.TitleUtil.sendSubtitle(msg, player, fadeIn, stay, fadeOut, reset);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.TitleUtil.sendSubtitle(msg, player, fadeIn, stay, fadeOut, reset);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.TitleUtil.sendSubtitle(msg, player, fadeIn, stay, fadeOut, reset);
                break;
        }
    }

    /**
     * Sends a subtitle to the player. This will reset any already sent titles.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    public static void sendSubtitle(String msg, Player player, int fadeIn, int stay, int fadeOut){
        sendSubtitle(msg, player, fadeIn, stay, fadeOut, true);
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
        switch(NMSUtil.SupportVersion.getServerVersion()){
            case v1_9_R1:
                com.endercrest.colorcube.utils.versions.v1_9_R1.TitleUtil.sendActionbar(msg, player, fadeIn, stay, fadeOut, reset);
                break;
            case v1_10_R1:
                com.endercrest.colorcube.utils.versions.v1_10_R1.TitleUtil.sendActionbar(msg, player, fadeIn, stay, fadeOut, reset);
                break;
            case v1_11_R1:
            case UNSUPPORTED:
                com.endercrest.colorcube.utils.versions.v1_11_R1.TitleUtil.sendActionbar(msg, player, fadeIn, stay, fadeOut, reset);
                break;
        }
    }

    /**
     * Sends a actionbar to the player. This will reset any already sent titles.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    public static void sendActionbar(String msg, Player player, int fadeIn, int stay, int fadeOut){
        sendActionbar(msg, player, fadeIn, stay, fadeOut, true);
    }
}
