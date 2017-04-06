package games.spleef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import games.knockbackffa.KNOCKBACKFFA;
import games.util.Game;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class SPLEEF implements Game{

	private boolean aus = false;
	ItemStack is = new ItemStack(Material.IRON_SPADE, 1);
	ItemMeta met = is.getItemMeta();
	private static SPLEEF instance;
	private boolean save;
	InteractListener il;
	Games game;
	@Override
	public void start() {
		save = true;
		game = Games.valueOf(this.getClass().getSimpleName());
		instance = this;
		met.setDisplayName(ChatColor.RED + "DER Spaten.");
		is.setItemMeta(met);
		
		
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				i--;
				if(i < 5){
					Bukkit.broadcastMessage(game.getPrefix() + " Spleef startet in " + i + " Sekunden!");
				}
				if(i == 0){
					List<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(game);
					Location spawnlocation = locs.get(0);
					spawnlocation.setWorld(MiniGames.getInstance().getMapManager().getCurrentGameWorld(game));
					for(Player all : Bukkit.getOnlinePlayers()){
						all.teleport(spawnlocation);
						System.out.println("Spieler: " + all.getCustomName() + " Teleportiert");
						all.spigot().setCollidesWithEntities(false);
					}
					
					registerListener();
					MiniGames.getInstance().getGameManager().setisIngame(true);
					InteractListener.getInstance().schutzZeitTimer();
					setItemtoPlayer();
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
		
		for(Player all : Bukkit.getOnlinePlayers()){
			all.teleport(MiniGames.getInstance().getMapManager().getGameSpawns(game).get(0));
			System.out.println("85main game: " + SPLEEF.getInstance().getGame().toString());
			all.getInventory().clear();
		}
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				aus = true;
				if(i == 10 || (i <= 5 && i !=0)){
					
						
					
				}
				Bukkit.broadcastMessage(game.getPrefix() + "Ein neues Spiel wird in §c" + i + " §6Sekunden ausgesucht!");
				if(i == 0){
					unregisterListener();
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
		il = new InteractListener();
		
	}

	@Override
	public void unregisterListener() {
		il.unregister();
		
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub
		
	}
	
	public static SPLEEF getInstance(){
		return instance;
	}
	
	public void setItemAfterDeath(Player p){
		p.getInventory().clear();
		for(int i = 0; i < 9; i++){
		
			p.getInventory().setItem(i, is);
		}
	}
	
	public void setItemtoPlayer(){
		for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
			System.out.println(all.getCustomName() + " Items  bekommen. #1");
			for(int i = 0; i < 9; i++){
				all.getInventory().setItem(i, is);
				System.out.println(all.getCustomName() + " Items  bekommen.");
			}
		}
	}
	
	public void performRespawn(Player p){
		MiniGames.getInstance().getGameManager().setLocalSpectator(p);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				((CraftPlayer)p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
				KNOCKBACKFFA.getInstance().addItem(p);
			}
		}.runTaskLater(MiniGames.getInstance(), 5);
	}
	
	
	
	public Games getGame(){
		return game;
	}
	
	public ItemStack getItem(){
		return is;
	}
	public boolean getSave(){
		return save;
	}
	public void setSave(boolean b){
		save = b;
	}
	
	public void setPunkte(Player winner){
		Random r = new Random();
		MiniGames.getInstance().getPunkteManager().addPunkte(winner, 15);
		for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
			if(all == winner){
				return;
			}else{
				int i = r.nextInt(12);
				MiniGames.getInstance().getPunkteManager().addPunkte(all, i);
			}
		}
		
	}

}
