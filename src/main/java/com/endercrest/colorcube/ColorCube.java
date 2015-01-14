package com.endercrest.colorcube;

import com.endercrest.colorcube.events.*;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.logging.QueueManager;
import com.endercrest.colorcube.utils.Update;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ColorCube extends JavaPlugin {

    private static WorldEditPlugin worldEdit;

    static ColorCube p = null;
    @Override
    public void onEnable(){
        ColorCube.p = this;
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Startup(), 10);
    }

    @Override
    public void onDisable(){
        SettingsManager.getInstance().saveSystemConfig();
        reloadConfig();
        for (Game g: GameManager.getInstance().getGames()) {
            try{
                g.disable();
            }catch(Exception e){}
            QueueManager.getInstance().rollback(g.getGameID(), true);
        }
        MessageManager.getInstance().log("&e" + getDescription().getVersion() + " by EnderCrest disabled");
    }

    public void loadDependencies(){
        worldEdit = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit != null) {
            MessageManager.getInstance().log("&eWorldEdit has been found.");
        } else {
            MessageManager.getInstance().log("&cWorldEdit not found! Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public WorldEditPlugin getWorldEdit(){
        return worldEdit;
    }

    class Startup implements Runnable {
        public void run() {
            PluginManager pm = Bukkit.getPluginManager();
            MessageManager.getInstance().setup(p);
            SettingsManager.getInstance().setup(p);
            GameManager.getInstance().setup(p);
            LobbyManager.getInstance().setup(p);
            QueueManager.getInstance().setup(p);
            PowerupManager.getInstance().setup(p);

            pm.registerEvents(new PlayerMoveListener(), p);
            pm.registerEvents(new PlayerRespawnListener(), p);
            pm.registerEvents(new PlayerBreakListener(), p);
            pm.registerEvents(new ExplosionListener(), p);
            pm.registerEvents(new PlayerPlaceListener(), p);
            pm.registerEvents(new PlayerDamageListener(), p);
            pm.registerEvents(new PlayerLeaveListener(), p);
            pm.registerEvents(new PlayerRightClickListener(), p);
            pm.registerEvents(new PlayerInventoryListener(), p);
            pm.registerEvents(new PreCommandListener(), p);
            pm.registerEvents(new PlayerPVPListener(), p);
            pm.registerEvents(new PlayerPickupListener(), p);
            pm.registerEvents(new PlayerDropListener(), p);
            pm.registerEvents(new PlayerClickListener(), p);

            loadDependencies();
            getCommand("colorcube").setExecutor(new CommandHandler(p));
            MessageManager.getInstance().log("&e" + getDescription().getVersion() + " by EnderCrest enabled");

            if(SettingsManager.getInstance().getPluginConfig().getBoolean("update-checker")){
                Update update = new Update(87360);
            }
        }
    }

    public static ColorCube getPlugin(){
        return p;
    }
}
