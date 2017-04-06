package games.blockparty;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import games.spleef.InteractListener;
import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class BLOCKPARTY implements Game{

	private boolean aus = false;
	private static BLOCKPARTY instance;
	BlockListener bl;
	private HashMap<Player, Block> onBlock;
	Games game;
	@Override
	public void start() {
		onBlock = new HashMap<>();
		game = Games.valueOf(this.toString());
	//	game = Games.BLOCKPARTY;
		
		
		registerListener();
		
		
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				i--;
				if(i == 0){
					List<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(game);
					Location spawnlocation = locs.get(0);
					spawnlocation.setWorld(MiniGames.getInstance().getMapManager().getCurrentGameWorld(game));
					for(Player all : Bukkit.getOnlinePlayers()){
						all.teleport(spawnlocation);
					}
					MiniGames.getInstance().getGameManager().setisIngame(true);
					BlockListener.getInstance().selectScheduler();
					MiniGames.getInstance().getGameManager().updatePlayers();
					
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
				Bukkit.broadcastMessage(game.getPrefix() + "Ein neues Spiel wird in §c" + i + " §6Sekunden ausgesucht!");
				if(i == 0){
					MiniGames.getInstance().getGameManager().createNewGame();
					MiniGames.getInstance().getGameManager().setisIngame(false);
					MiniGames.getInstance().getGameManager().updatePlayers();
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						MiniGames.getInstance().getPunkteManager().addPunkte(all, 4);
					}
					
					
					for(Player all : Bukkit.getOnlinePlayers()){
						try{
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
		}.runTaskTimer(MiniGames.getInstance(), 40, 20);
		
	}

	@Override
	public void registerListener() {
		bl = new BlockListener();
		
	}

	@Override
	public void unregisterListener() {
		bl.unregister();
		
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
		
	}

	public static BLOCKPARTY getInstance(){
		return instance;
	}
	public HashMap<Player, Block> getonBlock(){
		return onBlock;
	}
	public Games getGame(){
		return game;
	}

}
