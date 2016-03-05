package com.endercrest.colorcube.menu;

import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MenuManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;

public class Page implements InventoryHolder {

    private Inventory inventory;

    private HashMap<Integer, PageItem> items;
    private int pageNum;
    private List<Game> gamesList;
    private boolean last;

    /**
     * Constructor for Page
     * @param pageNum The Page num for this specific page.
     * @param gameList The list of games that will be displayed on this page.
     * @param last Whether this is the last page.
     */
    public Page(int pageNum, List<Game> gameList, boolean last){
        this.pageNum = pageNum;
        this.gamesList = gameList;
        this.last = last;
        items = new HashMap<>();

        createPage(pageNum, gameList, last);
    }

    private void createPage(int pageNum, List<Game> gamesList, boolean last){
        int size = (int)Math.ceil(gamesList.size()/9D)*9;
        inventory = Bukkit.createInventory(this, size+(2*9), MessageManager.getInstance().getFValue("menu.title", "page-"+(pageNum+1), "maxpage-"+ MenuManager.getInstance().calcNumberOfPages()));


        int index = 0;
        for(Game game: gamesList){
            GameItem gameItem = new GameItem(this, index, game);

            inventory.setItem(index, gameItem.getItemStack());

            items.put(index, gameItem);
            index++;
        }

        CloseItem closeItem = new CloseItem(this, getInventory().getSize()-5);
        items.put(closeItem.getItemIndex(), closeItem);
        getInventory().setItem(closeItem.getItemIndex(), closeItem.getItemStack());

        if(pageNum > 0) {
            ChangeItem backItem = new ChangeItem(this, getInventory().getSize() - 9, false);
            items.put(backItem.getItemIndex(), backItem);
            getInventory().setItem(backItem.getItemIndex(), backItem.getItemStack());
        }

        if(!last) {
            ChangeItem nextItem = new ChangeItem(this, getInventory().getSize() - 1, true);
            items.put(nextItem.getItemIndex(), nextItem);
            getInventory().setItem(nextItem.getItemIndex(), nextItem.getItemStack());
        }
    }

    public int getPageNum(){
        return pageNum;
    }

    public PageItem getPageItem(int index){
        return items.get(index);
    }

    public void updateGameItem(int gameID){
        for(Integer integer: items.keySet()){
            PageItem pageItem = items.get(integer);
            if(pageItem instanceof GameItem){
                GameItem gameItem = (GameItem) pageItem;
                if(gameItem.getGame().getId() == gameID){
                    inventory.setItem(integer, gameItem.getItemStack());
                    return;
                }
            }
        }
    }

    public List<Game> getGamesList(){
        return gamesList;
    }

    public void addGame(Game game){
        gamesList.add(game);
    }

    public void setLast(boolean last){
        this.last = last;
    }

    public void reconstructMenu(){
        items = new HashMap<>();
        createPage(pageNum, gamesList, last);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
