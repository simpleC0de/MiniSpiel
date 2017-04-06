package net.minesucht.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.minesucht.main.MiniGames;

public class CMD_dev implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("dev")){
			if(args[0].equalsIgnoreCase("newgame")){
				MiniGames.getInstance().getGameManager().createNewGame();
			}
		}
		
		return false;
	}

}
