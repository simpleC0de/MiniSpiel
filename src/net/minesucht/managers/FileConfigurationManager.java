 package net.minesucht.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class FileConfigurationManager {

	private HashMap<Games, FileConfig> configs; // Nötige Configs für den Ablauf des Spieles
	//Liste für alle Configs
	// Spielename, Weltenname, FileConfiguration
	private HashMap<Games, HashMap<World, FileConfig>> globalConfigs;
	
	public FileConfigurationManager(List<Games> games, HashMap<Games, World> gameworlds){
		globalConfigs = new HashMap<Games, HashMap<World, FileConfig>>();
		System.out.println("Games.size " + games.size());
		System.out.println("GameWorlds.Size" + gameworlds.size());
			
		for(int i = 0; i < gameworlds.size(); i++){
			createConfigIfNotExists(
					MiniGames.getInstance().getDataFolder().getAbsolutePath() + 
					File.separator + games.get(i).toString() + File.separator + gameworlds.get(games.get(i)).getName());			
		}
		
		
			
	}

	private void createConfigIfNotExists(String path){

		File cfgFile = new File(path + File.separator + "config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
		FileConfig fg = new FileConfig(cfg, cfgFile);

	}
	
	public FileConfig getLocalConfig(Games game){
		return configs.get(game);
	}
	
	public FileConfig getGlobalConfig(Games game, World w){
		if(globalConfigs.containsKey(game)){
			if(globalConfigs.get(game).containsKey(w)){
				return globalConfigs.get(game).get(w);
			}
			File cfgFile = new File(MiniGames.getInstance().getDataFolder() + File.separator + game.toString() + File.separator + w.getName() + File.separator + "config.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
			FileConfig fg;
			fg = new FileConfig(cfg, cfgFile);

			HashMap<World, FileConfig> hm = globalConfigs.get(game);
			hm.put(w, fg);
			
			globalConfigs.replace(game, hm);
			
			return globalConfigs.get(game).get(w);
		} 
		HashMap<World, FileConfig> hm = new HashMap<World, FileConfig>();
		File cfgFile = new File(MiniGames.getInstance().getDataFolder() + File.separator + game.toString() + File.separator + w.getName() + File.separator + "config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(cfgFile);
		FileConfig fg;
		fg = new FileConfig(cfg, cfgFile);
		hm.put(w, fg);
		globalConfigs.put(game, hm);
		return globalConfigs.get(game).get(w);
	}
	
	
	public void createConfigs(){
		//Hier
		File file;
		String[] directories;
		for(int i = 0; i < Games.values().length; i ++){
			file = new File(MiniGames.getInstance().getDataFolder() + File.separator + MiniGames.getInstance().getGameManager().getGames().get(i).toString());
			if(file.exists()){
				
				
			}else{
				try {
					file.createNewFile();
					directories = file.list();	// Alle Unterordner
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	

	
}
