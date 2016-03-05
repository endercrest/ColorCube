package com.endercrest.colorcube.commands;

import com.endercrest.colorcube.*;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.game.Powerup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * All Available Commands for Debugging
 * /cc debug end [id] - End the game you are in or the game your provide for id
 * /cc debug powerup - Gives a random powerup to a player
 * /cc debug forcestart [id] - Force a game to start no matter the amount of players
 * /cc debug forcejoin (player) (id) - Force a player to join a game in selected game
 * /cc debug forcevote (player) - Force a player to vote in the arena they are in
 * /cc debug forcevoteall (id) - Force all players in selected arena to vote
 * /cc debug forceremove (player) - Force remove a player from the arena
 * /cc debug forcespectate (player) (id) - Force a player to spectate a game
 * /cc debug openMenu - Opens menu select menu.
 *
 * () - Mandatory [] - Optional
 */
public class Debug implements SubCommand {

    private ColorCube plugin;

    public Debug(ColorCube plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(SettingsManager.getInstance().getPluginConfig().getBoolean("debug", false)) {
            if (!p.hasPermission(permission()) || !p.isOp()) {
                MessageManager.getInstance().sendFMessage("error.nopermission", p);
                return true;
            }
            if(args.length >= 1) {
                if(args[0].equalsIgnoreCase("end")) {
                    if (args.length == 1) {
                        try {
                            Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p));
                            game.endGame();
                        } catch (Exception e) {
                        }
                    } else if (args.length == 2) {
                        try {
                            Game game = GameManager.getInstance().getGame(Integer.parseInt(args[1]));
                            game.endGame();
                        } catch (Exception e) {}
                    }
                }else if(args[0].equalsIgnoreCase("powerup")){
                    if(args.length == 1) {
                        try{
                            Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p));
                            Powerup pu = game.createPowerup(p.getLocation(), false);
                            for(int i = 0; i < 9; i++){
                                if(p.getInventory().getItem(i) == null){
                                    p.getInventory().setItem(i, pu.getType().getItem());
                                    break;
                                }
                            }
                        }catch(NullPointerException e){}
                    }
                }else if(args[0].equalsIgnoreCase("forcestart")){
                    if(args.length == 1){
                        try{
                            if(GameManager.getInstance().isPlayerActive(p)){
                                Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p));
                                game.forceStartGame();
                                MessageManager.getInstance().sendMessage("Force Starting a Game", p);
                            }else if(GameManager.getInstance().isPlayerSpectator(p)){
                                Game game = GameManager.getInstance().getGame(GameManager.getInstance().getSpectatePlayerId(p));
                                game.forceStartGame();
                            }
                        }catch(NullPointerException e){}
                    }else if(args.length == 2){
                        try{
                            Game game = GameManager.getInstance().getGame(Integer.parseInt(args[1]));
                            game.forceStartGame();
                            MessageManager.getInstance().sendMessage("Force Starting a Game", p);
                        }catch (Exception e){}
                    }
                }else if(args[0].equalsIgnoreCase("forcejoin")){
                    if(args.length == 3){
                        try{
                            Player player = Bukkit.getPlayer(args[1]);
                            Game game = GameManager.getInstance().getGame(Integer.parseInt(args[2]));
                            game.addPlayer(player);
                            MessageManager.getInstance().sendMessage("Force adding " + args[1] + " to arena " + args[2], p);
                        }catch(Exception e){}
                    }
                }else if(args[0].equalsIgnoreCase("forcevote")){
                    if(args.length == 2){
                        try{
                            Player player = Bukkit.getPlayer(args[1]);
                            Game game = GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(p));
                            game.vote(player);
                            MessageManager.getInstance().sendMessage("Forcing " + args[1] + " to vote in arena " + game.getId(), p);
                        }catch (Exception e){}
                    }
                }else if(args[0].equalsIgnoreCase("forcevoteall")){
                    if(args.length == 2){
                        try {
                            Game game = GameManager.getInstance().getGame(Integer.parseInt(args[1]));
                            for(Player player: game.getActivePlayers()){
                                game.vote(player);
                            }
                            MessageManager.getInstance().sendMessage("Forcing all players in arena " + args[1] + " to vote", p);
                        }catch (Exception e){}
                    }
                }else if(args[0].equalsIgnoreCase("forceremove")){
                    if(args.length == 2){
                        try{
                            Player player = Bukkit.getPlayer(args[1]);
                            if(GameManager.getInstance().isPlayerActive(player)){
                                GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(player)).removePlayer(player, false);
                            }else if(GameManager.getInstance().isPlayerSpectator(player)){
                                GameManager.getInstance().getGame(GameManager.getInstance().getSpectatePlayerId(player)).removeSpectator(player, false);
                            }
                        }catch (Exception e){}
                    }
                }else if(args[0].equalsIgnoreCase("forcespectate")){
                    if(args.length == 3){
                        try{
                            Player player = Bukkit.getPlayer(args[1]);
                            Game game = GameManager.getInstance().getGame(Integer.parseInt(args[2]));
                            game.addSpectator(player);
                        }catch (Exception e){}
                    }
                }else if(args[0].equalsIgnoreCase("update")){
                    LobbyManager.getInstance().updateAll();
                    MessageManager.getInstance().sendMessage("Updating All Lobby Signs", p);
                }else if(args[0].equalsIgnoreCase("openMenu")){
                    p.openInventory(MenuManager.getInstance().getPages().get(0).getInventory());
                    MessageManager.getInstance().sendMessage("Opening Menu", p);
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
