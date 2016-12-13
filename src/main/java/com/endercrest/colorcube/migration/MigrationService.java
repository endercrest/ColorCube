package com.endercrest.colorcube.migration;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.MessageManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Thomas Cordua-von Specht on 12/12/2016.
 */
public class MigrationService {

    private ColorCube plugin;

    public MigrationService(ColorCube plugin){
        this.plugin = plugin;
    }

    public boolean runMigration(){
        return migrate20161213();
    }

    /**
     * Run the migration of 2016 December 12th
     *
     * This migration converts system.yml into its separate file systems.
     * Will convert arenas section into the new individual files and the same for signs.
     * @return The result of the migration Successful or Unsuccessful. Will also return true
     * if it has already been completed.
     */
    private boolean migrate20161213(){
        File file = new File(plugin.getDataFolder(), "system.yml");
        if(!file.exists()){
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: This migration has already been completed.");
            return true;
        }
        MessageManager.getInstance().log("Starting Migration 13/12/2016.");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        MessageManager.getInstance().debugConsole("Migration 13/12/2016: Migrating arena data.");

        File arenaFolder = new File(plugin.getDataFolder(), "Arena");
        if(arenaFolder.mkdirs())
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Creating arena folder.");

        File arenaArchiveFolder = new File(arenaFolder, "Archive");
        if(arenaArchiveFolder.mkdirs())
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Creating arena archive folder.");

        //Migrating each arena.
        ConfigurationSection arenaSection = config.getConfigurationSection("arenas");
        if(arenaSection != null) {
            for (String id : arenaSection.getKeys(false)) {
                String fileName = "Arena" + id + ".yml";
                File arenaFile;
                if (arenaSection.getBoolean(id + ".enabled"))
                    arenaFile = new File(arenaFolder, fileName);
                else arenaFile = new File(arenaArchiveFolder, fileName);

                if (arenaFile.exists()) {
                    MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: %s already exists, skipping migration of arena %s", fileName, id));
                    continue;
                }

                try {
                    if (arenaFile.createNewFile())
                        MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: Created %s", fileName));
                } catch (IOException e) {
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Failed to create %s. Aborting migration.", fileName));
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                    return false;
                }

                FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
                MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: Transferring arena %s data", id));
                arenaConfig.set("version", 0);

                //Arena Positional Data
                arenaConfig.set("loc.world", arenaSection.getString(id + ".world"));
                arenaConfig.set("loc.pos1.x", arenaSection.getInt(id + ".x1"));
                arenaConfig.set("loc.pos1.y", arenaSection.getInt(id + ".y1"));
                arenaConfig.set("loc.pos1.z", arenaSection.getInt(id + ".z1"));
                arenaConfig.set("loc.pos2.x", arenaSection.getInt(id + ".x2"));
                arenaConfig.set("loc.pos2.y", arenaSection.getInt(id + ".y2"));
                arenaConfig.set("loc.pos2.z", arenaSection.getInt(id + ".z2"));
                //Arena Option Data
                arenaConfig.set("options.pvp", arenaSection.getBoolean(id + ".pvp"));
                arenaConfig.set("options.reward", arenaSection.getDouble(id + ".reward", 0.0));
                arenaConfig.set("options.name", String.format("Arena %s", id));
                //Arena Lobby Data
                arenaConfig.set("lobby.world", arenaSection.getString(id + ".lworld"));
                arenaConfig.set("lobby.pos1.x", arenaSection.getInt(id + ".lx1"));
                arenaConfig.set("lobby.pos1.y", arenaSection.getInt(id + ".ly1"));
                arenaConfig.set("lobby.pos1.z", arenaSection.getInt(id + ".lz1"));
                arenaConfig.set("lobby.pos2.x", arenaSection.getInt(id + ".lx2"));
                arenaConfig.set("lobby.pos2.y", arenaSection.getInt(id + ".ly2"));
                arenaConfig.set("lobby.pos2.z", arenaSection.getInt(id + ".lz2"));
                //Arena Lobby Spawn Point
                arenaConfig.set("lobby.spawn.world", config.getString("lobby." + id + ".world"));
                arenaConfig.set("lobby.spawn.x", config.getDouble("lobby." + id + ".x"));
                arenaConfig.set("lobby.spawn.y", config.getDouble("lobby." + id + ".y"));
                arenaConfig.set("lobby.spawn.z", config.getDouble("lobby." + id + ".z"));
                arenaConfig.set("lobby.spawn.yaw", config.getDouble("lobby." + id + ".yaw"));
                arenaConfig.set("lobby.spawn.pitch", config.getDouble("lobby." + id + ".pitch"));
                //Arena Spawn Points.
                ConfigurationSection spawnSection = config.getConfigurationSection("spawns." + id);
                if (spawnSection != null) {
                    for (String spawnId : spawnSection.getKeys(false)) {
                        arenaConfig.set("spawns." + spawnId + ".x", spawnSection.getInt(spawnId + ".x"));
                        arenaConfig.set("spawns." + spawnId + ".y", spawnSection.getInt(spawnId + ".y"));
                        arenaConfig.set("spawns." + spawnId + ".z", spawnSection.getInt(spawnId + ".z"));
                        arenaConfig.set("spawns." + spawnId + ".yaw", spawnSection.getDouble(spawnId + ".yaw"));
                        arenaConfig.set("spawns." + spawnId + ".pitch", spawnSection.getDouble(spawnId + ".pitch"));
                    }
                }
                //Arena Status
                arenaConfig.set("enabled", arenaSection.getBoolean(id + ".enabled"));


                try {
                    arenaConfig.save(arenaFile);
                    MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: Successfully migrated Arena %s into %s", id, fileName));
                } catch (IOException e) {
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Failed to save %s, aborting migration.", fileName));
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                    return false;
                }
            }
        }

        //Global Arena File
        MessageManager.getInstance().debugConsole("Migration 13/12/2016: Creating global arena file");
        File globalArenaFile = new File(arenaFolder, "global.yml");
        if(!globalArenaFile.exists()){
            try {
                if(globalArenaFile.createNewFile())
                    MessageManager.getInstance().debugConsole("Migration 13/12/2016: Created global.yml");
            } catch (IOException e) {
                MessageManager.getInstance().log("&cMigration 13/12/2016: Failed to create global arena file. Aborting migration.");
                MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                return false;
            }
            FileConfiguration globalArenaConfig = YamlConfiguration.loadConfiguration(globalArenaFile);
            globalArenaConfig.set("version", 0);

            //Arena Id Count
            globalArenaConfig.set("nextId", config.getInt("arena_next_id", 0));
            //Global lobby information.
            globalArenaConfig.set("lobby.world", config.getString("lobby.global.world"));
            globalArenaConfig.set("lobby.x", config.getInt("lobby.global.x"));
            globalArenaConfig.set("lobby.y", config.getInt("lobby.global.y"));
            globalArenaConfig.set("lobby.z", config.getInt("lobby.global.z"));
            globalArenaConfig.set("lobby.yaw", config.getDouble("lobby.global.yaw"));
            globalArenaConfig.set("lobby.pitch", config.getDouble("lobby.global.pitch"));

            try {
                globalArenaConfig.save(globalArenaFile);
                MessageManager.getInstance().debugConsole("Migration 13/12/2016: Successfully saved global.yml");
            } catch (IOException e) {
                MessageManager.getInstance().log("&cMigration 13/12/2016: Failed to save global.yml, aborting migration.");
                MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                return false;
            }
        }else{
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Skipping global arena file, it already exists.");
        }


        //TODO Sign Migration
        File signFolder = new File(plugin.getDataFolder(), "Sign");
        if(signFolder.mkdirs())
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Creating sign folder.");

        File signArchiveFolder = new File(signFolder, "Archive");
        if(signArchiveFolder.mkdirs())
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Creating sign archive folder.");

        //Migrate Sign Data.
        ConfigurationSection signSection = config.getConfigurationSection("signs");
        if(signSection != null){
            for(String signId: signSection.getKeys(false)){
                String fileName = "Sign" + signId + ".yml";
                File signFile;
                if (signSection.getBoolean(signId + ".enabled"))
                    signFile = new File(signFolder, fileName);
                else signFile = new File(signArchiveFolder, fileName);

                if (signFile.exists()) {
                    MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: %s already exists, skipping migration of sign %s", fileName, signId));
                    continue;
                }

                try {
                    if (signFile.createNewFile())
                        MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: Created %s successfully", fileName));
                } catch (IOException e) {
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Failed to create %s. Aborting migration.", fileName));
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                    return false;
                }

                FileConfiguration signConfig = YamlConfiguration.loadConfiguration(signFile);
                signConfig.set("version", 0);

                signConfig.set("loc.world", signSection.getString(signId+".world"));
                signConfig.set("loc.x", signSection.getInt(signId+".x"));
                signConfig.set("loc.y", signSection.getInt(signId+".y"));
                signConfig.set("loc.z", signSection.getInt(signId+".z"));
                signConfig.set("gameId", signSection.getInt(signId+".gameID"));
                signConfig.set("enabled", signSection.getBoolean(signId+".enabled"));

                try {
                    signConfig.save(signFile);
                    MessageManager.getInstance().debugConsole(String.format("Migration 13/12/2016: Successfully migrated Sign %s into %s", signId, fileName));
                } catch (IOException e) {
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Failed to save %s, aborting migration.", fileName));
                    MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                    return false;
                }
            }
        }

        //Global Sign File
        MessageManager.getInstance().debugConsole("Migration 13/12/2016: Creating global sign file");
        File globalSignFile = new File(signFolder, "global.yml");
        if(!globalSignFile.exists()){
            try {
                if(globalSignFile.createNewFile())
                    MessageManager.getInstance().debugConsole("Migration 13/12/2016: Created global.yml for signs");
            } catch (IOException e) {
                MessageManager.getInstance().log("&cMigration 13/12/2016: Failed to create global sign file. Aborting migration.");
                MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                return false;
            }
            FileConfiguration globalSignConfig = YamlConfiguration.loadConfiguration(globalSignFile);
            globalSignConfig.set("version", 0);

            //Arena Id Count
            globalSignConfig.set("nextId", config.getInt("sign_next_id", 0));

            try {
                globalSignConfig.save(globalSignFile);
                MessageManager.getInstance().debugConsole("Migration 13/12/2016: Successfully saved global.yml for signs");
            } catch (IOException e) {
                MessageManager.getInstance().log("&cMigration 13/12/2016: Failed to save global.yml for signs, aborting migration.");
                MessageManager.getInstance().log(String.format("&cMigration 13/12/2016: Error: %s", e.getLocalizedMessage()));
                return false;
            }
        }else{
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Skipping global arena file, it already exists.");
        }

        //TODO Remove Commenting for deleting system.yml once all other systems are reading the new format.
        //if(file.delete())
        //    MessageManager.getInstance().debugConsole("Migration 13/12/2016: Deleted the system.yml file.");

        MessageManager.getInstance().log("Migration 13/12/2016: Migration Completed.");
        return true;
    }


}
