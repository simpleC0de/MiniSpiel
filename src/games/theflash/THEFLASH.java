package games.theflash;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class THEFLASH implements Game{

	private boolean aus = false;
	private static THEFLASH instance;
	private ArrayList<Player> winners;
	Move mv;
	Games game;
	@Override
	public void start() {
		//game = Games.THEFLASH;
		System.out.println("Start startet");
		instance = this;
		winners = new ArrayList<>();
		registerListener();
		game = Games.valueOf(this.getClass().getSimpleName());
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				i--;
				System.out.println(i);
				
				Bukkit.broadcastMessage(ChatColor.GOLD + game.getPrefix() + " startet in " + i + " Sekunden!");
					
				if(i == 0){
					System.out.println("i == 0");
					
					List<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(game);
					System.out.println("lol");
					Location spawnlocation = locs.get(0);
					System.out.println("KeK");
					spawnlocation.setWorld(MiniGames.getInstance().getMapManager().getCurrentGameWorld(game));
					System.out.println("KAKA");
					for(Player all : Bukkit.getOnlinePlayers()){
						all.teleport(spawnlocation);
					}
					System.out.println("Sollte tpt werden >:>");
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
		Bukkit.broadcastMessage(game.getPrefix() + "§d" + game.toString() + " §6ist zuende!");
		unregisterListener();
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				aus = true;
				for(Player all : Bukkit.getOnlinePlayers()){
					all.removePotionEffect(PotionEffectType.JUMP);
				}
				Bukkit.broadcastMessage(game.getPrefix() + "Ein neues Spiel wird in §c" + i + " §6Sekunden ausgesucht!");
				if(i == 0){
					MiniGames.getInstance().getGameManager().createNewGame();
					MiniGames.getInstance().getGameManager().setisIngame(false);
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						MiniGames.getInstance().getPunkteManager().addPunkte(all, 4);
					}
					cancel();
				}
				i--;
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
		
	}

	@Override
	public void registerListener() {
		mv = new Move();
	}

	@Override
	public void unregisterListener() {
		mv.unregister();
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
		
	}

	public static THEFLASH getInstance(){
		return instance;
	}
	public ArrayList<Player> getWinners(){
		return winners;
	}
	
	
	public void setSpeed(){
		for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
			all.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 4));
		}
	}
	
	public Games getGame(){
		return game;
	}
}
