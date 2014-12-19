package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.PowerupManager;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.game.Powerup;
import org.bukkit.entity.Player;

public class Debug implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(SettingsManager.getInstance().getPluginConfig().getBoolean("debug", false)) {
            if (!p.hasPermission(permission()) || !p.isOp()) {
                MessageManager.getInstance().sendFMessage("error.nopermission", p);
            }
            //cc debug end <id>
            if(args.length >= 1) {
                if(args[0].equalsIgnoreCase("end")) {
                    if (args.length == 1) {
                        try {
                            Game game = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameID(p));
                            game.endGame();
                        } catch (Exception e) {
                        }
                    } else if (args.length == 2) {
                        try {
                            Game game = GameManager.getInstance().getGame(Integer.parseInt(args[2]));
                            game.endGame();
                        } catch (Exception e) {}
                    }
                }else if(args[0].equalsIgnoreCase("powerup")){
                    if(args.length == 1) {
                        try{
                            Game game = GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameID(p));
                            Powerup pu = game.createPowerup(p.getLocation(), false);
                            for(int i = 0; i < 9; i++){
                                if(p.getInventory().getItem(i) == null){
                                    p.getInventory().setItem(i, pu.getType().getItem());
                                    break;
                                }
                            }
                        }catch(NullPointerException e){}
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/cc debug <subcommand> - Debugging for developers";
    }

    @Override
    public String permission() {
        return "cc.dev.debug";
    }
}
