package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.MessageManager;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 3/3/2017.
 *
 * Handler for titles to be sent to clients.
 */
public interface TitleHandler {

    /**
     * Sends a title to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut);

    /**
     * Sends a title to the player, with default timings.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     */
    void sendTitle(String msg, Player player);

    /**
     * Reset the timing which should clear the message and any preset timings.
     * @param player The player the title should be reset for.
     */
    void resetTitle(Player player);

    /**
     * Clears the title from the player.
     * @param player The player.
     */
    void clearTitle(Player player);

    /**
     * Sends a subtitle to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    void sendSubtitle(String msg, Player player, int fadeIn, int stay, int fadeOut);

    /**
     * Sends a subtitle to the player, with default timing.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     *
     */
    void sendSubtitle(String msg, Player player);

    /**
     * Sends a action bar to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     * @param fadeIn The fadeIn time in ticks (20 ticks per second)
     * @param stay The stay time in ticks (20 ticks per second)
     * @param fadeOut The fadeOut time in ticks (20 ticks per second)
     */
    void sendActionbar(String msg, Player player, int fadeIn, int stay, int fadeOut);

    /**
     * Sends a action bar to the player.
     * @param msg The message to be displayed. (Supports ColorCodes)
     * @param player The player it is being sent to.
     */
    void sendActionbar(String msg, Player player);


    class NullTitleHandler implements TitleHandler{

        @Override
        public void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");

        }

        @Override
        public void sendTitle(String msg, Player player) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void resetTitle(Player player) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void clearTitle(Player player) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void sendSubtitle(String msg, Player player, int fadeIn, int stay, int fadeOut) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void sendSubtitle(String msg, Player player) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void sendActionbar(String msg, Player player, int fadeIn, int stay, int fadeOut) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }

        @Override
        public void sendActionbar(String msg, Player player) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
        }
    }
}