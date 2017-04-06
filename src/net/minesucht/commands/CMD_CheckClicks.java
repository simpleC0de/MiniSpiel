package net.minesucht.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.minesucht.main.MiniGames;

public class CMD_CheckClicks implements CommandExecutor, Listener{

	public CMD_CheckClicks(MiniGames plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	private boolean checkclickdspeed = false;
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(args.length == 0){
			if(checkclickdspeed){
				cs.sendMessage("Die Speed wird schon überprüft");
			}else{
				cs.sendMessage("Überprüfe die nächsten 5 Sekunden..");
				checkclickdspeed = true;
			}
		}
		return true;
	}
	
	
	
	private int i = 0;
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		
		if(checkclickdspeed){
			i++;
			if(i > 0){
				return;
			}
			new BukkitRunnable() {
				
				@Override
				public void run() {
					
					checkclickdspeed = false;
					
					Bukkit.broadcastMessage("Die Clickspeed liegt bei " + i + " alle 5 Sekunden.");
					i = 0;
				}
			}.runTaskLater(MiniGames.getInstance(), 20*5);
		}
	}

}
