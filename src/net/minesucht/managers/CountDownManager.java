package net.minesucht.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.minesucht.main.MiniGames;

public class CountDownManager {

	private static CountDownManager instance;
	private BukkitTask bt;
	private int runningTime;
	private boolean running = false;
	
	public CountDownManager(){
		running = false;
	}
	
	public void startingCountDown(int length){
		runningTime = length;
		bt = 
		new BukkitRunnable() {
			int i = length;
			int xp = length;
			@Override
			public void run() {
				i--;
					runningTime = i;
					xp--;
					
					running = true;
					if(i == 120 || i == 100 || i == 70 || i == 30 || i == 20){
						
					}
					
					
					
					if(i == 30){
						Bukkit.broadcastMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Das Spiel startet in 30 Sekunden!");
						
					}
					if(i <= 10 && i != 0){
						Bukkit.broadcastMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "Das Spiel startet in " + i + " Sekunden!");
					}
					
					
					
					
					for(Player all : Bukkit.getOnlinePlayers()){
						all.setLevel(xp);
					}
					
					if(i == 0){
						if(MiniGames.getInstance().getGameManager().getIngamePlayers().size() < 4){
							cancel();
						}
						MiniGames.getInstance().getGameManager().startInstance();
						cancel();
					}
				
				
				
			}
			
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}
	
	public boolean getRunningBoolean(){
		return running;
	}
	
	public void cancelScheduler(){
		if(running){
			bt.cancel();
		}
	}
	
	public int getRunningTime(){
		if(running){
			return runningTime;
		} else {
			return 11;
		}

	}
	
	public void setTimeForAllWorlds(){
		new BukkitRunnable() {
			
			@Override
			public void run() {
				MiniGames.getInstance().getMapManager().getWorlds().forEach((k,v) -> v.setTime(0));
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20*10);
	}
}
