package net.minesucht.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import net.minesucht.main.MiniGames;

public class CMD_SetStatsWall implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof Player){
			if(cs.hasPermission("MG.Config")){
				Location loc = ((Player) cs).getLocation();
				String configloc = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
				MiniGames.getInstance().getConfig().set("Statswall.Coord", configloc);
				MiniGames.getInstance().saveConfig();
				cs.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + " Die Statswall wurde gesetzt!");
				return true;
			}else{
				cs.sendMessage(ChatColor.WHITE + "Unkown Command /help for help");
				return true;
			}
			
		}
		return true;
	}

}
