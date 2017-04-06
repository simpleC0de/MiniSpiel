package net.minesucht.commands;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minesucht.main.MiniGames;
import net.minesucht.managers.PunkteManager;
import net.minesucht.mysql.MySQL;

public class CMD_Stats implements CommandExecutor{

	private MySQL sql = MiniGames.getMySQL();
	
	
	private DecimalFormat df = new DecimalFormat("###.##");
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
	
		
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(cmd.getName().equalsIgnoreCase("stats")){
				try{
					String uuid = p.getUniqueId().toString();
					String prefix = MiniGames.getInstance().getGameManager().getPluginPrefix();
					//
					int gamesplayed = sql.getPlayed(uuid);
					//
					int gameswon = sql.getWon(uuid);
					//
					int points = sql.getPunkte(uuid);
					//
					int RankValue = MiniGames.getMySQL().getRank(uuid);
					String rank = MiniGames.getMySQL().getRankName(RankValue);
					//
					MiniGames.getInstance().getPunkteManager().checkrankUp(uuid);
					double allplayed = (double)gameswon + (double)gamesplayed;
					double winpercentage = 0;
					try{
						winpercentage = gameswon / allplayed;
						System.out.println("WP" + winpercentage);
						System.out.println("Afterr + " +  winpercentage * 100);
						winpercentage = winpercentage * 100;
						System.out.println(p.getCustomName() + "Wins: " + gameswon  + " Played Games:" + gamesplayed + " Allgames: " + allplayed + " Prozent: " + winpercentage);
					}catch(Exception ex){
						winpercentage = 0.0;
					}
					
					//
					
					p.sendMessage(ChatColor.GRAY +"-----" + ChatColor.RED + "Stats" + ChatColor.GRAY + "------");
					p.sendMessage(ChatColor.GOLD + "Spiele gespielt: " + ChatColor.GRAY + (int)allplayed);
					p.sendMessage(ChatColor.GOLD + "davon gewonnen: " + ChatColor.GRAY + gameswon);
					p.sendMessage(ChatColor.GOLD + "Winchance: "+ ChatColor.GRAY + df.format(winpercentage) + "%");
					p.sendMessage(ChatColor.GOLD + "Deine Punkte: " + ChatColor.GRAY + points);
					p.sendMessage(ChatColor.GOLD + "Dein Rank: "  + rank);
					p.sendMessage(ChatColor.GRAY +"-----" + ChatColor.RED + "Stats" + ChatColor.GRAY + "------");
					return true;
				}catch(Exception ex){
					ex.printStackTrace();
					p.sendMessage(ChatColor.RED + "Fehler im System, bitte melde dies einem Admin" + ChatColor.GRAY + "Fehlercode: ST23AT");
					return true;
				}	
			}
		}else{
			return true;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return true;
	}

}
