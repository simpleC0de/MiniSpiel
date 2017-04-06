package games.freefall;

import java.util.ArrayList;
import java.util.logging.Handler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class MoveListener extends GameListener{

	private FREEFALL plugin;
	private boolean run;
	private boolean gamePlayed = false;
	public MoveListener(FREEFALL plugin){
		this.plugin = plugin;
		run = false;
	}
	private boolean trollolol = false;
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(MiniGames.getInstance().getGameManager().getIngamePlayers().contains(p)){
			// Spieler ist ingame
			System.out.println("z47 -> winner size -> " + plugin.winner.size());
			if(p.getLocation().add(0, 1, 0).getBlock().getRelative(BlockFace.DOWN).getType() == Material.WATER || p.getLocation().add(0, 1, 0).getBlock().getRelative(BlockFace.DOWN).getType() == Material.STATIONARY_WATER){			
				if(plugin.winner.contains(e.getPlayer())){
					
				}
				else{
					if(plugin.winner.size() == 3){
						return;
					}
					else{
						plugin.winner.add(p);
						for(Player all : Bukkit.getOnlinePlayers()){
							all.sendTitle("§2" + p.getCustomName(), "§8ist unten angekommen");
						}
						if(plugin.winner.size() == 3){
							
							Bukkit.broadcastMessage(ChatColor.GOLD + "Platz 1: " + ChatColor.GRAY + FREEFALL.getInstance().winner.get(0).getCustomName());
							Bukkit.broadcastMessage(ChatColor.GOLD + "Platz 2: " + ChatColor.GRAY + FREEFALL.getInstance().winner.get(1).getCustomName());
							Bukkit.broadcastMessage(ChatColor.GOLD + "Platz 3: " + ChatColor.GRAY + FREEFALL.getInstance().winner.get(2).getCustomName());
							
							if(trollolol){
								return;
							}else{
								if(run){
									return;
								}else{
									run = true;
									new BukkitRunnable() {
										
										@Override
										public void run() {
											
											FREEFALL.getInstance().end();
											gamePlayed = true;
											trollolol = true;
											
										}
									}.runTaskLater(MiniGames.getInstance(), 40);
								}
								
							}
							
							return;
							
						}else{
							
						}
						
					}
					
				}
				
			}else{
				return;
			}
			
		}
		else{
			// Spieler ist Local oder Global Spectator.
			return;
		}
	}
	
	public void trommelWirbel(Player p){
		for(double d = 0.1; d < 1; d =+ 0.1){
			p.playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 1, (float) d);
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		e.setDeathMessage("");
	}
	
	@EventHandler
	public void damageEvent(EntityDamageEvent e){
		if(gamePlayed){
			e.setCancelled(true);
			HandlerList.unregisterAll(this);
			System.out.println("Freefall bug");
			return;
		}
		if(e.getCause() == DamageCause.FALL){
			e.setCancelled(false);
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				e.setDamage(20.0);
				performRespawn(p);
			}
		}else{
			e.setCancelled(true);
		}
	}
	
	public void performRespawn(Player p){
			new BukkitRunnable() {
				
				@Override
				public void run() {
					((CraftPlayer)p).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
					tpToStartPoint(p);
				}
			}.runTaskLater(MiniGames.getInstance(), 5);
	}
	
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		
		Location spawnLoc = MiniGames.getInstance().getMapManager().getGameSpawns(FREEFALL.getInstance().getGame()).get(0);
		e.setRespawnLocation(spawnLoc);
	}
	public void tpToStartPoint(Player p){
		ArrayList<Location> loc = MiniGames.getInstance().getMapManager().getGameSpawns(plugin.getGame());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				p.teleport(loc.get(0));
				p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 0.5F);
				
			}
		}.runTaskLater(MiniGames.getInstance(), 10);
	}
}
