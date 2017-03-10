package com.endercrest.colorcube.commands.player;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;

public class ListArenas implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        StringBuilder arenas = new StringBuilder();
        try{
            if(GameManager.getInstance().getGames().isEmpty()){
                arenas.append(SettingsManager.getInstance().getMessagesConfig().getString("messages.words.noarenas", "No arenas"));
                MessageManager.getInstance().sendMessage("&c" + arenas.toString(), p);
                return true;
            }
            arenas.append(SettingsManager.getInstance().getMessagesConfig().getString("messages.words.arenas", "Arenas")).append(": ");
            for(Game g: GameManager.getInstance().getGames()){
                arenas.append(g.getId()).append(", ");
            }
            MessageManager.getInstance().sendMessage("&6" + arenas.toString(), p);
        }catch (Exception e){
            MessageManager.getInstance().sendFMessage("error.gamenotexist", p);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc arenalist - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.arenalist");
    }

    @Override
    public String permission() {
        return null;
    }
}
