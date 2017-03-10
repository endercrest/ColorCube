package com.endercrest.colorcube.events;

import com.endercrest.colorcube.*;
import com.endercrest.colorcube.api.PlayerPowerupEvent;
import com.endercrest.colorcube.powerups.SubPowerup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerRightClickListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(GameManager.getInstance().isPlayerActive(player)){
                try {
                    SubPowerup powerup = PowerupManager.getInstance().getPowerup(player.getItemInHand());
                    PlayerPowerupEvent pe = new PlayerPowerupEvent(event.getPlayer(), GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(player)), PowerupManager.getInstance().getPowerupId(powerup));
                    Bukkit.getServer().getPluginManager().callEvent(pe);
                    powerup.onRightClick(player, GameManager.getInstance().getGame(GameManager.getInstance().getActivePlayerGameID(player)));
                    event.setCancelled(true);
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
                    player.updateInventory();
                }catch(NullPointerException e){}
            }

            if(event.getItem() != null) {
                if (event.getItem().equals(MenuManager.getInstance().getMenuItemStack())) {
                    if (player.hasPermission("cc.lobby.menu")) {
                        player.openInventory(MenuManager.getInstance().getPages().get(0).getInventory());
                    } else {
                        MessageManager.getInstance().sendFMessage("error.nopermission", player);
                    }
                }
            }
        }
    }
}
