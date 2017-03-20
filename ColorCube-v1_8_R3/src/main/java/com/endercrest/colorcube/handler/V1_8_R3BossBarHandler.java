package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.handler.bossbar.BossBar;
import com.endercrest.colorcube.handler.bossbar.V1_8_R3BossBar;
import org.bukkit.Bukkit;

public class V1_8_R3BossBarHandler implements BossBarHandler {

    boolean isBossBarAPI;

    public V1_8_R3BossBarHandler(){
        isBossBarAPI = Bukkit.getPluginManager().isPluginEnabled("BossBarAPI");
    }

    @Override
    public BossBar createBossBar(String title, BossBar.BarColor color, BossBar.BarStyle style, BossBar.BarFlag... flags) {
        BossBar bossBar = new V1_8_R3BossBar(isBossBarAPI);
        bossBar.setTitle(title);
        bossBar.setColor(color);
        bossBar.setStyle(style);
        for(BossBar.BarFlag flag: flags)
            bossBar.addFlag(flag);

        return bossBar;
    }
}
