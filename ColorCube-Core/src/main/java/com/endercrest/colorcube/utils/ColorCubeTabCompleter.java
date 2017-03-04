package com.endercrest.colorcube.utils;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.CommandHandler;
import com.endercrest.colorcube.commands.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ColorCubeTabCompleter implements TabCompleter {

    ColorCube plugin;

    public ColorCubeTabCompleter(ColorCube plugin){
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if(cmd.getName().equalsIgnoreCase("colorcube")){
            if(sender instanceof Player){
                List<String> list = new ArrayList<String>();
                Player player = (Player)sender;
                if(args.length == 1){
                    CommandHandler cmdH = new CommandHandler(plugin);
                    for(String string: cmdH.getCommands().keySet()){
                        SubCommand subCommand = cmdH.getCommands().get(string);
                        if(subCommand.permission() != null){
                            if(player.hasPermission(subCommand.permission())){
                                if(string.startsWith(args[0]) || args[0].isEmpty()) {
                                    list.add(string);
                                }
                            }
                        }else{
                            if(string.startsWith(args[0]) || args[0].isEmpty()) {
                                list.add(string);
                            }
                        }
                    }
                    return list;
                }
            }
        }
        return null;
    }
}
