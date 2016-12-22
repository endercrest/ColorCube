package com.endercrest.colorcube.game;

import com.endercrest.colorcube.*;
import com.endercrest.colorcube.api.PlayerJoinArenaEvent;
import com.endercrest.colorcube.api.PlayerLeaveArenaEvent;
import com.endercrest.colorcube.api.TeamWinEvent;
import com.endercrest.colorcube.logging.LoggingManager;
import com.endercrest.colorcube.logging.QueueManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;
import sun.security.krb5.Config;

import java.util.*;

/**
 * Created by Thomas Cordua-von Specht on 12/18/2016.
 *
 * This class contains almost all of the logic behind each game. It contains the players, the game status and all
 * the teams information. Using the game manager to create a new game will make a new game available to all the other
 * systems in this plugin.
 */
@SuppressWarnings({"unused", "SameParameterValue", "WeakerAccess"})
public class Game {

    public enum Status{
        DISABLED, LOADING, IDLE, LOBBY,
        STARTING, INGAME, FINISHING, RESETING, ERROR
    }

    public enum CCTeam{
        RED(ChatColor.RED, 14),
        BLUE(ChatColor.BLUE, 11),
        GREEN(ChatColor.GREEN, 5),
        YELLOW(ChatColor.YELLOW, 4);

        private ChatColor color;
        private int blockData;

        CCTeam(ChatColor color, int blockData){
            this.color = color;
            this.blockData = blockData;
        }

        public ChatColor getColor(){
            return color;
        }

        public int getBlockData(){
            return blockData;
        }

    }

    private Status status = Status.DISABLED;
    private List<Player> activePlayers = new ArrayList<>();
    private ArrayList<Integer>tasks = new ArrayList<>();
    private List<Powerup> powerups = new ArrayList<>();

    private Arena arena;
    private Lobby lobby;
    private Integer id;
    private HashMap<Player, ItemStack[][]> inventory_store = new HashMap<>();
    private boolean disabled = false;
    private int endgameTaskID = 0;
    private boolean endgameRunning = false;
    private boolean countdownRunning;
    private int timerTaskID = 0;
    private int particleTaskID = 0;
    private MessageManager msg = MessageManager.getInstance();

    //Options
    private boolean pvp;
    private double reward;
    private int perTeam;
    private String name;

    private List<Player> voted = new ArrayList<>();

    private HashMap<CCTeam, Team> teams;
    private HashMap<CCTeam, Location> teamSpawns;
    private HashMap<CCTeam, Integer> teamScores;

    private List<Player> spectators = new ArrayList<>();
    private Team spectatorsTeam;

    private ScoreboardManager manager;
    private Scoreboard board;

    private BossBar timeBar;

    private ColorCube plugin;

    public Game(Integer id, ColorCube plugin){
        this.id = id;
        this.plugin = plugin;
        setup();
    }

