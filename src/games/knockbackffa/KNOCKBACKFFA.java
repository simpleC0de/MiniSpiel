package games.knockbackffa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import net.md_5.bungee.api.ChatColor;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class KNOCKBACKFFA implements Game{

	private boolean aus = false;
	KnockBackFFAListener kbffal;
	private static KNOCKBACKFFA instance;
	private World w;
	private ItemStack stick = new ItemStack(Material.STICK, 1);
	ItemMeta met = stick.getItemMeta();
	private HashMap<Player, Integer> stats;
	private Games game;
	
	
	 
	@Override
	public void start() {
		instance = this;
		stats = new HashMap<Player, Integer>();
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
					met.setDisplayName(ChatColor.LIGHT_PURPLE +" "+ ChatColor.MAGIC + "III" +ChatColor.RESET + " " + ChatColor.RED + "Pow"  + ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC + "III" + ChatColor.RESET);
					stick.setItemMeta(met);
					stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
					List<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(game);
					Location spawnlocation = locs.get(0);
					spawnlocation.setWorld(MiniGames.getInstance().getMapManager().getCurrentGameWorld(game));
					for(Player all : Bukkit.getOnlinePlayers()){
						all.teleport(spawnlocation);
					}
					addSticksforAll();
					Bukkit.getOnlinePlayers().stream().forEach(name -> name.spigot().setCollidesWithEntities(false));
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						stats.put(all, 0);
					}
					
					addSticksforAll();
					registerListener();
					MiniGames.getInstance().getGameManager().updatePlayers();
					MiniGames.getInstance().getGameManager().setisIngame(true);
					KnockBackFFAListener.getInstance().startRoundTimer();
					addSticksforAll();
					cancel();
				}
				
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	public void addItem(Player p){
		p.getInventory().clear();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				p.getInventory().clear();
				for(int i = 0; i < 9; i++){
					
					p.getInventory().setItem(i, stick);
				}	
			}
		}.runTaskLater(MiniGames.getInstance(), 20);
		
	}
	public void addSticksforAll(){
		for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
			all.getInventory().clear();
			for(int i = 0; i < 9; i++){
				all.getInventory().setItem(i, stick);
			}
		}
	}
	
	

	public World getGameWorld(){
		return this.w;
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
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
		
	}

	@Override
	public void registerListener() {
		kbffal = new KnockBackFFAListener();
	}

	@Override
	public void unregisterListener() {
		kbffal.unregister();
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
		
	}
	
	public Games getGame(){
		return game;
	}
	
	public static KNOCKBACKFFA getInstance(){
		return instance;
	}

	
	public HashMap<Player, Integer> getStats(){
		 return stats;
	 }
}
