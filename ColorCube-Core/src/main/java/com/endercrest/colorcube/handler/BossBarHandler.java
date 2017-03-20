package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.handler.bossbar.BossBar;

/**
 * Created by Thomas Cordua-von Specht on 3/10/2017.
 *
 * BossBar Handler is in charge of creating new boss bar objects with the correct api's.
 */
public interface BossBarHandler {

    /**
     * Creates a boss bar instance to display to players. The progress
     * defaults to 1.0
     *
     * @param title the title of the boss bar
     * @param color the color of the boss bar
     * @param style the style of the boss bar
     * @param flags an optional list of flags to set on the boss bar
     * @return the created boss bar
     */
    BossBar createBossBar(String title, BossBar.BarColor color, BossBar.BarStyle style, BossBar.BarFlag... flags);


    class NullBossBarHandler implements BossBarHandler{

        @Override
        public BossBar createBossBar(String title, BossBar.BarColor color, BossBar.BarStyle style, BossBar.BarFlag... flags) {
            MessageManager.getInstance().debugConsole("&cWarning: &rHandler not correctly initiated.");
            return null;
        }
    }
}
