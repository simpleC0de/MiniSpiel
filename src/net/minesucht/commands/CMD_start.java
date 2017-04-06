package net.minesucht.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minesucht.enums.GameStates;
import net.minesucht.main.MiniGames;
import net.minesucht.managers.CountDownManager;

public class CMD_start implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.hasPermission("mg.start")){
				if(MiniGames.getInstance().getGameManager().getGameState() == GameStates.LOBBY){
					if(MiniGames.getInstance().getGameManager().getIngamePlayers().size() >= 2){
						if(MiniGames.getInstance().getCountDownManager().getRunningTime() > 10){
							MiniGames.getInstance().getCountDownManager().cancelScheduler();
							MiniGames.getInstance().getCountDownManager().startingCountDown(10);
						} else {
							p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Das Spiel beginnt sofort...");
						}
					} else {
						p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Du kannst nicht alleine Spielen!");
					}
				} else {
					p.sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Das Spiel hat bereits begonnen!");
				}
			}
			
		}
		
		return true;
	}

}
