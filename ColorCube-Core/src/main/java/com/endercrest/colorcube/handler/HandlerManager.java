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

    public void setup(){
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        String titleClass;
        String worldBorderClass;
        if(version.equals("v1_11_R1")){
            titleClass = "com.endercrest.colorcube.handler.MC1_11TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_11WorldBorderHandler";
        }else if(version.equals("v1_10_R1")){
            titleClass = "com.endercrest.colorcube.handler.MC1_10TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_10WorldBorderHandler";
        }else if(version.equals("v1_9_R1")){
            titleClass = "com.endercrest.colorcube.handler.MC1_9TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_9WorldBorderHandler";
        }else{
            titleClass = "com.endercrest.colorcube.handler.MC1_11TitleHandler";
            worldBorderClass = "com.endercrest.colorcube.handler.MC1_11WorldBorderHandler";
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
        MessageManager.getInstance().debugConsole("&eHandler Manager Set up");
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
