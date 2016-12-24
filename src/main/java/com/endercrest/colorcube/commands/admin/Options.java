package com.endercrest.colorcube.commands.admin;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.commands.SubCommand;
import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Thomas Cordua-von Specht on 12/17/2016.
 *
 * Options command class that handles the permissions, helpinfo and onCommand execution.
 * The options command is meant to set specific options for each arenas.
 */
public class Options implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            MessageManager.getInstance().sendFMessage("error.nopermission", p);
            return true;
        }

        if(args.length < 3){
            MessageManager.getInstance().sendFMessage("info.optionsusage", p);
            if(GameManager.getInstance().getGameCount() > 0) {
                String methodsString = "";
                HashMap<String, Method> methods = setupMethods(GameManager.getInstance().getGames().get(0));
                for (String method : methods.keySet()) {
                    if (methodsString.equals("")) {
                        methodsString += method;
                    } else {
                        methodsString += ", " + method;
                    }
                }
                MessageManager.getInstance().sendMessage("&6Possible options: " + methodsString, p);
            }
        }else{
            try{
                int id = Integer.parseInt(args[0]);
                Game game = GameManager.getInstance().getGame(id);
                if(game == null){
                    MessageManager.getInstance().sendFMessage("error.nosuchgame", p, "arena-Arena " + id);
                    return true;
                }

                HashMap<String, Method> methods = setupMethods(game);

                String s = "";
                for(int i = 2; i < args.length; i++){
                    if(i != 2){
                        s += " ";
                    }
                    s += args[i];
                }

                Method method = methods.get(args[1].toLowerCase());

                if(method != null) {
                    try {
                        Type type = method.getParameterTypes()[0];
                        if (type == String.class) {
                            method.invoke(game, s);
                        } else if (type == int.class) {
                            method.invoke(game, Integer.parseInt(s));
                        } else if (type == double.class) {
                            method.invoke(game, Double.parseDouble(s));
                        }else if(type == boolean.class){
                            method.invoke(game, Boolean.parseBoolean(s));
                        }else{
                            method.invoke(game, s);
                        }
                        MessageManager.getInstance().sendFMessage("info.optionset", p, "option-" + args[1], "arena-" + id);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }else{
                    MessageManager.getInstance().sendFMessage("error.notoption", p, "option-"+args[1]);
                }
            }catch(NumberFormatException e){
                MessageManager.getInstance().sendFMessage("error.notanumber", p, "input-" + args[1]);
            }
        }
        return false;
    }

    private HashMap<String, Method> setupMethods(Game game){
        HashMap<String, Method> methods = new HashMap<>();
        try {
            methods.put("perteam", game.getClass().getMethod("setPerTeam", int.class));
            methods.put("reward", game.getClass().getMethod("setReward", double.class));
            methods.put("pvp", game.getClass().getMethod("setPvp", boolean.class));
            methods.put("name", game.getClass().getMethod("setName", String.class));
            methods.put("border", game.getClass().getMethod("setBorder", boolean.class));
            methods.put("border-extension", game.getClass().getMethod("setBorderExtension", double.class));
            methods.put("border-spectator-only", game.getClass().getMethod("setBorderSpectatorOnly", boolean.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return methods;
    }

    @Override
    public String helpInfo() {
        return "/cc options <id> <option> <value> - " + SettingsManager.getInstance().getMessagesConfig().getString("messages.help.options");
    }

    @Override
    public String permission() {
        return "cc.arena.options";
    }
}
