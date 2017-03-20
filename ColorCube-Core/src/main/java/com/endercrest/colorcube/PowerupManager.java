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

    public static PowerupManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;

        powerupTypes.put("freeze", new Freeze());
        powerupTypes.put("splash", new Splash());
        powerupTypes.put("swap", new Swap());
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

    public int getPowerupId(SubPowerup powerup) {
        int id = 0;
        for (String s : powerupTypes.keySet()) {
            if (powerupTypes.get(s).equals(powerup)) {
                return id;
            }
            id++;
        }
        return -1;
    }


}
