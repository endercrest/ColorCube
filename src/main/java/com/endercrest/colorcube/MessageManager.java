package com.endercrest.colorcube;

import com.endercrest.colorcube.utils.MessageUtil;
import com.endercrest.colorcube.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class MessageManager {

    public static MessageManager instance = new MessageManager();

    private ColorCube plugin = null;
    private String prefix = "&f[&6ColorCube&f]";

    public static MessageManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;

        debugConsole("&eMessage Manager Set up");
    }

    /**
     * Colorize the string using MineCraft coloring.
     * @param str The string to have codes changed
     * @return Colorized string
     */
    public String colorize(String str){
        return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
    }

    /**
     * Logs a message to the console.
     * @param obj The obj to be printed.
     */
    public void log(Object obj){
        if(plugin.getConfig().getBoolean("color-logs", true)){
            plugin.getServer().getConsoleSender().sendMessage(colorize("&f[&6" + plugin.getName() + "&f] &r" + obj));
        }else{
            Bukkit.getLogger().log(Level.INFO, "[" + plugin.getName() + "] " + (colorize((String) obj)).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }

    /**
     * Send Message to a single player
     * @param msg The message
     * @param p The player
     */
    public void sendMessage(String msg, Player p){
        p.sendMessage(colorize(prefix + " " + msg));
    }

    public void sendTitle(String msg, Player player){
        msg = MessageManager.getInstance().colorize(msg);

        Title title = new Title("");
        title.setTitle(msg);

        title.send(player);
    }

    public void sendSubTitle(String msg, Player player){
        msg = MessageManager.getInstance().colorize(msg);

        Title title = new Title("");
        title.setSubtitle(msg);

        title.send(player);
    }

    /**
     * Broadcast a server wide message
     * @param msg The message
     */
    public void broadcastMessage(String msg){
        Bukkit.broadcastMessage(colorize(prefix + " " + msg));
    }

    /**
     * Broadcast a server wide message with a pre-loaded message
     * @param path
     */
    public void broadcastFMessage(String path, String ...args){
        String msg = prefix + " " + SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error] Could not load message! Please contact a Administrator");
        boolean enabled = SettingsManager.getInstance().getMessagesConfig().getBoolean("messages."+path+"_enabled", true);
        if(!enabled)
            return;
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        Bukkit.broadcastMessage(colorize(msg));
    }

    /**
     * Send a message to a player
     * @param path The path of the message
     * @param p The player
     */
    public void sendFMessage(String path, Player p, String ...args){
        String msg = prefix + " " + SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error] Could not load message! Please contact a Administrator");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        p.sendMessage(colorize(msg));
    }

    public void sendFTitle(String path, Player player, String ...args){
        String msg = SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error]");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        msg = MessageManager.getInstance().colorize(msg);

        Title title = new Title("");
        title.setTitle(msg);

        title.send(player);
    }

    public void sendFSubTitle(String path, Player player, String ...args){
        String msg = SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error]");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        msg = MessageManager.getInstance().colorize(msg);

        Title title = new Title("");
        title.setSubtitle(msg);

        title.send(player);
    }

    public String getFValue(String path, String ...args){
        String msg = SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error]");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        return MessageManager.getInstance().colorize(msg);
    }

    public void debug(String msg, Player p){
        if(plugin.getConfig().getBoolean("debug", false)) {
            sendMessage("[Debug]" +msg, p);
        }
    }

    public void debugConsole(String msg){
        if(plugin.getConfig().getBoolean("debug", false)) {
            log("[Debug]"+msg);
        }
    }
}
