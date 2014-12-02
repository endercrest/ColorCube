package com.endercrest.colorcube.commands;

import org.bukkit.entity.Player;

public class ResetSpawns implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        return false;
    }

    @Override
    public String helpInfo() {
        return null;
    }

    @Override
    public String permission() {
        return "cc.arena.";
    }
}
