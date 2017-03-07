package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.MessageManager;
import org.bukkit.Bukkit;

public class HandlerManager {

    private static HandlerManager instance = new HandlerManager();

    public static HandlerManager getInstance(){
        return instance;
    }

    private TitleHandler titleHandler;
    private WorldBorderHandler worldBorderHandler;

    private Class<? extends BossBar> bossBarClass;

    public void setup(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String titleClass;
        String worldBorderClass;
        String bossBarClassPath;
        if(version.equals("v1_11_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.11 Handlers");
            titleClass = "com.endercrest.colorcube.handler.MC1_11TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_11WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.MC1_11BossBar";
        }else if(version.equals("v1_10_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.10 Handlers");
            titleClass = "com.endercrest.colorcube.handler.MC1_10TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_10WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.MC1_10BossBar";
        }else if(version.equals("v1_9_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.9 Handlers");
            titleClass = "com.endercrest.colorcube.handler.MC1_9TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_9WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.MC1_9BossBar";
        }else{
            MessageManager.getInstance().debugConsole("Defaulting to 1.11 Handlers");
            titleClass = "com.endercrest.colorcube.handler.MC1_11TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_11WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.MC1_11BossBar";
        }

        try {
            titleHandler = (TitleHandler)Class.forName(titleClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            MessageManager.getInstance().log("&cWarning: &rFailed to load title handler. Setting null handler.");
            titleHandler = new TitleHandler.NullTitleHandler();
        }

        try {
            worldBorderHandler = (WorldBorderHandler)Class.forName(worldBorderClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            MessageManager.getInstance().log("&cWarning: &rFailed to load title handler. Setting null handler.");
            worldBorderHandler = new WorldBorderHandler.NullWorldBorderHandler();
        }

        try{
            bossBarClass = (Class<? extends BossBar>) Class.forName(bossBarClassPath);
        } catch (ClassNotFoundException e){
            MessageManager.getInstance().log("&cWarning: &rFailed to load title handler. Setting null handler.");
            bossBarClass = BossBar.NullBossBar.class;
        }
        MessageManager.getInstance().debugConsole("&eHandler Manager Set up");
    }

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
    public BossBar createBossBar(String title, BossBar.BarColor color, BossBar.BarStyle style, BossBar.BarFlag... flags){
        BossBar bar = null;
        try {
            bar = bossBarClass.newInstance();
            bar.setTitle(title);
            bar.setColor(color);
            bar.setStyle(style);
            for(BossBar.BarFlag flag: flags)
                bar.addFlag(flag);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return bar;
    }

    /**
     * Get the world border handler that is in charge of setting and resetting the world border to individual clients.
     * @return The world border handler associated with the server version (or defaults to 1.11)
     */
    public WorldBorderHandler getWorldBorderHandler(){
        return worldBorderHandler;
    }

    /**
     * Get the title handler that is in charge of sending title, subtitle, and actionbar messages.
     * @return The title handler associated with the server version (or defaults to 1.11)
     */
    public TitleHandler getTitleHandler(){
        return titleHandler;
    }
}
