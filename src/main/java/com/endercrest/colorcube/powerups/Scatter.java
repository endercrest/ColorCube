package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Scatter implements SubPowerup {
    @Override
    public void onRightClick(Player p, Game g) {

    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.SNOW_BALL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "Scatter!");
        item.setItemMeta(itemMeta);
        return item;
    }
}
