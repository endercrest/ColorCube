package com.endercrest.colorcube.logging;

import com.endercrest.colorcube.*;
import com.endercrest.colorcube.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {

    public static QueueManager instance = new QueueManager();
    private ConcurrentHashMap<Integer, ArrayList<BlockData>> queue = new ConcurrentHashMap<Integer, ArrayList<BlockData>>();
    File baseDir;

    private ColorCube plugin;

    public static QueueManager getInstance(){
        return instance;
    }

    public void setup(ColorCube plugin){
        this.plugin = plugin;
        baseDir = new File(plugin.getDataFolder()+"/ArenaData/");
        try{
            if(!baseDir.exists()){
                baseDir.mkdirs();
            }
            for(Game g : GameManager.getInstance().getGames()){
                ensureFile(g.getId());
            }

        }catch(Exception e){}

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new DataDumper(), 100, 100);
        MessageManager.getInstance().debugConsole("&eQueue Manager Set up");
    }



    public void rollback(final int id, final boolean shutdown){
        loadSave(id);
        if(!shutdown){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    new Rollback(id, shutdown,0,1,0));
        }
        else{
            new Rollback(id, shutdown,0,1,0).run();
        }

        if(shutdown){
            new RemoveEntities(id);
        }
        else{
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    new RemoveEntities(id), 5);
        }//


    }

    class RemoveEntities implements Runnable{
        private int id;

        protected RemoveEntities(int id){
            this.id = id;
        }

        public void run(){
            ArrayList<Entity>removelist = new ArrayList<Entity>();

            for(Entity e: SettingsManager.getInstance().getGameWorld(id).getEntities()){
                if((!(e instanceof Player)) && (!(e instanceof HumanEntity))){
                    if(GameManager.getInstance().getBlockGameId(e.getLocation()) == id){
                        removelist.add(e);
                    }
                }
            }
            for(int a = 0; a < removelist.size(); a = 0){
                try{removelist.remove(0).remove();}catch(Exception e){}
            }
        }
    }


    public void add(BlockData data){
        ArrayList<BlockData>dat = queue.get(data.getGameId());
        if(dat == null){
            dat = new ArrayList<BlockData>();
            ensureFile(data.getGameId());
        }
        dat.add(data);
        queue.put(data.getGameId(), dat);

    }

    public void ensureFile(int id){
        try{
            File f2 = new File(baseDir, "Arena"+id+".dat");
            if(!f2.exists()){
                f2.createNewFile();
            }
        }catch(Exception e){}
    }

    class DataDumper implements Runnable{
        public void run(){
            for(int id: queue.keySet()){
                try{

                    ArrayList<BlockData>data = queue.get(id);
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(baseDir, "Arena"+id+".dat")));

                    out.writeObject(data);
                    out.flush();
                    out.close();

                }catch(Exception e){}
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSave(int id){
        ensureFile(id);
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(baseDir, "Arena"+id+".dat")));

            ArrayList<BlockData>input = (ArrayList<BlockData>) in.readObject();

            ArrayList<BlockData>data = queue.get(id);
            if(data == null){
                data = new ArrayList<BlockData>();
            }

            for(BlockData d:input){
                if(!data.contains(d)){
                    data.add(d);
                }
            }

            queue.put(id, data);
            in.close();
        }catch(Exception e){}
    }






    class Rollback implements Runnable{

        int id, totalRollback, iteration;
        Game game;
        long time;

        boolean shutdown;

        public Rollback(int id, boolean shutdown, int trb, int it, long time){
            this.id = id;
            this.totalRollback = trb;
            this.iteration = it;
            this.time = time;
            game = GameManager.getInstance().getGame(id);
            this.shutdown = shutdown;
        }



        public void run(){
            MessageManager.getInstance().debugConsole("Starting Arena restoration");
            ArrayList<BlockData> data = queue.get(id);
            if(data != null){
                int a = data.size()-1;
                int rb = 0;
                long t1 = new Date().getTime();
                int pt = SettingsManager.getInstance().getPluginConfig().getInt("rollback.per-tick", 100);
                while(a>=0 && (rb < pt|| shutdown)){
                    MessageManager.getInstance().debugConsole("Reseting " + a);
                    BlockData result = data.get(a);
                    if(result.getGameId() == game.getId()){

                        data.remove(a);
                        Location l = new Location(Bukkit.getWorld(result.getWorld()), result.getX(), result.getY(), result.getZ());
                        Block b = l.getBlock();
                        b.setTypeIdAndData(result.getPrevid(), result.getPrevdata(), false);
                        b.getState().update();

						/*	if(result.getItems() != null){
							Chest c = (Chest)b;
							c.getBlockInventory().setContents(result.getItems());
						}
						 */

                        rb++;

                    }
                    a--;
                }
                time += new Date().getTime() - t1;
                if(a != -1){
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                            new Rollback(id, shutdown, totalRollback + rb, iteration+1, time), 1);
                }
                else{
                    MessageManager.getInstance().log("Arena "+id+" reset. Rolled back "+(totalRollback+rb)+" blocks in "+iteration+" iterations ("+pt+" blocks per iteration Total time spent rolling back was "+time+"ms)");
                    game.resetCallback();
                }
            }else{
                MessageManager.getInstance().log("Arena "+id+" reset. Rolled back "+totalRollback+" blocks in "+iteration+" iterations. Total time spent rolling back was "+time+"ms");
                game.resetCallback();
            }
        }


    }
}
