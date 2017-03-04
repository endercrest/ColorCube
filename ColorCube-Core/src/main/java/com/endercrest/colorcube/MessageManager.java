package com.endercrest.colorcube;

import com.endercrest.colorcube.handler.HandlerManager;
import com.endercrest.colorcube.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Created by Thomas Cordua-von Specht on 12/18/2016.
 *
 * This class is the handler for sending any messages that might be needed to be sent to players. This includes chat messages,
 * titles, and subtitles as well as debugging. This class also handles retrieving messages from the messages.yml file.
 */
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
     * Sets the prefix after the loading of everything.
     * @param prefix The prefix that will be attached to all messages.
     */
    protected void setPrefix(String prefix){
        this.prefix = prefix;
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
        if(plugin != null && plugin.getConfig().getBoolean("color-logs", true)){
            plugin.getServer().getConsoleSender().sendMessage(colorize("&f[&6" + plugin.getName() + "&f] &r" + obj));
        }else{
            if(Bukkit.getServer() != null)
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

    /**
     * Send a title message to the player.
     * @param msg The message to be sent.
     * @param player The player that is receiving the message.
     */
    public void sendTitle(String msg, Player player){
        sendTitle(msg, player, 10, 70, 20);
    }

    /**
     * Send a title message to the player.
     * @param msg The message to be sent
     * @param player The player that is receiving the message.
     * @param fadeIn The fade in time.
     * @param stay The display time.
     * @param fadeOut The fade out time.
     */
    public void sendTitle(String msg, Player player, int fadeIn, int stay, int fadeOut){
        msg = MessageManager.getInstance().colorize(msg);

        HandlerManager.getInstance().getTitleHandler().sendTitle(msg, player, fadeIn, stay, fadeOut);
    }

    /**
     * Send a subtitle message to the player.
     * @param msg The message to be sent.
     * @param player The player that is receiving the message.
     */
    public void sendSubTitle(String msg, Player player){
        sendSubTitle(msg, player, 10, 70, 20);
    }

    /**
     * Send a subtitle message to the player.
     * @param msg The message to be sent.
     * @param player The player that is receiving the message.
     * @param fadeIn The fade in time.
     * @param stay The display time.
     * @param fadeOut The fade out time.
     */
    public void sendSubTitle(String msg, Player player, int fadeIn, int stay, int fadeOut){
        msg = MessageManager.getInstance().colorize(msg);


        HandlerManager.getInstance().getTitleHandler().sendSubtitle(msg, player, fadeIn, stay, fadeOut);
    }

    /**
     * Broadcast a server wide message
     * @param msg The message
     */
    public void broadcastMessage(String msg){
        Bukkit.broadcastMessage(colorize(prefix + " " + msg));
    }

    /**
     * Broadcast a server wide message with a pre-loaded message.
     * @param path The path of the message in messages.yml.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
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
     * Send a message to a player from the messages.yml, if missing error message is sent..
     * @param path The path of the message in messages.yml.
     * @param p The player to recieve the message.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
     */
    public void sendFMessage(String path, Player p, String ...args){
        String msg = prefix + " " + SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error] Could not load message! Please contact a Administrator");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        p.sendMessage(colorize(msg));
    }

    /**
     * Send a title message to a player by retrieving the message from the message config.
     * @param path The path to the message in the messages.yml.
     * @param player The player to be receiving the message.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
     */
    public void sendFTitle(String path, Player player, String ...args){
        sendFTitle(path, player, 10, 70, 20, args);
    }

    /**
     * Send a title message to a player by retrieving the message from the message config.
     * @param path The path to the message in the messages.yml.
     * @param player The player to be receiving the message.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
     * @param fadeIn The fade in time.
     * @param stay The display time.
     * @param fadeOut The fade out time.
     */
    public void sendFTitle(String path, Player player, int fadeIn, int stay, int fadeOut, String ...args){
        String msg = SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error]");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        sendTitle(msg, player, 10, 70, 20);
    }

    /**
     * Send a subtitle message to a player by retrieving the message from the message config.
     * @param path The path to the message in the messages.yml.
     * @param player The player to be receiving the message.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
     */
    public void sendFSubTitle(String path, Player player, String ...args){
        sendFSubTitle(path, player, 10, 70, 20, args);
    }

    /**
     * Send a subtitle message to a player by retrieving the message from the message config.
     * @param path The path to the message in the messages.yml.
     * @param player The player to be receiving the message.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
     * @param fadeIn The fade in time.
     * @param stay The display time.
     * @param fadeOut The fade out time.
     */
    public void sendFSubTitle(String path, Player player, int fadeIn, int stay, int fadeOut, String ...args){
        String msg = SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error]");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);

        sendSubTitle(msg, player, fadeIn, stay, fadeOut);
    }

    /**
     * Get the message value from messages.yml.
     * @param path The path to the message in the messages.yml.
     * @param args The arguments to be replaced in the message from the messages.yml. Each argument must follow the format of
     *             [id]-[value] where the id is the id of the variable in the message and the value is the new value to replace
     *             id.
     * @return The message with all arguments replaced in it.
     */
    public String getFValue(String path, String ...args){
        String msg = SettingsManager.getInstance().getMessagesConfig().getString("messages." + path, "&c[Error]");
        if(args != null && args.length != 0)
            msg = MessageUtil.replaceVars(msg, args);
        return MessageManager.getInstance().colorize(msg);
    }

    /**
     * Send a debug message to the player if debug is set to true in the configuration file.
     * @param msg The message to be sent to the player.
     * @param p The player to be receiving the message.
     */
    public void debug(String msg, Player p){
        if(plugin != null && plugin.getConfig().getBoolean("debug", false)) {
            sendMessage("[Debug]" +msg, p);
        }
    }

    /**
     * Send a debug message to the console if debug is set to true in the configuration file.
     * @param msg The message to be sent to the player.
     */
    public void debugConsole(String msg){
        if(plugin != null && plugin.getConfig().getBoolean("debug", false)) {
            log("[Debug]"+msg);
        }
    }

    /**
     * Send a debug message to the console if debug is set to true in the configuration file.
     * @param msg The message with '%s' in them to be replaced by the args.
     * @param args The arguments to replace any %s in them.
     */
    public void debugConsole(String msg, Object... args){
        debugConsole(String.format(msg, args));
    }
}
