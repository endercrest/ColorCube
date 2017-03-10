package com.endercrest.colorcube.commands;

import org.bukkit.entity.Player;

public interface SubCommand {

    public boolean onCommand(Player p, String[] args);

    public String helpInfo();

    public String permission();
}