    public void setup(){
        status = Status.LOADING;

        FileConfiguration arenaConfig = SettingsManager.getInstance().getArenaConfig(id);

        World world = Bukkit.getWorld(arenaConfig.getString("loc.world"));

        int x = arenaConfig.getInt("loc.pos1.x");
        int y = arenaConfig.getInt("loc.pos1.y");
        int z = arenaConfig.getInt("loc.pos1.z");

        int x1 = arenaConfig.getInt("loc.pos2.x");
        int y1 = arenaConfig.getInt("loc.pos2.y");
        int z1 = arenaConfig.getInt("loc.pos2.z");

        Location pos1 = new Location(world, Math.max(x, x1), Math.max(y, y1), Math.max(z, z1));//max
        Location pos2 = new Location(world, Math.min(x, x1), Math.min(y, y1), Math.min(z, z1));//min

        arena = new Arena(pos1, pos2);

        World lobbyWorld = Bukkit.getWorld(arenaConfig.getString("lobby.world"));

        int lx = arenaConfig.getInt("lobby.pos1.x");
        int ly = arenaConfig.getInt("lobby.pos1.y");
        int lz = arenaConfig.getInt("lobby.pos1.z");

        int lx1 = arenaConfig.getInt("lobby.pos2.x");
        int ly1 = arenaConfig.getInt("lobby.pos2.y");
        int lz1 = arenaConfig.getInt("lobby.pos2.z");

        Location lpos1 = new Location(lobbyWorld, Math.max(lx, lx1), Math.max(ly, ly1), Math.max(lz, lz1));//max
        Location lpos2 = new Location(lobbyWorld, Math.min(lx, lx1), Math.min(ly, ly1), Math.min(lz, lz1));//min

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

        //Load Options
        pvp = arenaConfig.getBoolean("options.pvp", false);
        reward = arenaConfig.getDouble("options.reward", 0.0);
        perTeam = arenaConfig.getInt("options.perteam", 1);
        name = arenaConfig.getString("options.name", "Arena " + id);

        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();

        //Setup Spawns & Team Information
        teams = new HashMap<>();
        teamScores = new HashMap<>();
        teamSpawns = new HashMap<>();

        ConfigurationSection spawnsSection = arenaConfig.getConfigurationSection("spawns");
        for(String team: spawnsSection.getKeys(false)){
            addTeam(team);
        }

        //Setup Spectators Teams
        spectatorsTeam = board.registerNewTeam("spectatorArena"+id);

        timeBar = Bukkit.createBossBar(ChatColor.GOLD + "Arena " + id, BarColor.WHITE, BarStyle.SOLID);

        status = Status.LOBBY;

        MenuManager.getInstance().addGame();
    }

    /**
     * Add a team to the arena or updates the teams spawn point.
     * @param team The team to be added or updated.
     */
    private void addTeam(String team){
        CCTeam ccTeam = CCTeam.valueOf(team.toUpperCase());
        YamlConfiguration config = SettingsManager.getInstance().getArenaConfig(getId());
        ConfigurationSection spawnsSection = config.getConfigurationSection("spawns");

        if(teams.get(ccTeam) == null) {
            Team boardTeam = board.registerNewTeam(team + "Arena" + id);
            boardTeam.setPrefix(ccTeam.getColor() + "");

            teams.put(ccTeam, boardTeam);
            teamScores.put(ccTeam, 0);
        }

        Location teamSpawn = new Location(getArena().getPos1().getWorld(), spawnsSection.getDouble(team+".x"), spawnsSection.getDouble(team+".y"),
                spawnsSection.getDouble(team+".z"), (float) spawnsSection.getDouble(team+".yaw"),
                (float) spawnsSection.getDouble(team+".pitch"));
        teamSpawns.put(ccTeam, teamSpawn);
        updateGameItems();
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
        updateGameItems();
    }

    ///////////////////////////////////
    ///          Disable            ///
    ///////////////////////////////////
    public void disable(Boolean shutdown){
        disabled = true;

        for (Player p : activePlayers) {
            //removePlayer(p, false);
            MessageManager.getInstance().sendFMessage("game.status", p, "state-disabled");
        }
        endGame(shutdown);
        status = Status.DISABLED;
        MessageManager.getInstance().debugConsole(String.format("Arena %s disabled", id));
        updateGameItems();
    }

