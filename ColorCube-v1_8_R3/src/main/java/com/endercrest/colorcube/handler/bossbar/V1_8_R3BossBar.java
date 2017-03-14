package com.endercrest.colorcube.handler.bossbar;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Cordua-von Specht on 3/10/2017.
 *
 * The 1.8 Handler for the boss bar that uses a different packet system then upper versions.
 */
public class V1_8_R3BossBar implements BossBar {

    private boolean bossBarAPI;

    private List<Player> players;
    private double progress;
    private String title;
    private boolean visible;

    public V1_8_R3BossBar(boolean bossBarAPI){
        this.bossBarAPI = bossBarAPI;

        players = new ArrayList<>();
        progress = 1.0;
        title = "";
        visible = true;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        for(Player player: players) {
            updateBar(player);
        }
        this.title = title;
    }

    @Override
    public BarColor getColor() {
        return BarColor.PURPLE;
    }

    @Override
    public void setColor(BarColor color) {
        //NOTHING HAPPENS SINCE 1.8 DOES NOT SUPPORT BARCOLOR.
    }

    @Override
    public BarStyle getStyle() {
        return BarStyle.SOLID;
    }

    @Override
    public void setStyle(BarStyle style) {
        //NOTHING HAPPENS SINCE 1.8 DOES NOT SUPPORT BARSTYLE
    }

    @Override
    public void removeFlag(BarFlag flag) {
        //NOTHING HAPPENS SINCE 1.8 DOES NOT SUPPORT BARFLAG
    }

    @Override
    public void addFlag(BarFlag flag) {
        //NOTHING HAPPENS SINCE 1.8 DOES NOT SUPPORT BARFLAG
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        //NOTHING HAPPENS SINCE 1.8 DOES NOT SUPPORT BARFLAG
        return false;
    }

    @Override
    public void setProgress(double progress) {
        for(Player player: players) {
            updateBar(player);
        }
        this.progress = progress;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public void addPlayer(Player player) {
        updateBar(player);
        players.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        removeBar(player);
        players.remove(player);
    }

    @Override
    public void removeAll() {
        for(Player player: players){
            removeBar(player);
        }
        players.clear();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible && this.visible != visible){
            for(Player player: players) {
                updateBar(player);
            }
        }else if(!visible && this.visible != visible){
            for(Player player: players) {
                removeBar(player);
            }
        }
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    private void updateBar(Player player){
        if(visible) {
            if (bossBarAPI){
                TextComponent text = new TextComponent(title);
                BossBarAPI.addBar(player, text, BossBarAPI.Color.PURPLE, BossBarAPI.Style.PROGRESS, (float) progress);
            }else {
                StatusBarAPI.setStatusBar(player, title, (float) progress);
            }
        }
    }

    private void removeBar(Player player){
        if(bossBarAPI){
            BossBarAPI.removeAllBars(player);
        }else{
            StatusBarAPI.removeStatusBar(player);
        }
    }
}
