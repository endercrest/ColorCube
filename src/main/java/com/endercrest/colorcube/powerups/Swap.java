package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.game.Arena;
import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class Swap implements SubPowerup {
    @Override
    public void onRightClick(Player p, Game g) {
        Arena arena = g.getArena();
        Random random = new Random();
        for(int i = 0; i < 20; i ++){
            double x;
            double y;
            double z;
            boolean finish = false;
            int attempt = 0;
            while(!finish) {
                x = random.nextInt((arena.getPos1().getBlockX() - arena.getPos2().getBlockX()) + 1) + arena.getPos2().getBlockX() + 0.5;
                y = random.nextInt((arena.getPos1().getBlockY() - arena.getPos2().getBlockY()) + 1) + arena.getPos2().getBlockY();
                z = random.nextInt((arena.getPos1().getBlockZ() - arena.getPos2().getBlockZ()) + 1) + arena.getPos2().getBlockZ() + 0.5;
                Location loc = new Location(arena.getPos1().getWorld(), x, y, z);
                Location loc2 = new Location(arena.getPos1().getWorld(), x, y, z).subtract(0,1,0);
                if(loc2.getBlock().getType() == Material.STAINED_CLAY){
                    if(loc.getBlock().getType() == Material.AIR) {
                        if (loc.getBlock().getData() != (byte) 15) {
                            if (loc2.getBlock().getData() != g.getTeamBlockByte(g.getTeamID(p))) {
                                g.changeBlock(loc2, g.getTeamID(p));
                                finish = true;
                            }
                        }
                    }
                }
                if(attempt == 50){
                    finish = true;
                    MessageManager.getInstance().debugConsole("Could Not swap block failed after 5 tries.");
                }
                attempt++;
            }
        }
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.RED_MUSHROOM);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "Swap!");
        item.setItemMeta(itemMeta);
        return item;
    }
}