    public void disable(){
        disable(false);
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
        if (GameManager.getInstance().getActivePlayerGameID(p) != -1) {
            if (GameManager.getInstance().isPlayerActive(p)) {
                MessageManager.getInstance().sendFMessage("game.joinmutliple", p);
                return false;
            }
        }
        if(p.isInsideVehicle()){
            p.leaveVehicle();
        }

        if(isSpectator(p))
            removeSpectator(p, false);

        if(status == Status.LOBBY || status == Status.STARTING) {
            if (activePlayers.size() < getTotalSlots()) {
                msg.sendFMessage("game.join", p, "arena-" + name);
                PlayerJoinArenaEvent joinArena = new PlayerJoinArenaEvent(p, this);
                Bukkit.getServer().getPluginManager().callEvent(joinArena);
                if(!joinArena.isCancelled()) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.setFallDistance(0);
                    p.teleport(lobby.getSpawn());
                    Collection<PotionEffect> effects = p.getActivePotionEffects();
                    for (PotionEffect pe : effects) {
                        p.removePotionEffect(pe.getType());
                    }
                    saveInv(p);
                    clearInv(p);
                    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    p.setFoodLevel(20);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 1));
                    clearInv(p);
                    p.setScoreboard(board);


                    activePlayers.add(p);
                    addToTeam(p);
                    timeBar.addPlayer(p);
                    msgFArena("game.team", "team-" + getTeamNameLocalized(p), "player-" + p.getDisplayName());
                    if (getTotalSlots() == activePlayers.size()) {
                        countdown(5);
                    }
                }
            } else if (getTotalSlots() == 0) {
                msg.sendFMessage("game.nospawns", p, "arena-" + id);
                return false;
            } else {
                msg.sendFMessage("error.gamefull", p, "arena-" + id);
                return false;
            }
            msgFArena("game.playerjoingame", "player-" + p.getName(), "activeplayers-" + activePlayers.size(), "maxplayers-" + getTotalSlots());
            if(!countdownRunning){
                float startMin = (float)SettingsManager.getInstance().getPluginConfig().getDouble("auto-start", 0.75);
                float start = (float) activePlayers.size()/getTotalSlots();
                if(start >= startMin){
                    countdown(20);
                }
            }
            updateGameItems();
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
    ///         Vote Start          ///
    ///////////////////////////////////
    public void vote(Player p){
        if(status == Status.INGAME){
            msg.sendFMessage("error.alreadyingame", p);
            return;
        }else if(status != Status.LOBBY){
            msg.sendFMessage("error.alreadyingame", p);
            return;
        }

        if(voted.contains(p)){
            msg.sendFMessage("error.alreadyvoted", p);
            return;
        }

        voted.add(p);
        msgFArena("game.playervote", "player-" + p.getDisplayName());
        msgFArena("game.voteamount", "percent-" + Math.round(((voted.size()+0.0)/(activePlayers.size()+0.0))*100));

        if((voted.size()/activePlayers.size()) >= SettingsManager.getInstance().getPluginConfig().getDouble("vote-start") && activePlayers.size() > 1){
            countdown(20);
        }
    }

    /**
     * Returns the total number of player slots allowed in this game.
     * @return Total of teams * perTeam option value.
     */
    public int getTotalSlots(){
        return perTeam * teams.size();
    }

    /**
     * Get the number of open slots still available.
     * @return total slots subtracted by player count.
     */
    public int getOpenSlots(){
        return getTotalSlots() - getPlayerCount();
    }

    /**
     * Get the count of players in the arena.
     * @return Counts the players on each team.
     */
    @SuppressWarnings("deprecation")
    public int getPlayerCount(){
        int playerCount = 0;
        for(CCTeam ccTeam: teams.keySet()){
            Team team = teams.get(ccTeam);
            playerCount += team.getPlayers().size();
        }
        return playerCount;
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
            setupPlayers();
        }
        status = Status.INGAME;
        timerTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new GameTimer(), 0, 20);
        particleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new ParticleTimer(), 0, 5);
        tasks.add(timerTaskID);
        tasks.add(particleTaskID);
        MessageManager.getInstance().broadcastFMessage("broadcast.gamestarted", "arena-" + id);
        updateGameItems();
    }

    public void forceStartGame(){
        if(status == Status.INGAME){
            return;
        }

        setupPlayers();
        status = Status.INGAME;
        timerTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new GameTimer(), 0, 20);
        particleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new ParticleTimer(), 0, 5);
        tasks.add(timerTaskID);
        tasks.add(particleTaskID);
        MessageManager.getInstance().broadcastFMessage("broadcast.gamestarted", "arena-" + id);
        updateGameItems();
    }

    /**
     * Setup all the players by teleporting them and setting their gamemodes and all other needed attributes.
     */
    @SuppressWarnings("deprecation")
    private void setupPlayers(){
        for(CCTeam ccTeam: teamSpawns.keySet()){
            Team team = getTeam(ccTeam);
            for(OfflinePlayer p: team.getPlayers()){
                Player player = p.getPlayer();
                if(!player.isDead()) {
                    player.teleport(getSpawn(ccTeam));
                    clearInv(player);
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                msg.sendFMessage("game.goodluck", player);
            }
        }
    }

    /**
     * Get the spawn for the specified team.
     * @param team The team of the spawn wanted.
     * @return Returns a location if found, or will return null if no spawn is found.
     */
    public Location getSpawn(CCTeam team){
        return teamSpawns.get(team);
    }

    /**
     * Gets the players team spawn.
     * @param player The player.
     * @return Returns a location if found or returns null if no spawn is found.
     */
    public Location getSpawn(Player player){
        CCTeam team = getCCTeam(player);
        return getSpawn(team);
    }

    /**
     * Add a spawmn point for the given team by either adding the team or updating the spawn point.
     * @param team The team the spawn point is being added for.
     */
    public void addSpawn(CCTeam team){
        addTeam(team.name().toLowerCase());
    }

    /**
     * Removes the spawn point for the given team, thus also removing the team form the arena.
     * @param team The team to be removed.
     */
    public void removeSpawn(CCTeam team){
        if(teams.get(team) != null) {
            teamSpawns.remove(team);
            teams.get(team).unregister();
            teams.remove(team);
            teamScores.remove(team);
            updateGameItems();
        }
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
            tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                public void run() {
                    if (count > 0) {
                        if (count % 10 == 0) {
                            msgFArena("game.countdown", "t-"+count);
                            subTitleFArena("game.countdown", "t-"+count);
                        }
                        if (count < 6) {
                            msgFArena("game.countdown", "t-"+count);
                            titleArena("&6" + count);
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
    @SuppressWarnings("deprecation")
    public void removePlayer(Player player, boolean b){
        PlayerLeaveArenaEvent playerLeaveArenaEvent = new PlayerLeaveArenaEvent(player, this, b);
        Bukkit.getPluginManager().callEvent(playerLeaveArenaEvent);

        player.teleport(SettingsManager.getInstance().getGlobalLobbySpawn());
        restoreInv(player);
        Collection<PotionEffect> effects = player.getActivePotionEffects();
        for(PotionEffect pe: effects){
            player.removePotionEffect(pe.getType());
        }
        activePlayers.remove(player);
        getTeam(player).removePlayer(player);
        msgFArena("game.playerleave", "player-" + player.getDisplayName());
        if(activePlayers.size() <= 1){
            if(status.equals(Status.LOBBY)) {
                msg.debugConsole("Player Left Arena " + id + " while waiting in lobby.");
            }else if(status.equals(Status.STARTING)){
                Bukkit.getScheduler().cancelTask(tid);
                countdownRunning = false;
                status = Status.LOBBY;
                msgFArena("game.end", "reason-Player left the game.");
            }else{
                msgFArena("game.end", "reason-Not enough players");
                endGame();
            }
        }
        player.setScoreboard(manager.getNewScoreboard());
        timeBar.removePlayer(player);

        PlayerLeaveArenaEvent pl = new PlayerLeaveArenaEvent(player, this, b);
        Bukkit.getPluginManager().callEvent(pl);
        updateGameItems();
    }

    ///////////////////////////////////
    ///        Reset Arena          ///
    ///////////////////////////////////
    public void resetArena(boolean shutdown){
        for(Integer i: tasks){
            Bukkit.getScheduler().cancelTask(i);
        }
        tasks.clear();
        powerups.clear();
        voted.clear();

        MessageManager.getInstance().debugConsole("Resetting Player information in arena " + id);
        activePlayers.clear();
        spectators.clear();
        clearTeams();
        clearSpectatorTeam();
        resetScores();

        board.resetScores("Time");

        status = Status.RESETING;
        updateGameItems();
        endgameRunning = false;

        Bukkit.getScheduler().cancelTask(timerTaskID);
        Bukkit.getScheduler().cancelTask(endgameTaskID);
        Bukkit.getScheduler().cancelTask(particleTaskID);
        QueueManager.getInstance().rollback(id, shutdown);
    }

    @SuppressWarnings("deprecation")
    private void clearSpectatorTeam(){
        for(OfflinePlayer player: spectatorsTeam.getPlayers()){
            spectatorsTeam.removePlayer(player);
        }
    }

    /**
     * Empty all the teams. (RED, BLUE, GREEN, YELLOW). This does not include spectators.
     */
    @SuppressWarnings("deprecation")
    private void clearTeams(){
        for(CCTeam ccTeam: teams.keySet()){
            Team team = teams.get(ccTeam);
            for(OfflinePlayer player: team.getPlayers()){
                team.removePlayer(player);
            }
        }
    }

    /**
     * Reset all team scores back to zero.
     */
    public void resetScores(){
        for(CCTeam team: teamScores.keySet()){
            teamScores.put(team, 0);
        }
    }

    ///////////////////////////////////
    ///          Win Game           ///
    ///////////////////////////////////
    @SuppressWarnings("deprecation")
    public void winGame(){
        if(status == Status.INGAME) {
            Set<OfflinePlayer> players;
            CCTeam winningTeam = getWinningTeam();
            if(winningTeam != null) {
                players = getTeam(winningTeam).getPlayers();
            }else{
                players = new HashSet<>();
            }
            giveReward(players);
            giveReward(players);
            TeamWinEvent tw = new TeamWinEvent(players, winningTeam, reward);
            Bukkit.getPluginManager().callEvent(tw);
            MessageManager.getInstance().broadcastFMessage("broadcast.gamewin", "team-" + getTeamNameLocalized(winningTeam), "arena-" + id);
        }
    }

    ///////////////////////////////////
    ///          End Game           ///
    ///////////////////////////////////
    public void endGame(boolean shutdown) {
        winGame();
        for(Player p : activePlayers){
            endGamePlayer(p);
        }
        for(Player p: spectators){
            endGamePlayer(p);
        }
        timeBar.removeAll();
        timeBar.setTitle(ChatColor.GOLD + "Arena " + id);
        timeBar.setProgress(1);
        status = Status.FINISHING;
        updateGameItems();
        resetArena(shutdown);
        status = Status.LOBBY;
        updateGameItems();
    }

    private void endGamePlayer(Player p){
        for(PotionEffect pe: p.getActivePotionEffects()){
            p.removePotionEffect(pe.getType());
        }
        p.setScoreboard(manager.getNewScoreboard());
        p.teleport(SettingsManager.getInstance().getGlobalLobbySpawn());
        restoreInv(p);
    }

    public void endGame(){
        endGame(false);
    }

    ///////////////////////////////////
    ///        Add Spectator        ///
    ///////////////////////////////////
    public boolean addSpectator(Player p){
        if(!p.hasPermission("cc.arena.spectate." + id) || !p.hasPermission("cc.arena.spectate.*")){
            msg.debugConsole("Need cc.arena.spectate." + id + "or cc.arena.spectate.*");
            msg.sendFMessage("error.nopermission", p);
            return false;
        }
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
        if (GameManager.getInstance().getSpectatePlayerId(p) != -1) {
            if (GameManager.getInstance().isPlayerActive(p)) {
                MessageManager.getInstance().sendFMessage("game.joinmutliple", p);
                return false;
            }
        }

        if(p.isInsideVehicle()){
            p.leaveVehicle();
        }

        if(status == Status.INGAME){
            msg.sendFMessage("game.join", p, "arena-" + id);
            //TODO Spectate API
            p.setGameMode(GameMode.CREATIVE);
            p.teleport(teamSpawns.values().iterator().next());
            saveInv(p);
            clearInv(p);
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            p.setFoodLevel(20);
            clearInv(p);
            p.setScoreboard(board);

            p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, SettingsManager.getInstance().getPluginConfig().getInt("game-length", 600) * 20, 1, true));
            spectators.add(p);
            //spectate.addPlayer(p);
            updateGameItems();
            return true;
        }else if(status == Status.DISABLED){
            msg.sendFMessage("error.gamedisabled", p, "arena-" + id);
        }else if(status == Status.RESETING){
            msg.sendFMessage("error.gameresetting", p);
        }else if(status == Status.LOBBY || status == Status.STARTING){
            msg.sendFMessage("error.notingame", p);
        }else{
            msg.sendFMessage("error.joinfail", p);
        }
        return false;
    }

    ///////////////////////////////////
    ///       Remove Spectator      ///
    ///////////////////////////////////
    @SuppressWarnings("deprecation")
    public boolean removeSpectator(Player player, boolean logout){
        player.teleport(SettingsManager.getInstance().getGlobalLobbySpawn());
        restoreInv(player);
        player.setScoreboard(manager.getNewScoreboard());

        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        spectatorsTeam.removePlayer(player);
        spectators.remove(player);
        //TODO Spectator API
        updateGameItems();
        return true;
    }

    public int getTeamScore(CCTeam team){
        return teamScores.get(team);
    }

    public int setTeamScore(CCTeam team, int score){
        return teamScores.put(team, score);
    }

    @SuppressWarnings("deprecation")
    public void addToTeam(Player player){
        CCTeam teamToSet = null;

        for(CCTeam team: teams.keySet()){
            if(teamToSet == null){
                teamToSet = team;
                continue;
            }

            if(teams.get(team).getSize() < teams.get(teamToSet).getSize()){
                teamToSet = team;
            }
        }

        teams.get(teamToSet).addPlayer(player);
    }

    @SuppressWarnings("deprecation")
    public Team getTeam(Player player){
        for(CCTeam ccTeam: teams.keySet()){
            Team t = teams.get(ccTeam);
            if(t.getPlayers().contains(player)){
                return t;
            }
        }
        return null;
    }

    public Team getTeam(CCTeam team){
        for(CCTeam ccTeam: teams.keySet()){
            if(ccTeam.equals(team)){
                return teams.get(ccTeam);
            }
        }
        return null;
    }

    public String getTeamNameLocalized(Player player){
        CCTeam team = getCCTeam(player);
        if(team == null){
            return null;
        }
        return SettingsManager.getInstance().getMessagesConfig().getString(String.format("messages.color.%s", team.name().toLowerCase()));
    }

    /**
     * Retrieves the CCTeam the player is on.
     * @param player The player that the team is trying to be retrieved from.
     * @return The team the player is on, but may return null if the player is not on any of the teams.
     */
    @SuppressWarnings("deprecation")
    public CCTeam getCCTeam(Player player){
        for(CCTeam team: teams.keySet()){
            if(teams.get(team).getPlayers().contains(player)){
                return team;
            }
        }
        return null;
    }

    public void increaseScore(CCTeam team, int amount){
        for(CCTeam ccTeam: teamScores.keySet()){
            if(ccTeam.equals(team)){
                setTeamScore(team, getTeamScore(team)+amount);
            }
        }
    }

    public void decreaseScore(CCTeam team, int amount){
        for(CCTeam ccTeam: teamScores.keySet()){
            if(ccTeam.equals(team)){
                setTeamScore(team, getTeamScore(team)-amount);
            }
        }
    }

    public boolean isBlockInArena(Location v) {
        return arena.containsBlock(v);
    }

    public boolean isBlockInLobby(Location v) {
        return lobby != null && lobby.containsBlock(v);
    }

    public void resetCallback() {
        if (!disabled){
            enable();
        }
        else status = Status.DISABLED;
    }

    ///////////////////////////////////
    ///         Game Timer          ///
    ///////////////////////////////////

    class GameTimer implements Runnable {
        double counter = (double)SettingsManager.getInstance().getPluginConfig().getInt("game-length", 600);
        double maxTime = counter;
        int powerupDefault = SettingsManager.getInstance().getPluginConfig().getInt("powerup-freq", 15);
        int powerup = powerupDefault;
        @Override
        public void run() {
            if(counter > 0){
                --counter;
                timeBar.setTitle(ChatColor.GOLD + "Time: "+ ChatColor.WHITE + (int)(counter) + " seconds");
                timeBar.setProgress(counter/maxTime);
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
                int attempt = 1;
                while(finish) {
                    x = random.nextInt((arena.getPos1().getBlockX() - arena.getPos2().getBlockX()) + 1) + arena.getPos2().getBlockX() + 0.5;
                    y = random.nextInt((arena.getPos1().getBlockY() - arena.getPos2().getBlockY()) + 1) + arena.getPos2().getBlockY();
                    z = random.nextInt((arena.getPos1().getBlockZ() - arena.getPos2().getBlockZ()) + 1) + arena.getPos2().getBlockZ() + 0.5;
                    Location loc = new Location(arena.getPos1().getWorld(), x, y, z);
                    Location loc2 = loc.clone();
                    loc2.subtract(0, 1, 0);
                    if(SettingsManager.getInstance().getPluginConfig().getStringList("paintable-blocks").contains(loc2.getBlock().getType().toString())){
                        Location loc3 = loc.clone();
                        loc3.add(0, 1, 0);
                        if(loc3.getBlock().getType() == Material.AIR) {
                            if (loc.getBlock().getType() == Material.AIR) {
                                createPowerup(loc2, true);
                                finish = false;
                            }
                        }
                    }

                    if(attempt == 100){
                        MessageManager.getInstance().debugConsole("Could not spawn powerup.");
                        finish = false;
                    }else {
                        attempt++;
                    }
                }
                powerup = powerupDefault;
            }else{
                --powerup;
            }
        }
    }

    /**
     * Method that is called whenever LobbySign needs to be updated, or if a game menu needs to be updated.
     */
    private void updateGameItems(){
        LobbyManager.getInstance().update(getId());
        MenuManager.getInstance().update(getId());
    }

    class ParticleTimer implements Runnable {
        @Override
        public void run() {
            if (powerups.size() > 0) {
                for (Powerup pu : powerups) {
                    for(Player player: getAllPlayers()){
                        player.spawnParticle(Particle.NOTE, pu.getLocation(), 10, 0.2F, 0.5F, 0.2F, 1);
                    }
                }
            }
        }
    }

    /**
     * Retrieves the lobby spawn for the game. This lobby will be located within the lobby.
     * @return This will return the location of the lobby, but if it is not set will return null.
     */
    public Location getLobbySpawn(){
        if(lobby.isSpawnSet()){
            return lobby.getSpawn();
        }
        return null;
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

    /**
     * Calculates the winning team.
     * @return This will return the {@link CCTeam} but will return null if there is no winner.
     */
    public CCTeam getWinningTeam(){
        for(CCTeam team: teamScores.keySet()){
            boolean isLeader = true;
            for(CCTeam teamCheck: teamScores.keySet()){
                if(!teamCheck.equals(team)){
                    if(getTeamScore(teamCheck) > getTeamScore(team)) {
                        isLeader = false;
                        break;
                    }
                }
            }

            if(isLeader)
                return team;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void changeBlock(Location loc, CCTeam team) {
        if(loc.getBlock().getType().equals(Material.STAINED_CLAY)){
            if(loc.getBlock().getData() != team.getBlockData()){
                switch (loc.getBlock().getData()) {
                    case 14:
                        scoreManagement(team, CCTeam.RED, 1);
                        break;
                    case 3:
                        scoreManagement(team, CCTeam.BLUE, 1);
                        break;
                    case 5:
                        scoreManagement(team, CCTeam.GREEN, 1);
                        break;
                    case 4:
                        scoreManagement(team, CCTeam.YELLOW, 1);
                        break;
                    case 0:
                        scoreManagement(team, null, 1);
                        break;
                }
            }
        }
        LoggingManager.getInstance().logBlockDestroyed(loc.getBlock());
        loc.getBlock().setType(Material.STAINED_CLAY);
        if(team == null)
            loc.getBlock().setData((byte) 0);
        else
            loc.getBlock().setData((byte) team.getBlockData());
    }

    public void giveReward(Set<OfflinePlayer> players){
        if(ColorCube.economy != null) {
            for (OfflinePlayer player : players) {
                ColorCube.economy.depositPlayer(player, reward);
            }
        }
    }

    public void scoreManagement(CCTeam teamIncrease, CCTeam teamDecrease, int amount) {
        increaseScore(teamIncrease, amount);
        decreaseScore(teamDecrease, amount);
    }

    public List<Powerup> getPowerups() {
        return powerups;
    }

    public void removePowerup(Powerup powerup){
        powerups.remove(powerup);
    }

    public void setLobbySpawn(int id, World world, int x, int y, int z){
        lobby.setSpawn(id, new Location(world, x, y, z));
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

    public HashMap<Player, ItemStack[][]> getInventory_store() {
        return inventory_store;
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

    public boolean isPvp(){
        return pvp;
    }

    public void setPvp(Boolean pvp){
        this.pvp = pvp;
        SettingsManager.getInstance().getArenaConfig(id).set("options.pvp", pvp);
        SettingsManager.getInstance().saveArenaConfig(id);
    }

    public double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
        SettingsManager.getInstance().getArenaConfig(id).set("options.reward", reward);
        SettingsManager.getInstance().saveArenaConfig(id);
    }

    public int getPerTeam(){
        return perTeam;
    }

    public void setPerTeam(Integer perTeam){
        this.perTeam = perTeam;
        updateGameItems();
        SettingsManager.getInstance().getArenaConfig(id).set("options.perteam", perTeam);
        SettingsManager.getInstance().saveArenaConfig(id);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
        updateGameItems();
        SettingsManager.getInstance().getArenaConfig(id).set("options.name", name);
        SettingsManager.getInstance().saveArenaConfig(id);
    }

    public ArrayList <Player> getAllPlayers() {
        ArrayList <Player> all = new ArrayList <> ();
        all.addAll(activePlayers);
        all.addAll(spectators);
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

    public void titleFArena(String string, String...args){
        for(Player p: getAllPlayers()){
            msg.sendFTitle(string, p, args);
        }
    }

    public void titleArena(String string){
        for(Player p: getAllPlayers()){
            msg.sendTitle(string, p);
        }
    }

    public void subTitleFArena(String string, String...args){
        for(Player p: getAllPlayers()){
            msg.sendFSubTitle(string, p, args);
        }
    }

    public void subTitleArena(String string){
        for(Player p: getAllPlayers()){
            msg.sendSubTitle(string, p);
        }
    }

    /**
     * Get the team name that is also localized.
     * @param team The team to get the name for.
     * @return Will return the value set in the message.yml for the team colours.
     */
    public static String getTeamNameLocalized(CCTeam team){
        return SettingsManager.getInstance().getMessagesConfig().getString(String.format("messages.color.%s", team.name().toLowerCase()));
    }
}
