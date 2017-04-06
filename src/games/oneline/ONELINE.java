package games.oneline;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import games.util.GamePlayer;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class ONELINE implements Game{

	private Games game;
	private GamePlayer first, second;
	private ArrayList<Player> played;
	private boolean lastGame;
	
	@Override
	public void start() {
		game = Games.valueOf(this.getClass().toString().split(".")[2]);
		waitScheduler();
	}
		
	private void waitScheduler(){
		int i = 10;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(i == 10 || i <= 5){
					Bukkit.broadcastMessage(game.getPrefix() + "Das Spiel startet in " + i + " Sekunden");
					if(i == 1) {
						Bukkit.getOnlinePlayers().forEach(p -> p.teleport(MiniGames.getInstance().getMapManager().getGameSpawns(game).get(0).add(0, 5, 0)));
						this.cancel();
						newFight();
					}
				}
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}
	
	private void newFight(){
		if(played.size() == MiniGames.getInstance().getGameManager().getIngamePlayers().size()-1){
			lastGame = true;
		}
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerListener() {

	}

	@Override
	public void unregisterListener() {

	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
	}

	private void waitCountdown(Location loc, boolean newFight){
		
	}
	
	private void startFight(boolean newFight){ 

	}
	private void countdown(){

	}
	
	private void stopFight(Player looser){

	}
	
	private void updateLocalWinner(Player p){

	}
	
}
