package net.minesucht.main;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minesucht.commands.CMD_CheckClicks;
import net.minesucht.commands.CMD_Key;
import net.minesucht.commands.CMD_SetLobbySpawn;
import net.minesucht.commands.CMD_SetStatsWall;
import net.minesucht.commands.CMD_Stats;
import net.minesucht.commands.CMD_Upload;
import net.minesucht.commands.CMD_dev;
import net.minesucht.commands.CMD_endGameTest;
import net.minesucht.commands.CMD_info;
import net.minesucht.commands.CMD_move;
import net.minesucht.commands.CMD_setspawns;
import net.minesucht.commands.CMD_start;
import net.minesucht.enums.Games;
import net.minesucht.listener.BlockEvent;
import net.minesucht.listener.DropandItemListener;
import net.minesucht.listener.EntityInteractListener;
import net.minesucht.listener.FoodListener;
import net.minesucht.listener.ItemClickListener;
import net.minesucht.listener.JoinListener;
import net.minesucht.listener.MoveListener;
import net.minesucht.listener.PingListener;
import net.minesucht.listener.PlayerDamageEvent;
import net.minesucht.listener.QuitListener;
import net.minesucht.managers.CountDownManager;
import net.minesucht.managers.FIleLogger;
import net.minesucht.managers.Fetcher;
import net.minesucht.managers.FileConfigurationManager;
import net.minesucht.managers.GameManager;
import net.minesucht.managers.MapManager;
import net.minesucht.managers.PunkteManager;
import net.minesucht.mysql.MySQL;
import net.minesucht.statswall.statswall;

public class MiniGames extends JavaPlugin{

	private static MiniGames instance; 
	//private static MineAPI mineAPI;
	private static MySQL sql;
	//private static NickAPI nick;
	private GameManager gameManager;
	private MapManager mapManager;
	private FileConfigurationManager configManager;
	private CountDownManager cdm;
	private PunkteManager pkm;
	private statswall sw;
	private Logger log;
	private Fetcher fetcher;
	private String fileName;
	private ArrayList<String> allBlocked;
	FIleLogger fl;
	@Override
	public void onEnable(){
		allBlocked = new ArrayList<>();
		log = Bukkit.getServer().getLogger();
		instance = this; // Weise Instanz zu!
		generateRandomFileName();
		
		loadConfig(); 		
		File dir = null;

		for(int i = 0; i < Games.values().length; i++){
			dir = new File(MiniGames.getInstance().getDataFolder() + File.separator + Games.values()[i].toString());
			if (!dir.exists()) {
			    try{
			        dir.mkdir();
			    } 
			    catch(SecurityException e){
			    	e.printStackTrace();
			    }        
			}	
				
		}
		
		registerMySQL();		
		registerAPIS();
		registerManagers();		
		registerListener();		
		registerCommands();		
		for(Games g : MiniGames.getInstance().getGameManager().getGames()){
			System.out.println(g.getPrefix());
			System.out.println(getMapManager().getGameSpawns(g).get(0).toString());
		}
		System.gc();			//Führe GarbageContainer aus!
		checkIngame();
		System.out.println("Dateiname " + fileName);
	//	statswall.getInstance().setStatsWand();
		fl = new FIleLogger();
		
		System.out.println("Dateiname " + fileName);
		System.out.println("Dateiname " + fileName);
		
		System.out.println("Dateiname " + fileName);
		System.out.println("Dateiname " + fileName);
		System.out.println("Dateiname " + fileName);
		System.out.println("Dateiname " + fileName);
		System.out.println("Dateiname " + fileName);
		
		System.out.println("Dateiname " + fileName);
		
		System.out.println("Dateiname " + fileName);
		
		for(Games g : MiniGames.getInstance().getGameManager().getGames()){
			System.out.println(mapManager.getWorlds().get(g).toString());
			System.out.println(mapManager.getGameSpawns(g).get(0).toString());
		}
		cdm.setTimeForAllWorlds();
	}
	
	@Override
	public void onDisable(){
		FIleLogger.getInstance().replaceLogs();
			
	
			
		Bukkit.getOnlinePlayers().stream().forEach(p -> p.kickPlayer(" "));
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kill @e");
		FIleLogger.getInstance().uploadToFtp();
		System.gc();
		
				
				
			
	}
	
