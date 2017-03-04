package com.endercrest.colorcube.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PageItem {

    private Page page;
    private int itemIndex;

    public PageItem(Page page, int itemIndex){
        this.page = page;
        this.itemIndex = itemIndex;
    }

    public Page getPage(){
        return page;
    }

    public int getItemIndex(){
        return itemIndex;
    }

    public abstract void onClick(Player player);

    public abstract ItemStack getItemStack();
}
