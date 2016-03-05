package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Splash implements SubPowerup {
    @Override
    public void onRightClick(Player p, Game g) {
        Location loc = p.getLocation();
        splash(p, g, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0,0,0);
        splash(p, g, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0,1,0);
        splash(p, g, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0,2,0);

        MessageManager.getInstance().debugConsole("Splash Powerup Used in Arena " + g.getId());
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.BUCKET, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "Splash!");
        item.setItemMeta(itemMeta);
        return item;
    }

    public void splash(Player p, Game g, int xStart, int yStart, int zStart, int xI, int yI, int zI){
        for(int x = 0; x < 3;x++){
            for(int z = 0; z < 3; z++){
                Location loc = new Location(p.getWorld(), xStart + x + xI, yStart + yI, zStart + z + zI);
                Location loc1 = new Location(p.getWorld(), xStart - x + xI, yStart + yI, zStart - z + zI);
                Location loc2 = new Location(p.getWorld(), xStart + x + xI, yStart + yI, zStart - z + zI);
                Location loc3 = new Location(p.getWorld(), xStart - x + xI, yStart + yI, zStart + z + zI);
                loc.subtract(0,1,0);
                loc1.subtract(0,1,0);
                loc2.subtract(0,1,0);
                loc3.subtract(0,1,0);
                if(g.isBlockInArena(loc)) {
                    if (loc.getBlock().getData() != (byte) 15)
                        if (SettingsManager.getInstance().getPluginConfig().getStringList("paintable-blocks").contains(loc.getBlock().getType().toString())) {
                            g.changeBlock(loc, g.getTeamID(p));
                        }
                }
                if(g.isBlockInArena(loc1)){
                    if (loc1.getBlock().getData() != (byte) 15)
                        if (SettingsManager.getInstance().getPluginConfig().getStringList("paintable-blocks").contains(loc1.getBlock().getType().toString())) {
                            g.changeBlock(loc1, g.getTeamID(p));
                        }
                }
                if(g.isBlockInArena(loc2)){
                    if (loc2.getBlock().getData() != (byte) 15)
                        if (SettingsManager.getInstance().getPluginConfig().getStringList("paintable-blocks").contains(loc2.getBlock().getType().toString())) {
                            g.changeBlock(loc2, g.getTeamID(p));
                        }
                }
                if(g.isBlockInArena(loc3)){
                    if (loc3.getBlock().getData() != (byte) 15)
                        if (SettingsManager.getInstance().getPluginConfig().getStringList("paintable-blocks").contains(loc3.getBlock().getType().toString())) {
                            g.changeBlock(loc3, g.getTeamID(p));
                        }
                }

            }
        }
    }
}
