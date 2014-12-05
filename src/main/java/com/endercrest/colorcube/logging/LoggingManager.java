package com.endercrest.colorcube.logging;

import com.endercrest.colorcube.game.Game;
import com.endercrest.colorcube.GameManager;
import org.bukkit.block.Block;

public class LoggingManager {

    public static LoggingManager instance = new LoggingManager();

    public static LoggingManager getInstance(){
        return instance;
    }

    public void logBlockCreated(Block b){
        if(GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
            return;
        if( GameManager.getInstance().getStatus(GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.Status.DISABLED)
            return ;

        QueueManager.getInstance().add(
                new BlockData(
                        GameManager.getInstance().getBlockGameId(b.getLocation()),
                        b.getWorld().getName(),
                        0,
                        (byte)0,
                        b.getTypeId(),
                        b.getData(),
                        b.getX(),
                        b.getY(),
                        b.getZ(),
                        null)
        );
    }


    public void logBlockDestoryed(Block b){
        if(GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
            return;
        if( GameManager.getInstance().getStatus(GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.Status.DISABLED)
            return ;
        if(b.getTypeId() == 51)
            return;
        QueueManager.getInstance().add(
                new BlockData(
                        GameManager.getInstance().getBlockGameId(b.getLocation()),
                        b.getWorld().getName(),
                        b.getTypeId(),
                        b.getData(),
                        0,
                        (byte)0,
                        b.getX(),
                        b.getY(),
                        b.getZ(),
                        null)
        );
    }
}