	//Registriere alle Manager Classen
	private void registerManagers(){
		try{
			mapManager = new MapManager();
			gameManager = new GameManager();
			cdm = new CountDownManager();
			pkm = new PunkteManager();
			sw = new statswall();
			fetcher = new Fetcher();
			createDirs();
			registerConfigManager();
			MiniGames.getInstance().getMapManager().registerGameSpawns();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void registerConfigManager(){
		configManager = new FileConfigurationManager(MiniGames.getInstance().getGameManager().getGames(), MiniGames.getInstance().getMapManager().getWorlds());
	}
	
	private void createDirs(){
		MiniGames.getInstance().getMapManager().registerGameWorlds();
	}
	
	
	public void registerAPIS(){
		//mineAPI = new MineAPI();
	}
	//Registriere alle Commands
	private void registerCommands(){
		getCommand("setlobbyspawn").setExecutor(new CMD_SetLobbySpawn());
		getCommand("world").setExecutor(new CMD_move());
		getCommand("dev").setExecutor(new CMD_dev());
		getCommand("stats").setExecutor(new CMD_Stats());
		getCommand("start").setExecutor(new CMD_start());
		getCommand("setspawns").setExecutor(new CMD_setspawns());
		getCommand("setstats").setExecutor(new CMD_SetStatsWall());
		getCommand("info").setExecutor(new CMD_info());
		getCommand("upload").setExecutor(new CMD_Upload());
		getCommand("endgame").setExecutor(new CMD_endGameTest());
		getCommand("checkcs").setExecutor(new CMD_CheckClicks(this));
		getCommand("key").setExecutor(new CMD_Key());
	}
	
	//Registriere alle Listener
	private void registerListener(){
	//	new WeatherChange(this);
		new MoveListener(this);
		new QuitListener(this);
		new JoinListener(this);
		new ItemClickListener(this);
		//new PingListener(this);
		new BlockEvent(this);
		new PlayerDamageEvent(this);
		new FoodListener(this);
		new EntityInteractListener(this);
		new DropandItemListener(this);
		new CMD_CheckClicks(this);
	}
	
	private void registerMySQL(){
		try {sql = new MySQL(this);} catch (Exception e) {e.printStackTrace();}
	}
	
	//Lade Konfiguartionsdatei!
	public void loadConfig(){
		if(getDataFolder().exists())
		{
			reloadConfig();
			return;
		}
		else
		{
			getConfig().options().header("Viele Minispiele in einem :)");
			getConfig().set("FTP.Hostname", "127.0.0.1");
			getConfig().set("FTP.User", "Pete");
			getConfig().set("FTP.Password", "root");
			getConfig().set("FTP.Path", "/home/stacktrace/");
			getConfig().set("FTP.Port", 21);
			getConfig().set("MySQL.User", "root");
			getConfig().set("MySQL.Password", "root");
			getConfig().set("MySQL.Port", 3306);
			getConfig().set("MySQL.Hostname", "localhost");
			getConfig().set("MySQL.Database", "Database");
			getConfig().set("Lobby.Worldname", "world");
			getConfig().set("Lobby.Spawn", "none");
			getConfig().set("Lobby.Generator", "0,0,0");
			getConfig().set("Statswall.Coord", "0,0,0,0,0");
			saveConfig();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Getter Section
	
	public static MySQL getMySQL(){
		return sql;
	}
	/*
	public MineAPI getMineAPI(){
		return mineAPI;
	}
	
	public NickAPI getNickAPI(){
		//return nick;
	}
	*/
	public CountDownManager getCountDownManager(){
		return cdm;
	}
	public PunkteManager getPunkteManager(){
		return pkm;
	}
	public static MiniGames getInstance(){
		return instance;
	}

	public MapManager getMapManager(){
		return mapManager;
	}
	
	public GameManager getGameManager(){
		return gameManager;
	}
	
	public FileConfigurationManager getFileConfigurationManager(){
		return configManager;
	}
	
	public Logger getLog(){
		return log;
	}
	
	public Fetcher getFetcher(){
		return fetcher;
	}
	
	public void checkIngame(){
		new BukkitRunnable() {
			
			@Override
			public void run() {
				System.out.println("Spielerliste: " + getGameManager().getIngamePlayers().size());
				
			}
		}.runTaskTimer(this, 0, 20);
		
	}
	
	public void generateRandomFileName(){
		Random r = new Random();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String dateString = dateFormat.format(date);
		dateString = dateString.replace("/", "");
		dateString = dateString.replace("//", "");
		dateString = dateString.replace(":", "-");
		String name = "" + r.nextInt(50) + "Date" + dateString + "_"+ r.nextInt(750) + ".log";
	
		name = FIleLogger.sha256(name);
		fileName = name;
	}
	
	public String getFileName(){
		return fileName;
	}
	public ArrayList<String> getBlocked(){
		return allBlocked;
	}
}
