package com.endercrest.colorcube.migration;

import com.endercrest.colorcube.SettingsManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Created by Thomas Cordua-von Specht on 1/27/2017.
 *
 * MigrationService Test, this
 */
public class MigrationServiceTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();
    private File pluginFolder;

    @Before
    public void setUp() throws Exception {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        pluginFolder = new File(folder.getRoot(), "plugin");

        pluginFolder.mkdirs();
        try {
            File oldConfig = new File(classLoader.getResource("system.yml").toURI());
            File newConfig = new File(pluginFolder, "system.yml");
            oldConfig.renameTo(newConfig);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runMigration() throws Exception {
        MigrationService migrationService = new MigrationService(pluginFolder);
        migrationService.runMigration();

        assertTrue("Failed to archive system file.", new File(pluginFolder, "system_archive.yml").exists());

        arenaTests();
        signTests();
    }

    private void signTests(){
        File signFolder = new File(pluginFolder, "Sign");
        File[] files = signFolder.listFiles();
        assertNotNull("No files have been generated in Sign folder", files);
        assertEquals(String.format("4 Arena Files and 1 Archive Folder should have been generated(%s were generated).", files.length),  files.length, 8);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(signFolder, "global.yml"));
        assertEquals(config.get("version"), SettingsManager.SIGN_VERSION);
    }

    private void arenaTests(){
        File arenaFolder = new File(pluginFolder, "Arena");
        File[] files = arenaFolder.listFiles();
        assertNotNull("No files have been generated in Arena folder", files);
        assertEquals(String.format("4 Arena Files and 1 Archive Folder should have been generated(%s were generated).", files.length),  files.length ,5);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(arenaFolder, "global.yml"));
        assertEquals(config.get("version"), SettingsManager.ARENA_VERSION);
    }
}