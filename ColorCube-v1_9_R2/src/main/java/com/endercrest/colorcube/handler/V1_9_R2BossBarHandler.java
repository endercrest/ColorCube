package com.endercrest.colorcube.handler;

import com.endercrest.colorcube.handler.bossbar.BossBar;
import com.endercrest.colorcube.handler.bossbar.V1_9_R2BossBar;

public class V1_9_R2BossBarHandler implements BossBarHandler {

    @Override
    public BossBar createBossBar(String title, BossBar.BarColor color, BossBar.BarStyle style, BossBar.BarFlag... flags) {
        BossBar bossBar = new V1_9_R2BossBar();
        bossBar.setTitle(title);
        bossBar.setColor(color);
        bossBar.setStyle(style);
        for(BossBar.BarFlag flag: flags)
            bossBar.addFlag(flag);

        return bossBar;
    }
}
