package net.minesucht.commands;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class CMD_move implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("world")){
			if(sender instanceof Player){
				if(sender.hasPermission("mg.config")){
					Player p = (Player) sender;
						if(args.length == 1){
							if(args[0].equalsIgnoreCase("list")){
								HashMap<Games, ArrayList<String>> worldList = getWorldList();
								
								for(Games game : worldList.keySet()){

									p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Verfügbare Maps für §4" + game.toString());
									worldList.get(game).forEach(name -> p.sendMessage("§c |---> " + "§2" + name));
									p.sendMessage("");
								}
							}
						} else if(args.length == 2){
							if(args[0].equalsIgnoreCase("move")){
								
								if(args[1].equalsIgnoreCase(MiniGames.getInstance().getMapManager().getLobby().getName())){
									p.teleport(MiniGames.getInstance().getMapManager().getLobbySpawn());
									p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Du wurdest auf die Lobby Teleportiert");
								}
								
								HashMap<Games, ArrayList<String>> worldList = getWorldList();
								for(Games game : worldList.keySet()){
									for(String s : worldList.get(game)){
										if(s.equalsIgnoreCase(args[1])){
											
											MiniGames.getInstance().getMapManager().moveToWorld(p, game, s);
											p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Du wurdest auf die Map §c" + s + "§6 teleportiert.");
											
											return true;
										}
									}
								
								}
								p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Die ist keine gültige Map");
								return true;
							}
						} else {
							p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Bitte gib eine richtige Welt an!");
						}		
				} else {
					return true;
				}
			}
			
			return true;
		}	
		return true;
	}
	
	private HashMap<Games, ArrayList<String>> getWorldList(){
		File mapsPath;
		String[] mapDirectories = null;
		ArrayList<String> mapsPerGame = new ArrayList<String>();
		
		HashMap<Games, ArrayList<String>> maps = new HashMap<Games, ArrayList<String>>();	
		
		for(int i = 0; i < MiniGames.getInstance().getGameManager().getGames().size(); i++){
			
			mapsPath = new File(MiniGames.getInstance().getDataFolder() + File.separator + MiniGames.getInstance().getGameManager().getGames().get(i).toString());
			mapDirectories = mapsPath.list(new FilenameFilter() {
			
				 @Override
				 public boolean accept(File current, String name) {
				    return new File(current, name).isDirectory();
				 }
		 
			});	
			
			maps.put(MiniGames.getInstance().getGameManager().getGames().get(i), getList(mapDirectories));
		}

		return maps;
		
	}
	
	private ArrayList<String> getList(String[] array){
		ArrayList<String> al = new ArrayList<String>();
		
		for(String s : array) al.add(s);
		
		return al;
	}
	
}
