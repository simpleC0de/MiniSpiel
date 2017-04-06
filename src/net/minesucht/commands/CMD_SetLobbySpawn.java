package net.minesucht.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minesucht.main.MiniGames;

public class CMD_SetLobbySpawn implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("setlobbyspawn")){
			if(cs instanceof Player){
				Player p = (Player)cs;
				if(p.hasPermission("MiniGames.SetSpawn")){
					
					Location spawn = p.getLocation();
					double x = spawn.getX();
					double y = spawn.getY();
					double z = spawn.getZ();
					float yaw = spawn.getYaw();
					float pitch = spawn.getPitch();
					
					String locstring = x + "," + y +"," + z + "," + yaw + "," + pitch;
					
					MiniGames.getInstance().getConfig().set("Lobby.Spawn", locstring);
					
					MiniGames.getInstance().saveConfig();
					
					p.sendMessage(ChatColor.GREEN + "Du hast erfolgreich den Lobbyspawnpunkt gesetzt.");
					
					
					
				}else{
					p.sendMessage(ChatColor.WHITE + "Unknown Command type /help for help.");
					return true;
				}
				
				
				
			}else{
				return true;
			}
		}
		return true;
	}

}
