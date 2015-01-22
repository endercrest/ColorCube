package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.*;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.logging.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Reload implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }
        if(args.length != 1){
            MessageManager.getInstance().sendMessage("Valid reload types <Settings | Games |All>", p);
            MessageManager.getInstance().sendMessage("Settings will reload the settings configs and attempt to reapply them", p);
            MessageManager.getInstance().sendMessage("Games will reload all games currently running", p);
            MessageManager.getInstance().sendMessage("All will attempt to reload the entire plugin", p);
        }else{
            if(args[0].equalsIgnoreCase("settings")){
                SettingsManager.getInstance().reloadSystem();
                SettingsManager.getInstance().reloadMessages();
                SettingsManager.getInstance().reloadConfig();
                for(Game game: GameManager.getInstance().getGames()){
                    game.reloadConfig();
                }
                MessageManager.getInstance().sendMessage("Settings Reloaded", p);
            }else if(args[0].equalsIgnoreCase("games")){
                for(Game game: GameManager.getInstance().getGames()){
                    QueueManager.getInstance().rollback(game.getGameID(), true);
                    game.disable();
                    game.enable();
                }
                MessageManager.getInstance().sendMessage("Games Reloaded", p);
            }else if(args[0].equalsIgnoreCase("all")){
                final Plugin pinstance = GameManager.getInstance().getPlugin();
                Bukkit.getPluginManager().disablePlugin(pinstance);
                Bukkit.getPluginManager().enablePlugin(pinstance);
                MessageManager.getInstance().sendMessage("Plugin reloaded", p);
            }
        }
        return false;
    }

    @Override
    public String helpInfo() {
        return "/cc reload - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.reload");
    }

    @Override
    public String permission() {
        return "cc.admin.reload";
    }
}
