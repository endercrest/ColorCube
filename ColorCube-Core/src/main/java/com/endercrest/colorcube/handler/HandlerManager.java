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
    private BossBarHandler bossBarHandler;

    public void setup(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String titleClass, worldBorderClass, bossBarClassPath, particleClass;
        if(version.equals("v1_11_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.11 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_11_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_11_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_11_R1BossBarHandler";
            particleClass = "com.endercrest.colorcube.handler.V1_11_R1ParticleHandler";
        }else if(version.equals("v1_10_R1")){
            MessageManager.getInstance().debugConsole("Loading 1.10 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_10_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_10_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_10_R1BossBarHandler";
            particleClass = "com.endercrest.colorcube.handler.V1_10_R1ParticleHandler";
        }else if(version.equals("v1_9_R2")){
            MessageManager.getInstance().debugConsole("Loading 1.9 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_9_R2TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_9_R2WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_9_R2BossBarHandler";
            particleClass = "com.endercrest.colorcube.handler.V1_9_R2ParticleHandler";
        }else if(version.equals("v1_8_R3")){
            MessageManager.getInstance().debugConsole("Loading 1.8 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_8_R3TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_8_R3WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_8_R3BossBarHandler";
            particleClass = "";
        }else{
            MessageManager.getInstance().debugConsole("Defaulting to 1.11 Handlers");
            titleClass = "com.endercrest.colorcube.handler.V1_11_R1TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.V1_11_R1WorldBorderHandler";
            bossBarClassPath = "com.endercrest.colorcube.handler.V1_11_R1BossBarHandler";
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
            MessageManager.getInstance().log("&cWarning: &rFailed to load WorldBorder handler. Setting null handler.");
            worldBorderHandler = new WorldBorderHandler.NullWorldBorderHandler();
        }

        try {
            bossBarHandler = (BossBarHandler) Class.forName(bossBarClassPath).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            MessageManager.getInstance().log("&cWarning: &rFailed to load BossBar handler. Setting null handler.");
            bossBarHandler = new BossBarHandler.NullBossBarHandler();
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
     * Get the boss bar handler that is in charge of creating bossbars.
     * @return The boss bar handler associated with the server version (or defaults to 1.11)
     */
    public BossBarHandler getBossBarHandler(){
        return bossBarHandler;
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
