package com.endercrest.colorcube.migration;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.MessageManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Thomas Cordua-von Specht on 12/12/2016.
 */
public class MigrationService {

    private ColorCube plugin;

    public MigrationService(ColorCube plugin){
        this.plugin = plugin;
    }

    public boolean runMigration(){
        return migrate20161213() && migrate20161215();
    }

    /**
     * Run the migration of 2016 December 12th
     *
     * This migration converts system.yml into its separate file systems.
     * Will convert arenas section into the new individual files and the same for signs.
     *
     * Creates arena<id>.yml with the version 0
     *
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
                String fileName = "arena" + id + ".yml";
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

                //ID
                arenaConfig.set("id", Integer.parseInt(id));
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
                arenaConfig.set("lobby.spawn.x", config.getDouble("lobby." + id + ".x")+0.5);
                arenaConfig.set("lobby.spawn.y", config.getDouble("lobby." + id + ".y"));
                arenaConfig.set("lobby.spawn.z", config.getDouble("lobby." + id + ".z")+0.5);
                arenaConfig.set("lobby.spawn.yaw", config.getDouble("lobby." + id + ".yaw"));
                arenaConfig.set("lobby.spawn.pitch", config.getDouble("lobby." + id + ".pitch"));
                //Arena Spawn Points.
                ConfigurationSection spawnSection = config.getConfigurationSection("spawns." + id);
                if (spawnSection != null) {
                    for (String spawnId : spawnSection.getKeys(false)) {
                        if(spawnId.equalsIgnoreCase("count"))
                            continue;

                        arenaConfig.set("spawns." + spawnId + ".x", spawnSection.getDouble(spawnId + ".x")+0.5);
                        arenaConfig.set("spawns." + spawnId + ".y", spawnSection.getDouble(spawnId + ".y"));
                        arenaConfig.set("spawns." + spawnId + ".z", spawnSection.getDouble(spawnId + ".z")+0.5);
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
                String fileName = "sign" + signId + ".yml";
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

                signConfig.set("id", Integer.parseInt(signId));
                //Location Info
                signConfig.set("loc.world", signSection.getString(signId+".world"));
                signConfig.set("loc.x", signSection.getInt(signId+".x"));
                signConfig.set("loc.y", signSection.getInt(signId+".y"));
                signConfig.set("loc.z", signSection.getInt(signId+".z"));
                //Other information
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

        if(file.renameTo(new File(plugin.getDataFolder(), "system_archive.yml")))
            MessageManager.getInstance().debugConsole("Migration 13/12/2016: Renamed system.yml to system_archive.yml.");

        MessageManager.getInstance().log("Migration 13/12/2016: Migration Completed.");
        return true;
    }

    /**
     * Run the migration of 2016 December 15th
     *
     * This migration updates the spawns in each arena to follow the format of the new team based spawns.
     * The first 4 spawns will be assigned to the teams, then the rest of the spawns will be removed.
     *
     * This will check for arena configs of version 0 and upgrade it to version 1.
     *
     * @return The result of the migration and whether it was Successful or Unsuccessful. True is also
     * returned if it has already been completed.
     */
    private boolean migrate20161215(){
        File arenaFolder = new File(plugin.getDataFolder(), "Arena");

        if(arenaFolder != null && arenaFolder.listFiles().length > 0){
            for(File file: arenaFolder.listFiles()){
                if(file.isFile()) {
                    if(file.getName().equalsIgnoreCase("global.yml")){
                        continue;
                    }
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    if (config.getInt("version") == 0) {
                        config.set("spawns_old", config.get("spawns"));
                        config.set("spawns", null);

                        ConfigurationSection spawns = config.getConfigurationSection("spawns_old");
                        Set<String> keys = spawns.getKeys(false);
                        if (keys != null && keys.size() > 0) {
                            MessageManager.getInstance().debugConsole(String.format("Migration 15/12/2016: Upgrading %s", file.getName()));

                            int originalSpawnCount = keys.size();
                            int teamCount = Math.min(4, originalSpawnCount);
                            int slotsPerTeam = teamCount != 0 ? (int) (Math.ceil(originalSpawnCount / teamCount)) : 1;

                            config.set("options.perteam", slotsPerTeam);

                            String[] keyArray = keys.toArray(new String[]{});
                            if (teamCount < originalSpawnCount) {
                                MessageManager.getInstance().log(String.format("Migration 15/12/2016: Please note that some spawns have been removed from %s", file.getName()));
                            }
                            for (int i = 0; i < teamCount; i++) {
                                String key = keyArray[i];

                                String team;
                                switch (i) {
                                    case 0:
                                        team = "red";
                                        break;
                                    case 1:
                                        team = "blue";
                                        break;
                                    case 2:
                                        team = "green";
                                        break;
                                    case 3:
                                        team = "yellow";
                                        break;
                                    default:
                                        team = "red";
                                        break;
                                }

                                config.set("spawns." + team + ".x", config.getDouble("spawns_old." + key + ".x"));
                                config.set("spawns." + team + ".y", config.getDouble("spawns_old." + key + ".y"));
                                config.set("spawns." + team + ".z", config.getDouble("spawns_old." + key + ".z"));
                                config.set("spawns." + team + ".yaw", config.getDouble("spawns_old." + key + ".yaw"));
                                config.set("spawns." + team + ".pitch", config.getDouble("spawns_old." + key + ".pitch"));

                            }

                            config.set("spawns_old", null);
                            config.set("version", 1);

                            try {
                                MessageManager.getInstance().debugConsole(String.format("Migration 15/12/2016: Saving %s", file.getName()));
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}









