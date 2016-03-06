package com.endercrest.colorcube;

import com.endercrest.colorcube.utils.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MessageTest {

    File file;
    FileConfiguration messageConfig;

    @Before
    public void setupFile() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        file = new File(classLoader.getResource("messages.yml").toURI());
        messageConfig = YamlConfiguration.loadConfiguration(file);
    }

    @Test
    public void testMessageRetrieval(){
        String expected = "&6Game starting in 10";
        String test = MessageUtil.replaceVars(messageConfig.getString("messages.game.countdown", ""), "t-10");
        assertThat(test, is(expected));
    }


}
