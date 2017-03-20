package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SubPowerup {

    public void onRightClick(Player p, Game g);

    public ItemStack getItem();
}
