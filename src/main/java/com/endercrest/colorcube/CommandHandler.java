package com.endercrest.colorcube;

import com.endercrest.colorcube.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class CommandHandler implements CommandExecutor {

    private ColorCube plugin;
    private HashMap<String, SubCommand> commands;
    private HashMap < String, Integer > helpinfo;
    private MessageManager msg = MessageManager.getInstance();

    public CommandHandler(ColorCube plugin){
        this.plugin = plugin;
        commands = new HashMap<String, SubCommand>();
        helpinfo = new HashMap<String, Integer>();
        loadCommands();
        loadHelpInfo();
    }

    public void loadCommands(){
        commands.put("createarena", new CreateArena());
        commands.put("deletearena", new DeleteArena());
        commands.put("disable", new Disable());
        commands.put("enable", new Enable());
        commands.put("createlobby", new CreateLobby());
        commands.put("join", new Join());
        commands.put("reload", new Reload());
        commands.put("leave", new Leave());
        commands.put("setspawn", new SetSpawn());
        commands.put("setlobbyspawn", new SetLobbySpawn());
        commands.put("setgloballobbyspawn", new SetGlobalLobbySpawn());
        commands.put("forcestart", new ForceStart());
        commands.put("resetspawns", new ResetSpawns());
        commands.put("arenalist", new ListArenas());
        commands.put("vote", new Vote());
        commands.put("spectate", new Spectate());
        commands.put("createsign", new CreateSign());
        commands.put("deletesign", new DeleteSign());

        commands.put("debug", new Debug(plugin));
    }

    private void loadHelpInfo(){
        helpinfo.put("createarena", 3);
        helpinfo.put("deletearena", 3);
        helpinfo.put("disable", 2);
        helpinfo.put("enable", 2);
        helpinfo.put("createlobby", 3);
        helpinfo.put("join", 1);
        helpinfo.put("reload", 3);
        helpinfo.put("leave", 1);
        helpinfo.put("setspawn", 3);
        helpinfo.put("setlobbyspawn", 3);
        helpinfo.put("setgloballobbyspawn", 3);
        helpinfo.put("forcestart", 2);
        helpinfo.put("arenalist", 1);
        helpinfo.put("vote", 1);
        helpinfo.put("spectate", 1);
        helpinfo.put("createsign", 3);
        helpinfo.put("deletesign", 3);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if(!(cs instanceof Player)){
            cs.sendMessage(MessageManager.getInstance().colorize("&cOnly players can use these commands"));
            return false;
        }
        Player player = (Player)cs;
        if(cmd.getName().equalsIgnoreCase("colorcube")){
            if(args == null || args.length < 1){
                msg.sendMessage("&6ColorCube version " + plugin.getDescription().getVersion() + " by tcvs", player);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    help(player, 1);
                }
                else {
                    if (args[1].toLowerCase().startsWith("player")) {
                        help(player, 1);
                        return true;
                    }
                    if (args[1].toLowerCase().startsWith("staff")) {
                        help(player, 2);
                        return true;
                    }
                    if (args[1].toLowerCase().startsWith("admin")) {
                        help(player, 3);
                        return true;
                    }
                    else {
                        MessageManager.getInstance().sendMessage("&c" + args[1] + " is not a valid page! Valid pages are Player, Staff, and Admin.", player);
                    }
                }
                return true;
            }
            String sub = args[0].toLowerCase();
            Vector< String > l = new Vector < String > ();
            l.addAll(Arrays.asList(args));
            l.remove(0);
            args = (String[]) l.toArray(new String[0]);
            if (!commands.containsKey(sub)) {
                msg.sendMessage("&cCommand doesn't exist.", player);
                msg.sendMessage("&cType /cc help for command information", player);
                return true;
            }
            try {
                commands.get(sub).onCommand(player, args);
            } catch (Exception e) {
                e.printStackTrace();
                msg.sendFMessage("error.command", player, "command-["+sub+"] "+Arrays.toString(args));
                msg.sendMessage("&cType /cc help for command information", player);
            }
        }
        return false;
    }

    public void help (Player p, int page) {
        if (page == 1) {
            p.sendMessage("--- " + ChatColor.GOLD + " Player Commands" + ChatColor.WHITE + " ---");
        }
        if (page == 2) {
            p.sendMessage("--- " + ChatColor.GOLD + " Staff Commands" + ChatColor.WHITE + " ---");
        }
        if (page == 3) {
            p.sendMessage("--- " + ChatColor.GOLD + " Admin Commands" + ChatColor.WHITE + " ---");
        }

        for (String command : commands.keySet()) {
            try{
                if (helpinfo.get(command) == page) {
                    MessageManager.getInstance().sendMessage(commands.get(command).helpInfo(), p);
                }
            }catch(Exception e){}
        }
    }

    public HashMap<String, SubCommand> getCommands(){
        return commands;
    }
}
