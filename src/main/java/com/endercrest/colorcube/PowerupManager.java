package com.endercrest.colorcube;

import com.endercrest.colorcube.powerups.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
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
        powerupTypes.put("scatter", new Scatter());
    }

    public HashMap<String, SubPowerup> getPowerupTypes(){
        return powerupTypes;
    }

    public SubPowerup getRandomPowerup(){
        Random r = new Random();
        int random = r.nextInt(powerupTypes.size() - 1 + 1) + 1;
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


}
