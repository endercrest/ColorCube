package com.endercrest.colorcube.events;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PreCommandListener implements Listener {

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event){
        boolean whitelisted = false;
        if(GameManager.getInstance().isPlayerActive(event.getPlayer())) {
            List<String> wCommand = SettingsManager.getInstance().getPluginConfig().getStringList("command-whitelist");
            wCommand.add("/cc");
            wCommand.add("/colorcube");
            wCommand.add("/colourcube");

            for (String cmd : wCommand) {
                boolean match = false;
                if(event.getMessage().startsWith(cmd)){
                    match = true;
                }

                if (match) {
                    whitelisted = true;
                    break;
                }
            }
            if(!whitelisted){
                MessageManager.getInstance().sendFMessage("error.nocommand", event.getPlayer());
                event.setCancelled(true);
            }
        }


    }
}
