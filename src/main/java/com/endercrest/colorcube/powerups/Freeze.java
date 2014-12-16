package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.PowerupManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class Freeze implements SubPowerup {
    @Override
    public void onRightClick(Player p, Game g) {
        final List<Player> playerList = g.getActivePlayers();
        PowerupManager.getInstance().addFrozenPlayers(g.getActivePlayers());
        PowerupManager.getInstance().removeFrozenPlayer(p);

        g.msgFArena("game.freeze", "player-" + p.getDisplayName());

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(ColorCube.getPlugin(), new Runnable() {
            @Override
            public void run() {
                PowerupManager.getInstance().removeFrozenPlayers(playerList);
            }
        }, 100L);
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
