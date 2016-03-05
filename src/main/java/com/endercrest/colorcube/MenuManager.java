package com.endercrest.colorcube;

import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.menu.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    public static MenuManager instance = new MenuManager();

    public static MenuManager getInstance(){
        return instance;
    }

    private List<Page> pages;

    public void setup(){
        pages = new ArrayList<>();
        List<Game> games = GameManager.getInstance().getGames();
        if(games.size() == 0){
            pages.add(new Page(0, new ArrayList<Game>(), true));
            return;
        }
        for(int i = 0; i < calcNumberOfPages(); i++){
            int minIndex = i*27;
            int maxIndex = i*27+27;
            boolean last = false;

            if(maxIndex >= games.size()){
                last = true;
                maxIndex = games.size();
            }

            List<Game> games1 = games.subList(minIndex, maxIndex);
            pages.add(new Page(i, games1, last));
        }
    }

    public List<Page> getPages(){
        return pages;
    }

    public void update(int gameID){
        if(pages != null && pages.size() > 0) {
            if(GameManager.getInstance().getGameCount() > 0) {
                int gameIndex = GameManager.getInstance().getGames().indexOf(GameManager.getInstance().getGame(gameID));
                int pageIndex = ((int) Math.ceil(gameIndex / 27D));

                Page page = pages.get(pageIndex);

                page.updateGameItem(gameID);
            }
        }

    }

    public void addGame(){
        List<HumanEntity> humanEntities = new ArrayList<>();
        for(Page page: pages){
            humanEntities.addAll(page.getInventory().getViewers());
        }
        setup();
        Page page = pages.get(0);
        if(page != null) {
            for (HumanEntity humanEntity : humanEntities) {
                humanEntity.openInventory(page.getInventory());
            }
        }
    }

    public void removeGame(){
        setup();
    }

    public int calcNumberOfPages(){
        return (int)Math.ceil(GameManager.getInstance().getGameCount()/27D);
    }

    public ItemStack getMenuItemStack(){
        ItemStack itemStack = new ItemStack(Material.SLIME_BALL);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(MessageManager.getInstance().getFValue("menu.item.open.title"));
        List<String> lore = new ArrayList<>();
        lore.add(MessageManager.getInstance().getFValue("menu.item.open.description"));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
