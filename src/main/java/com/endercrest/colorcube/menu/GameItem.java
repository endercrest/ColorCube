package com.endercrest.colorcube.menu;

import com.endercrest.colorcube.MessageManager;
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
        if(itemStack.getAmount() == 0){
            itemStack.setType(Material.MAP);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(MessageManager.getInstance().getFValue("menu.item.game.title", "arena-"+game.getId()));
        List<String> lore = new ArrayList<>();
        lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.players", "activeplayers-"+game.getActivePlayers().size(), "maxplayers-"+game.getSpawnCount()));
        lore.add("");
        lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.status", "status-"+game.getStatus().toString()));
        switch (game.getStatus()){
            case LOADING:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.loading"));
                break;
            case IDLE:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.idle"));
                break;
            case LOBBY:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.lobby"));
                break;
            case DISABLED:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.disabled"));
                break;
            case STARTING:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.starting"));
                break;
            case INGAME:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.ingame"));
                break;
            case FINISHING:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.finishing"));
                break;
            case RESETING:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.resetting"));
                break;
            case ERROR:
                lore.add(MessageManager.getInstance().getFValue("menu.item.game.description.message.error"));
                break;
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
