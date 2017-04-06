package net.minesucht.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.minesucht.main.MiniGames;

public class CMD_endGameTest implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("endgame")){
			try{
				MiniGames.getInstance().getGameManager().endGame();
				cs.sendMessage("§8Executed");
			}catch(Exception ex){
				ex.printStackTrace();
				cs.sendMessage("§c" + ex.getCause().toString());
			}
			
			
		}
		return true;
	}

}
