package games.pferderennen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import games.knockbackffa.KnockBackFFAListener;
import games.util.Game;
import net.md_5.bungee.api.ChatColor;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class PFERDERENNEN implements Game{

	private boolean aus = false;
	private static PFERDERENNEN instance;
	PferdeListener Pl;
	private Games game;
	public List<Player> winner = new ArrayList<>();
	@Override
	public void start() {
		instance = this;
		game = Games.valueOf(this.getClass().getSimpleName());
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				i--;
				
				if(i == 10 || (i <= 5 && i !=0)){
					Bukkit.broadcastMessage(game.getPrefix() + " §6startet in §b" + i + " §6Sekunden!");
				}
					
				if(i == 0){

					List<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(game);
					Location spawnlocation = locs.get(0);
					spawnlocation.setWorld(MiniGames.getInstance().getMapManager().getCurrentGameWorld(game));
					for(Player all : Bukkit.getOnlinePlayers()){
						all.teleport(spawnlocation);
					}
					Bukkit.getOnlinePlayers().stream().forEach(name -> name.spigot().setCollidesWithEntities(false));
					
					
					registerListener();
					MiniGames.getInstance().getGameManager().updatePlayers();
					MiniGames.getInstance().getGameManager().setisIngame(true);
					cancel();
				}
				
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
		
	}

	@Override
	public void end() {
		if(aus){
			return;
		}
		System.out.println("End 1");
		for(Player all : Bukkit.getOnlinePlayers()){
			all.getInventory().clear();
		}
		Bukkit.broadcastMessage(game.getPrefix() + "§d" + game.toString() + " §6ist zuende!");
		
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				aus = true;
				Bukkit.broadcastMessage(game.getPrefix() + "Ein neues Spiel wird in §c" + i + " §6Sekunden ausgesucht!");
				if(i == 0){
					MiniGames.getInstance().getGameManager().createNewGame();
					MiniGames.getInstance().getGameManager().setisIngame(false);
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
					MiniGames.getInstance().getGameManager().updatePlayers();
					unregisterListener();
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						try{
							if(all != winner.get(0) && all != winner.get(1) && all != winner.get(2)){
								MiniGames.getInstance().getPunkteManager().addPunkte(all, 6);
							}else{
								MiniGames.getInstance().getPunkteManager().addPunkte(all, 12);
							}
							int points = MiniGames.getInstance().getPunkteManager().getPunkte(all);
							System.out.println("Spieler " + all.getCustomName() + " hat " + points + " Punkte, diese Runde  bekommen: 4");
						}catch(Exception ex){
							ex.printStackTrace();
						}
						
					}
					cancel();
				}
				i--;
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
		
	}

	@Override
	public void registerListener() {
		Pl = new PferdeListener();
		
	}

	@Override
	public void unregisterListener() {
		Pl.unregister();
		
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
		
	}
	
	public static PFERDERENNEN getInstance(){
		return instance;
	}
	public Games getGame(){
		return game;
	}

}
