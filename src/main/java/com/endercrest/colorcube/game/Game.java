package com.endercrest.colorcube.game;

import com.endercrest.colorcube.ColorCube;
import com.endercrest.colorcube.GameManager;
import com.endercrest.colorcube.MessageManager;
import com.endercrest.colorcube.SettingsManager;
import com.endercrest.colorcube.api.PlayerJoinArenaEvent;
import com.endercrest.colorcube.api.PlayerLeaveArenaEvent;
import com.endercrest.colorcube.api.TeamWinEvent;
import com.endercrest.colorcube.logging.LoggingManager;
import com.endercrest.colorcube.logging.QueueManager;
import com.endercrest.colorcube.utils.ParticleEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.*;

public class Game {

    public static enum Status{
        DISABLED, LOADING, IDLE, LOBBY,
        STARTING, INGAME, FINISHING, RESETING, ERROR
    }

    private Status status = Status.DISABLED;
    private List<Player> activePlayers = new ArrayList<Player>();
    private List<Player> spectators = new ArrayList<Player>();
    private ArrayList<Integer>tasks = new ArrayList<Integer>();
    private List<Powerup> powerups = new ArrayList<Powerup>();

    private Arena arena;
    private Lobby lobby;
    private Integer id;
    private FileConfiguration config;
    private FileConfiguration system;
    private HashMap<Integer, Player> spawns = new HashMap<Integer, Player>();
    private HashMap<Player, ItemStack[][]> inventory_store = new HashMap<Player, ItemStack[][]>();
    private int spawnCount = 0;
    private boolean disabled = false;
    private int endgameTaskID = 0;
    private boolean endgameRunning = false;
    private boolean countdownRunning;
    private boolean timerRunning;
    private int timerTaskID = 0;
    private int particleTaskID = 0;
    private HashMap<String, String> hookvars = new HashMap<String, String>();
    private MessageManager msg = MessageManager.getInstance();

    Team red;
    Team blue;
    Team green;
    Team yellow;

    private int redScore = 0;
    private int blueScore = 0;
    private int greenScore = 0;
    private int yellowScore = 0;

    private ScoreboardManager manager;
    private Scoreboard board;
    private Objective objective;
    private Score timerScore;

    public Game(Integer id){
        this.id = id;
        reloadConfig();
        setup();
    }

    public void reloadConfig(){
        config = SettingsManager.getInstance().getPluginConfig();
        system = SettingsManager.getInstance().getSystemConfig();
    }

    public void setup(){
        status = Status.LOADING;

        int x = system.getInt("arenas." + id + ".x1");
        int y = system.getInt("arenas." + id + ".y1");
        int z = system.getInt("arenas." + id + ".z1");

        int x1 = system.getInt("arenas." + id + ".x2");
        int y1 = system.getInt("arenas." + id + ".y2");
        int z1 = system.getInt("arenas." + id + ".z2");

        Location pos1 = new Location(SettingsManager.getInstance().getGameWorld(id), Math.max(x, x1), Math.max(y, y1), Math.max(z, z1));//max
        Location pos2 = new Location(SettingsManager.getInstance().getGameWorld(id), Math.min(x, x1), Math.min(y, y1), Math.min(z, z1));//min

        arena = new Arena(pos1, pos2);

        int lx = system.getInt("arenas." + id + ".lx1");
        int ly = system.getInt("arenas." + id + ".ly1");
        int lz = system.getInt("arenas." + id + ".lz1");

        int lx1 = system.getInt("arenas." + id + ".lx2");
        int ly1 = system.getInt("arenas." + id + ".ly2");
        int lz1 = system.getInt("arenas." + id + ".lz2");

        Location lpos1 = new Location(SettingsManager.getInstance().getLobbyWorld(id), Math.max(lx, lx1), Math.max(ly, ly1), Math.max(lz, lz1));//max
        Location lpos2 = new Location(SettingsManager.getInstance().getLobbyWorld(id), Math.min(lx, lx1), Math.min(ly, ly1), Math.min(lz, lz1));//min

        if(lx != 0 && ly != 0 && lz != 0) {
            lobby = new Lobby(lpos1, lpos2);
            msg.debugConsole("Loaded Lobby for Arena:" + id);
        }else{
            lobby = null;
            msg.debugConsole("Could not load Arena " + id + " lobby");
            msg.debugConsole("&cX:" + lx + " Y:" + ly + " Z:" + lz + "X1:" + lx1 + " Y1:" + ly1 + " Z1:" + lz1);
        }

        if(lobby != null){
            lobby.loadSpawn(id);
            msg.debugConsole("Loading Lobby Spawn for Arena:" + id);
        }

        loadspawns();

        hookvars.put("arena", id + "");
        hookvars.put("maxplayers", spawnCount + "");
        hookvars.put("activeplayers", "0");

        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        red = board.registerNewTeam("red" + id);
        blue = board.registerNewTeam("blue" + id);
        green = board.registerNewTeam("green" + id);
        yellow = board.registerNewTeam("yellow" + id);
        red.setPrefix(ChatColor.RED + "");
        blue.setPrefix(ChatColor.AQUA + "");
        green.setPrefix(ChatColor.GREEN + "");
        yellow.setPrefix(ChatColor.YELLOW + "");

        objective = board.registerNewObjective("Arena" + id, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(MessageManager.getInstance().colorize("&6Arena " + id));
        timerScore = objective.getScore("Time");

        status = Status.LOBBY;
    }

    public void addSpawn() {
        spawnCount++;
        spawns.put(spawnCount, null);
    }

    public void loadspawns(){
        for(int a = 1; a <= SettingsManager.getInstance().getSpawnCount(id); a++){
            spawns.put(a, null);
            spawnCount = a;
            MessageManager.getInstance().debugConsole("Spawn:" + a + " loaded");
        }
    }

    public int getGameID(){
        return id;
    }

    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }

