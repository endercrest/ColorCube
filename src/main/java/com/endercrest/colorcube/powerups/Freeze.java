package com.endercrest.colorcube.powerups;

import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Freeze implements SubPowerup {
    @Override
    public void onRightClick(Player p, Game g) {
        g.msgFArena("game.freeze", "player-" + p.getDisplayName());
        List<Player> players = new ArrayList<Player>();
        for(Player player: g.getAllPlayers()){
            players.add(player);
        }
        players.remove(p);
        for(Player player: players){
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 100, true));
        }
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
