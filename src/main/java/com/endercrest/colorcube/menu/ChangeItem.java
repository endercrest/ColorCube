package com.endercrest.colorcube.menu;

import com.endercrest.colorcube.MenuManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChangeItem extends PageItem {

    private boolean next;

    public ChangeItem(Page page, int itemIndex, boolean next) {
        super(page, itemIndex);
        this.next = next;
    }

    @Override
    public void onClick(Player player) {
        Page page = null;
        if(next) {
            if(getPage().getPageNum()+1 < MenuManager.getInstance().getPages().size()) {
                page = MenuManager.getInstance().getPages().get(getPage().getPageNum()+1);
            }
        }else{
            if(getPage().getPageNum()-1 >= 0) {
                page = MenuManager.getInstance().getPages().get(getPage().getPageNum()-1);
            }
        }

        if(page != null){
            player.openInventory(page.getInventory());
        }
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)3);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(next){
            itemMeta.setDisplayName(ChatColor.BLUE+"Next Page -->");
        }else{
            itemMeta.setDisplayName(ChatColor.BLUE+"<-- Back Page");
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
