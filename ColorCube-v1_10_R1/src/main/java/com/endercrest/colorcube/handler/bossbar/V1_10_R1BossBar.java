package com.endercrest.colorcube.handler.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class V1_10_R1BossBar implements BossBar {

    private org.bukkit.boss.BossBar bossBar;

    public V1_10_R1BossBar(){
        bossBar = Bukkit.createBossBar("", org.bukkit.boss.BarColor.WHITE, org.bukkit.boss.BarStyle.SOLID);
    }

    @Override
    public String getTitle() {
        return bossBar.getTitle();
    }

    @Override
    public void setTitle(String title) {
        bossBar.setTitle(title);
    }

    @Override
    public BarColor getColor() {
        return BarColor.valueOf(bossBar.getColor().name());
    }

    @Override
    public void setColor(BarColor color) {
        bossBar.setColor(org.bukkit.boss.BarColor.valueOf(color.name()));
    }

    @Override
    public BarStyle getStyle() {
        return BarStyle.valueOf(bossBar.getStyle().name());
    }

    @Override
    public void setStyle(BarStyle style) {
        bossBar.setStyle(org.bukkit.boss.BarStyle.valueOf(style.name()));
    }

    @Override
    public void removeFlag(BarFlag flag) {
        bossBar.removeFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    @Override
    public void addFlag(BarFlag flag) {
        bossBar.addFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        return bossBar.hasFlag(org.bukkit.boss.BarFlag.valueOf(flag.name()));
    }

    @Override
    public void setProgress(double progress) {
        bossBar.setProgress(progress);
    }

    @Override
    public double getProgress() {
        return bossBar.getProgress();
    }

    @Override
    public void addPlayer(Player player) {
        bossBar.addPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        bossBar.removePlayer(player);
    }

    @Override
    public void removeAll() {
        bossBar.removeAll();
    }

    @Override
    public List<Player> getPlayers() {
        return bossBar.getPlayers();
    }

    @Override
    public void setVisible(boolean visible) {
        bossBar.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return bossBar.isVisible();
    }
}