    public boolean isLobbySet(){
        return lobby != null;
    }

    public Lobby getLobby(){
        return lobby;
    }

    ///////////////////////////////////
    ///           Enable            ///
    ///////////////////////////////////
    public void enable(){
        status = Status.LOBBY;
        disabled = false;
        MessageManager.getInstance().debugConsole("Arena " + id + " enabled");
    }

    ///////////////////////////////////
    ///          Disable            ///
    ///////////////////////////////////
    public void disable(){
        disabled = true;
        spawns.clear();

        for (Player p : activePlayers) {
            try {
                removePlayer(p, false);
                MessageManager.getInstance().sendFMessage("game.status", p, "state-disabled");
            } catch (Exception e) {}
        }
        endGame();
        MessageManager.getInstance().debugConsole("Arena " + id + " disabled");
    }

    ///////////////////////////////////
    ///           Join              ///
    ///////////////////////////////////

    public boolean addPlayer(Player p){
        if(!p.hasPermission("cc.arena.join."+ id) || !p.hasPermission("cc.arena.join.*")){
            msg.debugConsole("Need cc.arena.join." + id + "or cc.arena.join.*");
            msg.sendFMessage("error.nopermission", p);
            return false;
        }
        if(lobby == null){
            MessageManager.getInstance().sendFMessage("error.nolobby", p);
            return false;
        }
        if(!lobby.isSpawnSet()){
            MessageManager.getInstance().sendFMessage("error.nolobbyspawn", p, "arena-" + id);
            return false;
        }
        if(SettingsManager.getInstance().getGlobalLobbySpawn() == null){
            MessageManager.getInstance().sendFMessage("error.nomainlobby", p);
            return false;
        }
        if (GameManager.getInstance().getPlayerGameID(p) != -1) {
            if (GameManager.getInstance().isPlayerActive(p)) {
                MessageManager.getInstance().sendFMessage("game.joinmutliple", p);
                return false;
            }
        }
        if(p.isInsideVehicle()){
            p.leaveVehicle();
        }
        //if(isSpectator(p))
          //  removeSpectator(p);

        if(status == Status.LOBBY || status == Status.STARTING) {
            if (activePlayers.size() < SettingsManager.getInstance().getSpawnCount(id)) {
                msg.sendFMessage("game.join", p, "arena-" + id);
                PlayerJoinArenaEvent joinarena = new PlayerJoinArenaEvent(p, this);
                Bukkit.getServer().getPluginManager().callEvent(joinarena);
                p.setGameMode(GameMode.SURVIVAL);
                p.setFallDistance(0);
                p.teleport(lobby.getSpawn());
                saveInv(p);
                clearInv(p);
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                clearInv(p);
                p.setScoreboard(board);

                activePlayers.add(p);
                addToTeam(p);
                msgFArena("game.team", "team-" + getTeamName(p), "player-" + p.getDisplayName());
                if (spawnCount == activePlayers.size()) {
                    countdown(5);
                }
            } else if (SettingsManager.getInstance().getSpawnCount(id) == 0) {
                msg.sendFMessage("game.nospawns", p, "arena-" + id);
            } else {
                msg.sendFMessage("error.gamefull", p, "arena-" + id);
            }
            msgFArena("game.playerjoingame", "player-" + p.getName(), "activeplayers-" + activePlayers.size(), "maxplayers-" + spawnCount);
            //TODO Auto Start
            return true;
        }
        if(status == Status.INGAME)
            msg.sendFMessage("error.alreadyingame", p);
        else if(status == Status.DISABLED)
            msg.sendFMessage("error.gamedisabled", p, "arena-" + id);
        else if(status == Status.RESETING)
            msg.sendFMessage("error.gameresetting", p);
        else
            msg.sendFMessage("error.joinfail", p);
        return false;
    }

