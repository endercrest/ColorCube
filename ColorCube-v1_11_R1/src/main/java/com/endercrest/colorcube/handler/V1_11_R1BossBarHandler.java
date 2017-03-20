package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.handler.bossbar.BossBar;
import com.endercrest.colorcube.handler.bossbar.V1_11_R1BossBar;

public class V1_11_R1BossBarHandler implements BossBarHandler {

    @Override
    public BossBar createBossBar(String title, BossBar.BarColor color, BossBar.BarStyle style, BossBar.BarFlag... flags) {
        BossBar bossBar = new V1_11_R1BossBar();
        bossBar.setTitle(title);
        bossBar.setColor(color);
        bossBar.setStyle(style);
        for(BossBar.BarFlag flag: flags)
            bossBar.addFlag(flag);

        return bossBar;
    }
}
