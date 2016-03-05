package com.endercrest.colorcube.menu;

import com.endercrest.colorcube.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GameItem extends PageItem {

    private Game game;

    public GameItem(Page page, int itemIndex, Game game) {
        super(page, itemIndex);
        this.game = game;
    }

    public Game getGame(){
        return game;
    }

    @Override
    public void onClick(Player player) {
        player.closeInventory();
        switch (game.getStatus()){
            case INGAME:
                game.addSpectator(player);
                break;
            default:
                game.addPlayer(player);
                break;
        }
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
        itemStack.setAmount(game.getSpawnCount()-game.getActivePlayers().size());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Arena " + game.getId());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Players: "+game.getActivePlayers().size()+"/"+game.getSpawnCount());
        lore.add("");
        lore.add(ChatColor.GRAY+"Status: "+game.getStatus().toString());
        switch (game.getStatus()){
            case LOADING:
                lore.add(ChatColor.GRAY+"Game loading, please wait for game to finish!");
                break;
            case IDLE:
                lore.add(ChatColor.GRAY+"Click to join the game!");
                break;
            case LOBBY:
                lore.add(ChatColor.GRAY+"Click to join the game!");
                break;
            case DISABLED:
                lore.add(ChatColor.RED+"Game Disabled! Can not join.");
                break;
            case STARTING:
                lore.add(ChatColor.GRAY+"Click to join the game!");
                break;
            case INGAME:
                lore.add(ChatColor.GRAY+"Click to spectate the game!");
                break;
            case FINISHING:
                lore.add(ChatColor.GRAY+"Game ending, please wait for game to finish!");
                break;
            case RESETING:
                lore.add(ChatColor.GRAY+"Game reseting, please wait.");
                break;
            case ERROR:
                lore.add(ChatColor.RED+"ERROR!?!?!?!?!");
                break;
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