    ///////////////////////////////////
    ///         Start Game          ///
    ///////////////////////////////////
    public void startGame(){
        if(status == Status.INGAME){
            return;
        }
        if(activePlayers.size() <= 0){
            msgArena("error.noenoughplayers");
            status = Status.LOBBY;
            return;
        }else{
            for(Player p: activePlayers){
                for(int i = 1; i <= spawnCount; i++){
                    if(spawns.get(i) == null){
                        spawns.put(i, p);
                        p.teleport(SettingsManager.getInstance().getSpawnPoint(id, i));
                        clearInv(p);
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setHealth(p.getMaxHealth());
                        p.setFoodLevel(20);
                        msg.sendFMessage("game.goodluck", p);
                        break;
                    }
                }
            }
        }
        status = Status.INGAME;
        timerTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ColorCube.getPlugin(), new GameTimer(), 0, 20);
        particleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(ColorCube.getPlugin(), new ParticleTimer(), 0, 5);
        tasks.add(timerTaskID);
        tasks.add(particleTaskID);
        timerRunning = true;
        MessageManager.getInstance().broadcastFMessage("broadcast.gamestarted", "arena-" + id);
    }

    ///////////////////////////////////
    ///         CountDown           ///
    ///////////////////////////////////
    public int getCountdownTime(){
        return count;
    }

    int count = 20;
    int tid = 0;
    public void countdown(int time){
        MessageManager.getInstance().broadcastFMessage("broadcast.gamestarting", "arena-" + id, "t-" + time);
        countdownRunning = true;
        count = time;
        Bukkit.getScheduler().cancelTask(tid);

        if(status == Status.LOBBY || status == Status.STARTING){
            status = Status.STARTING;
            tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(ColorCube.getPlugin(), new Runnable() {
                public void run() {
                    if (count > 0) {
                        if (count % 10 == 0) {
                            msgFArena("game.countdown","t-"+count);
                        }
                        if (count < 6) {
                            msgFArena("game.countdown","t-"+count);
                        }
                        count--;
                    } else {
                        startGame();
                        Bukkit.getScheduler().cancelTask(tid);
                        countdownRunning = false;
                    }
                }
            }, 0, 20);
        }
    }

    ///////////////////////////////////
    ///       Remove Player         ///
    ///////////////////////////////////
    public void removePlayer(Player player, boolean b){
        player.teleport(SettingsManager.getInstance().getGlobalLobbySpawn());
        restoreInv(player);
        activePlayers.remove(player);
        msgFArena("game.playerleave", "player-" + player.getDisplayName());
        if(activePlayers.size() == 1){
            msgFArena("game.end", "reason-Not enough players");
            endGame();
        }
        player.setScoreboard(manager.getNewScoreboard());
        for (Object in : spawns.keySet().toArray()) {
            if (spawns.get(in) == player) spawns.remove(in);
        }

        PlayerLeaveArenaEvent pl = new PlayerLeaveArenaEvent(player, this, b);
    }

    ///////////////////////////////////
    ///        Reset Arena          ///
    ///////////////////////////////////
    public void resetArena(){
        for(Integer i: tasks){
            Bukkit.getScheduler().cancelTask(i);
        }
        tasks.clear();

        powerups.clear();

        MessageManager.getInstance().debugConsole("Resetting Player information in arena " + id);
        activePlayers.clear();
        Set<OfflinePlayer> redPlayers = red.getPlayers();
        for(OfflinePlayer player: redPlayers){
            red.removePlayer(player);
        }
        Set<OfflinePlayer> bluePlayers = blue.getPlayers();
        for(OfflinePlayer player: bluePlayers){
            blue.removePlayer(player);
        }
        Set<OfflinePlayer> greenPlayers = green.getPlayers();
        for(OfflinePlayer player: greenPlayers){
            green.removePlayer(player);
        }
        Set<OfflinePlayer> yellowPlayers = yellow.getPlayers();
        for(OfflinePlayer player: yellowPlayers){
            yellow.removePlayer(player);
        }
        redScore = 0;
        blueScore = 0;
        yellowScore = 0;
        greenScore = 0;

        board.resetScores("Time");

        status = Status.RESETING;
        endgameRunning = false;
        timerRunning = false;
        spawns.clear();

        Bukkit.getScheduler().cancelTask(timerTaskID);
        Bukkit.getScheduler().cancelTask(endgameTaskID);
        Bukkit.getScheduler().cancelTask(particleTaskID);
        QueueManager.getInstance().rollback(id, false);
    }
    ///////////////////////////////////
    ///          Win Game           ///
    ///////////////////////////////////
    public void winGame(){
        if(status == Status.INGAME) {
            String team = scoreResults();
            TeamWinEvent tw = new TeamWinEvent(getTeam(team).getPlayers(), team);
            MessageManager.getInstance().broadcastFMessage("broadcast.gamewin", "team-" + team, "arena-" + id);
        }
    }

    ///////////////////////////////////
    ///          End Game           ///
    ///////////////////////////////////
    public void endGame() {
        winGame();
        for(Player p : activePlayers){
            p.setScoreboard(manager.getNewScoreboard());
            p.teleport(SettingsManager.getInstance().getGlobalLobbySpawn());
            restoreInv(p);
        }
        status = Status.FINISHING;
        resetArena();
        status = Status.LOBBY;
    }

    public void addToTeam(Player player){
        if(red.getSize() <= blue.getSize()){
            red.addPlayer(player);
            return;
        }
        if(blue.getSize() <= green.getSize()){
            blue.addPlayer(player);
            return;
        }
        if(green.getSize() <= yellow.getSize()){
            green.addPlayer(player);
            return;
        }
        yellow.addPlayer(player);
    }

    public Team getTeam(Player player){
        if(red.getPlayers().contains(player)){
            return red;
        }
        if(blue.getPlayers().contains(player)){
            return blue;
        }
        if(green.getPlayers().contains(player)){
            return green;
        }
        if(yellow.getPlayers().contains(player)){
            return yellow;
        }
        return null;
    }

    public Team getTeam(String teamName){
        if(SettingsManager.getInstance().getMessagesConfig().getString("messages.color.red").equalsIgnoreCase(teamName)){
            return red;
        }else if(SettingsManager.getInstance().getMessagesConfig().getString("messages.color.blue").equalsIgnoreCase(teamName)){
            return blue;
        }else if(SettingsManager.getInstance().getMessagesConfig().getString("messages.color.green").equalsIgnoreCase(teamName)){
            return green;
        }else if(SettingsManager.getInstance().getMessagesConfig().getString("messages.color.yellow").equalsIgnoreCase(teamName)){
            return yellow;
        }
        return null;
    }

    public String getTeamName(Player player){
        if(red.getPlayers().contains(player)){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.red");
        }
        if(blue.getPlayers().contains(player)){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.blue");
        }
        if(green.getPlayers().contains(player)){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.green");
        }
        if(yellow.getPlayers().contains(player)){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.yellow");
        }
        return null;
    }

    public int getTeamID(Player player){
        if(red.getPlayers().contains(player)){
            return 0;
        }
        if(blue.getPlayers().contains(player)){
            return 1;
        }
        if(green.getPlayers().contains(player)){
            return 2;
        }
        if(yellow.getPlayers().contains(player)){
            return 3;
        }
        return -1;
    }

    public void increaseScore(int team, int amount){
        if(team == 0){
            redScore += amount;
        }else if(team == 1){
            blueScore += amount;
        }else if(team == 2){
            greenScore += amount;
        }else if(team == 3){
            yellowScore += amount;
        }
    }

    public void decreaseScore(int team, int amount){
        if(team == 0){
            redScore -= amount;
        }else if(team == 1){
            blueScore -= amount;
        }else if(team == 2){
            greenScore -= amount;
        }else if(team == 3){
            yellowScore -= amount;
        }
    }

    public String scoreResults(){
        if(redScore > blueScore && redScore > greenScore && redScore > yellowScore){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.red");
        }else if(blueScore > redScore && blueScore > greenScore && blueScore > yellowScore){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.blue");
        }else if(greenScore > redScore && greenScore > blueScore && greenScore > yellowScore){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.green");
        }else if(yellowScore > redScore && yellowScore > blueScore && yellowScore > greenScore){
            return SettingsManager.getInstance().getMessagesConfig().getString("messages.color.yellow");
        }else{
           return "None";
        }
    }

    public byte getTeamBlockByte(int teamId){
        switch(teamId){
            case 0:
                return 14;
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 4;
            default:
                return 0;
        }
    }

    public boolean isBlockInArena(Location v) {
        return arena.containsBlock(v);
    }

    public boolean isBlockInLobby(Location v) {
        if(lobby != null) {
            return lobby.containsBlock(v);
        }else{
            return false;
        }
    }

    public void resetCallback() {
        if (!disabled){
            enable();
        }
        else status = Status.DISABLED;
    }

    class GameTimer implements Runnable {
        int counter = SettingsManager.getInstance().getPluginConfig().getInt("game-length", 600);
        int powerupDefault = SettingsManager.getInstance().getPluginConfig().getInt("powerup-freq", 15);
        int powerup = powerupDefault;
        @Override
        public void run() {
            if(counter > 0){
                --counter;
                timerScore.setScore(counter);
            }else{
                endGame();
            }

            if(counter <= 10){
                msgFArena("game.time", "time-" + counter);
            }
            Random random = new Random();
            //int randomNum = random.nextInt((50 - 1) + 1) + 1;

            if(powerup == 0){
                double x;
                double y;
                double z;
                boolean finish = true;
                while(finish) {
                    x = random.nextInt((arena.getPos1().getBlockX() - arena.getPos2().getBlockX()) + 1) + arena.getPos2().getBlockX() + 0.5;
                    y = random.nextInt((arena.getPos1().getBlockY() - arena.getPos2().getBlockY()) + 1) + arena.getPos2().getBlockY();
                    z = random.nextInt((arena.getPos1().getBlockZ() - arena.getPos2().getBlockZ()) + 1) + arena.getPos2().getBlockZ() + 0.5;
                    Location loc = new Location(arena.getPos1().getWorld(), x, y, z);
                    Location loc2 = loc.subtract(0, 1, 0);
                    if(loc2.getBlock().getType() == Material.STAINED_CLAY){
                        createPowerup(loc, true);
                        finish = false;
                    }
                }
                powerup = powerupDefault;
            }else{
                --powerup;
            }
        }
    }

    class ParticleTimer implements Runnable {
        @Override
        public void run() {
            if (powerups.size() > 0) {
                for (Powerup pu : powerups) {
                    for (Player p : activePlayers) {
                        ParticleEffect.NOTE.display(0.2f, 0.5f, 0.2f, 1, 10, pu.getLocation(), activePlayers);
                    }
                }
            }
        }
    }

    public Powerup createPowerup(Location location, boolean spawn){
        Powerup pu = new Powerup(location.add(0,1,0));
        if(spawn) {
            powerups.add(pu);
            msgFArena("game.powerup");
        }
        return pu;
    }

    public Powerup createPowerup(Location location, int type, boolean spawn){
        Powerup pu = new Powerup(location.add(0,1,0), type);
        if(spawn) {
            powerups.add(pu);
            msgFArena("game.powerup");
        }
        return pu;
    }

    public void changeBlock(Location loc, int team) {
        if(loc.getBlock().getType().equals(Material.STAINED_CLAY)) {
            byte data;
            switch (team) {
                case 0://Red Team
                    data = 14;
                    break;
                case 1://Blue Team
                    data = 3;
                    break;
                case 2://Green Team
                    data = 5;
                    break;
                case 3://Yellow Team
                    data = 4;
                    break;
                default:
                    data = 0;
                    break;
            }
            if (loc.getBlock().getData() != data) {
                switch (loc.getBlock().getData()) {
                    case 14:
                        scoreManagement(id, team, 0, 1);
                        break;
                    case 3:
                        scoreManagement(id, team, 1, 1);
                        break;
                    case 5:
                        scoreManagement(id, team, 2, 1);
                        break;
                    case 4:
                        scoreManagement(id, team, 3, 1);
                        break;
                    case 0:
                        scoreManagement(id, team, -1, 1);
                        break;
                }
                LoggingManager.getInstance().logBlockDestoryed(loc.getBlock());
                loc.getBlock().setData(data);
            }
        }
    }

    public void scoreManagement(int id, int teamincrease, int teamdecrease, int amount){
        Game game = GameManager.getInstance().getGame(id);
        game.increaseScore(teamincrease, amount);
        game.decreaseScore(teamdecrease, amount);
    }

    public List<Powerup> getPowerups() {
        return powerups;
    }

    public void removePowerup(Powerup powerup){
        powerups.remove(powerup);
    }

    public void setLobbySpawn(int id, Location loc){
        lobby.setSpawn(id, loc);
    }

    public boolean isPlayerActive(Player player) {
        return activePlayers.contains(player);
    }

    public boolean isSpectator(Player player){
        return spectators.contains(player);
    }

    public Status getStatus() {
        return status;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }


    public List<Player> getSpectators() {
        return spectators;
    }

    public ArrayList<Integer> getTasks() {
        return tasks;
    }

    public Arena getArena() {
        return arena;
    }

    public Integer getId() {
        return id;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getSystem() {
        return system;
    }

    public HashMap<Integer, Player> getSpawns() {
        return spawns;
    }

    public HashMap<Player, ItemStack[][]> getInventory_store() {
        return inventory_store;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public int getEndgameTaskID() {
        return endgameTaskID;
    }

    public boolean isEndgameRunning() {
        return endgameRunning;
    }

    public boolean isCountdownRunning() {
        return countdownRunning;
    }

    public HashMap<String, String> getHookvars() {
        return hookvars;
    }

    public ArrayList < Player > getAllPlayers() {
        ArrayList < Player > all = new ArrayList < Player > ();
        all.addAll(activePlayers);
        return all;
    }

    public void saveInv(Player p) {
        ItemStack[][] store = new ItemStack[2][1];

        store[0] = p.getInventory().getContents();
        store[1] = p.getInventory().getArmorContents();

        inventory_store.put(p, store);

    }

    public void restoreInv(Player p) {
        try {
            clearInv(p);
            p.getInventory().setContents(inventory_store.get(p)[0]);
            p.getInventory().setArmorContents(inventory_store.get(p)[1]);
            inventory_store.remove(p);
            p.updateInventory();
        } catch (Exception e) { /*p.sendMessage(ChatColor.RED+"Inentory failed to restore or nothing was in it.");*/
        }
    }

    public void clearInv(Player p) {
        ItemStack[] inv = p.getInventory().getContents();
        for (int i = 0; i < inv.length; i++) {
            inv[i] = null;
        }
        p.getInventory().setContents(inv);
        inv = p.getInventory().getArmorContents();
        for (int i = 0; i < inv.length; i++) {
            inv[i] = null;
        }
        p.getInventory().setArmorContents(inv);
        p.updateInventory();
    }

    public void msgFArena(String string, String...args){
        for(Player p: getAllPlayers()){
            msg.sendFMessage(string, p, args);
        }
    }

    public void msgArena(String string){
        for(Player p: getAllPlayers()){
            msg.sendMessage(string, p);
        }
    }
}
