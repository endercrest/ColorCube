package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.PowerupManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Freeze implements SubPowerup {
    @Override
    public void onRightClick(Player p, Game g) {
        PowerupManager.getInstance().addFrozenPlayers(g.getActivePlayers());
        PowerupManager.getInstance().removeFrozenPlayer(p);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.ICE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "Freeze!");
        item.setItemMeta(itemMeta);
        return item;
    }
}
