 package games.wettfischen;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class WETTFISCHEN implements Game{

	private boolean aus = false;
	private static WETTFISCHEN instance;
	CaughtFish cf;
	private Games game;
	@Override
	public void start() {
		instance = this;
		game = Games.valueOf(this.getClass().getSimpleName());
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				i--;
				
				Bukkit.broadcastMessage(ChatColor.GOLD + game.getPrefix() + " startet in " + i + " Sekunden!");
					
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

	public HashMap<Player, Integer> stats = new HashMap<>();
	@Override
	public void registerListener() {
		cf = new CaughtFish();
	}

	@Override
	public void unregisterListener() {
		cf.unregister();
		
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
	}
	
	
	public static WETTFISCHEN getInstance(){
		return instance;
	}
	public Games getGame(){
		return game;
	}
	

}
