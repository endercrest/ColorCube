package com.endercrest.colorcube.events;

import com.endercrest.colorcube.MessageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreCommandListener implements Listener {

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event){
        MessageManager.getInstance().debugConsole(event.getMessage());
    }
}
