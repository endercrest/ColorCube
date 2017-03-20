package com.endercrest.colorcube.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Thomas Cordua-von Specht on 1/27/2017.
 */
public class MessageUtilTest {

    private FileConfiguration messageConfig;

    @Before
    public void setupFile() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        File file = new File(classLoader.getResource("messages.yml").toURI());
        messageConfig = YamlConfiguration.loadConfiguration(file);
    }

    @Test
    public void testMessageRetrieval(){
        String expected = "&6Game starting in 10";
        String test = MessageUtil.replaceVars(messageConfig.getString("messages.game.countdown", ""), "t-10");
        assertThat(test, is(expected));
    }
}