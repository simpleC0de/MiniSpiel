package net.minesucht.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class CMD_setspawns implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("mg.config")){
				if(args.length == 1){
					if(Games.valueOf(args[0].toUpperCase()) != null){
						setSpawn(p, Games.valueOf(args[0].toUpperCase()));
						return true;
					} else {
						p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Dies ist kein gültiger SpielModi.");
						p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Gültige Modi:");
						for(int i = 0; i < Games.values().length; i++){
							p.sendMessage("§4" + Games.values()[i]);
						}
						return true;
					}
				} else {
					p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Du musst dem Spielmodi angeben!");
					return true;
				}
			}
		}
		return true;
	}
	
	private void setSpawn(Player p, Games game){
		World w = p.getLocation().getWorld();
	
		String location = p.getLocation().getX() + "," + p.getLocation().getY() + "," + p.getLocation().getZ() + "," + p.getLocation().getYaw() + "," + p.getLocation().getPitch();
		List<String> l = MiniGames.getInstance().getFileConfigurationManager().getGlobalConfig(game, w).getFileConfiguration().getStringList("spawn");
		l.add(location);
		
		
		MiniGames.getInstance().getFileConfigurationManager().getGlobalConfig(game, w).getFileConfiguration().set("spawn", 
			l
		);
		
		MiniGames.getInstance().getFileConfigurationManager().getGlobalConfig(game, w).saveConfig();
		
		p.sendMessage("§2 Spawn für " + game.getPrefix() + "§2 erfolgreich gesetzt.");
	}

}
