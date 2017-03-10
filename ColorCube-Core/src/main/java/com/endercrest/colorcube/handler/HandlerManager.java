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
    private ParticleHandler particleHandler;

    private Class<? extends BossBar> bossBarClass;

    public void setup(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String titleClass, worldBorderClass, bossBarClassPath, particleClass;
        if(version.equals("v1_11_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.11 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_11_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_11_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_11_R1BossBar";
            particleClass = "com.endercrest.colorcube.handler.V1_11_R1ParticleHandler";
        }else if(version.equals("v1_10_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.10 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_10_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_10_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_10_R1BossBar";
            particleClass = "com.endercrest.colorcube.handler.V1_10_R1ParticleHandler";
        }else if(version.equals("v1_9_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.9 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_9_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_9_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_9_R1BossBar";
            particleClass = "com.endercrest.colorcube.handler.V1_9_R1ParticleHandler";
        }else{
            MessageManager.getInstance().debugConsole("Defaulting to 1.11 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_11_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_11_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_11_R1BossBar";
            particleClass = "com.endercrest.colorcube.handler.V1_11_R1ParticleHandler";
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
            MessageManager.getInstance().log("&cWarning: &rFailed to load worldborder handler. Setting null handler.");
            worldBorderHandler = new WorldBorderHandler.NullWorldBorderHandler();
        }

        try{
            bossBarClass = (Class<? extends BossBar>) Class.forName(bossBarClassPath);
        } catch (ClassNotFoundException e){
            MessageManager.getInstance().log("&cWarning: &rFailed to load bossbar handler. Setting null handler.");
            bossBarClass = BossBar.NullBossBar.class;
        }

        try {
            particleHandler = (ParticleHandler) Class.forName(particleClass).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            MessageManager.getInstance().log("&cWarning: &rFailed to load particle handler. Setting null handler.");
            particleHandler = new ParticleHandler.NullParticleHandler();
        }
        MessageManager.getInstance().debugConsole("&eHandler Manager Setup Complete.");
    }

    /**
     * Creates a boss bar instance to display to players. The progress
     * defaults to 1.0. This is also a wrapper for the
     * Bukkit.createBossBar(...) that is available in 1.9 and above
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

    /**
     * Get the particle handler in charge of sending particles to clients.
     * @return The particle handler associated with the server version (or defaults to 1.11)
     */
    public ParticleHandler getParticleHandler(){
        return particleHandler;
    }
}
