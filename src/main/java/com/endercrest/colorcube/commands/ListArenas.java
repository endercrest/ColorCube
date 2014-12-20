package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ListArenas implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        StringBuilder arenas = new StringBuilder();
        try{
            if(args.length == 0 || Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[0]) > GameManager.getInstance().getGameCount()){
                MessageManager.getInstance().sendFMessage("error.gamenoexist", p);
            }
            if (GameManager.getInstance().getGames().isEmpty()) {
                arenas.append(SettingsManager.getInstance().getMessagesConfig().getString("messages.words.noarenas", "No arenas")).append(": ");
                p.sendMessage(ChatColor.RED + arenas.toString());
                return true;
            }
            arenas.append(SettingsManager.getInstance().getMessagesConfig().getString("messages.words.noarenas", "Arenas")).append(": ");
            for (Game g : GameManager.getInstance().getGames()) {
                arenas.append(g.getGameID()).append(", ");
            }
            p.sendMessage(ChatColor.GREEN + arenas.toString());
        }catch(Exception e){
            MessageManager.getInstance().sendFMessage("error.gamenoexist", p);
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc arenalist - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.arenalist");
    }

    @Override
    public String permission() {
        return "";
    }
}
