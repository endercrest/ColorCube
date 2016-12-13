package com.endercrest.colorcube;

import com.endercrest.colorcube.events.*;
import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.logging.QueueManager;
import com.endercrest.colorcube.migration.MigrationService;
import com.endercrest.colorcube.utils.ColorCubeTabCompleter;
import com.endercrest.colorcube.utils.Update;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ColorCube extends JavaPlugin {

    private static WorldEditPlugin worldEdit;
    public Vault vault;

    public static Economy economy = null;

    @Override
    public void onEnable(){
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Startup(this), 10);
    }

    @Override
    public void onDisable(){
        SettingsManager.getInstance().saveSystemConfig();
        reloadConfig();
        for (Game g: GameManager.getInstance().getGames()) {
            g.disable(true);
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
        vault = (Vault) getServer().getPluginManager().getPlugin("Vault");
        if(vault != null){
            MessageManager.getInstance().log("&eVault has been found");
        }else{
            MessageManager.getInstance().log("&cVault not found! Disabling Economy Support");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public WorldEditPlugin getWorldEdit(){
        return worldEdit;
    }

    class Startup implements Runnable {
        ColorCube p;

        public Startup(ColorCube p){
            this.p = p;
        }

        public void run() {
            PluginManager pm = Bukkit.getPluginManager();
            MessageManager.getInstance().setup(p);

            boolean migrationResult = new MigrationService(p).runMigration();

            if(!migrationResult){
                SettingsManager.getInstance().setup(p);
                MessageManager.getInstance().log("&cA Migration has failed, disabling plugin.");
                pm.disablePlugin(p);
                return;
            }

            SettingsManager.getInstance().setup(p);
            GameManager.getInstance().setup(p);
            LobbyManager.getInstance().setup(p);
            QueueManager.getInstance().setup(p);
            PowerupManager.getInstance().setup(p);
            MenuManager.getInstance().setup();

            pm.registerEvents(new PlayerMoveListener(p), p);
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
            if(vault != null){
                setupEconomy();
            }
            getCommand("colorcube").setExecutor(new CommandHandler(p));
            getCommand("colorcube").setTabCompleter(new ColorCubeTabCompleter(p));
            MessageManager.getInstance().log("&e" + getDescription().getVersion() + " by EnderCrest enabled");

            if(SettingsManager.getInstance().getPluginConfig().getBoolean("update-checker")){
                Update update = new Update(87360);
            }
        }
    }
}
