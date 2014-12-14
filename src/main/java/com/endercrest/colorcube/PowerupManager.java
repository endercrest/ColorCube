package com.endercrest.colorcube;

import com.endercrest.colorcube.powerups.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PowerupManager {

    public static PowerupManager instance = new PowerupManager();

    private ColorCube plugin;
    private HashMap<String, SubPowerup> powerupTypes = new HashMap<String, SubPowerup>();

    private List<Player> frozenPlayers = new ArrayList<Player>();

    public static PowerupManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;

        powerupTypes.put("freeze", new Freeze());
        powerupTypes.put("splash", new Splash());
        /*powerupTypes.put("swap", new Swap());
        //powerupTypes.put("scatter", new Scatter());*/
    }

    public HashMap<String, SubPowerup> getPowerupTypes(){
        return powerupTypes;
    }

    public SubPowerup getRandomPowerup(){
        Random r = new Random();
        int random = r.nextInt(powerupTypes.size());
        int count = 0;
        for(String s: powerupTypes.keySet()){
            if(count == random){
                return powerupTypes.get(s);
            }else {
                ++count;
            }
        }
        return null;
    }

    public String getPowerupName(SubPowerup powerup) {
        for(String string: powerupTypes.keySet()){
            if(powerupTypes.get(string).equals(powerup)){
                return string;
            }
        }
        return null;
    }

    public SubPowerup getPowerup(ItemStack item){
        for(SubPowerup p: powerupTypes.values()){
            if(p.getItem().equals(item)){
                return p;
            }
        }
        return null;
    }

    public SubPowerup getPowerup(int num){
        int count = 0;
        for(String s: powerupTypes.keySet()){
            if(count == num){
                return powerupTypes.get(s);
            }else{
                count++;
            }
        }
        return null;
    }

    public void addFrozenPlayer(Player p){
        frozenPlayers.add(p);
    }

    public void addFrozenPlayers(List<Player> players){
        for(Player p: players){
            frozenPlayers.add(p);
        }
    }

    public void removeFrozenPlayer(Player p){
        frozenPlayers.remove(p);
    }

    public void removeFrozenPlayers(List<Player> players){
        for(Player p: players){
            frozenPlayers.remove(p);
        }
    }

    public boolean isPlayerFrozen(Player p){
        return frozenPlayers.contains(p);
    }


}
