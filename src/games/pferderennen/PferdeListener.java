package games.pferderennen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import games.util.GameListener;
import net.minesucht.main.MiniGames;

public class PferdeListener extends GameListener{

	private static PferdeListener pl;
	public PferdeListener(){
		pl = this;
		setPlayersHorses();
	}
	
	
	private HashMap<Player, Horse> pferde = new HashMap<>();
	
	public void setPlayersHorses(){
		for(Player all : Bukkit.getOnlinePlayers()){
			Entity pferd = all.getWorld().spawnEntity(all.getLocation(), EntityType.HORSE);
			Horse hr = (Horse)pferd;
			hr.setAdult();
			hr.setJumpStrength(0);
			hr.setTamed(true);
			hr.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			hr.setPassenger(all);	
			hr.setDomestication(1);
			hr.setOwner(all);
			hr.setCustomNameVisible(false);
			hr.setRemoveWhenFarAway(false);
			pferde.put(all, hr);
			state.put(all,false);
		}
	}
	
	private void spawnHorse(Player p){
		Entity pferd = p.getWorld().spawnEntity(locs.get(p), EntityType.HORSE);
		Horse hr = (Horse)pferd;
		hr.setAdult();
		hr.setJumpStrength(0);
		hr.setTamed(true);
		hr.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		hr.setPassenger(p);	
		CraftPlayer cp = (CraftPlayer)p;try{
			cp.getHandle().mount((net.minecraft.server.v1_8_R3.Entity) pferd);
		}catch(ClassCastException ex){
			
		}
		
		hr.setDomestication(1);
		hr.setOwner(p);
		hr.setCustomNameVisible(false);
		hr.setRemoveWhenFarAway(false);
		pferde.put(p, hr);
	}
	
	@EventHandler
	public void onDismount(EntityDismountEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			Horse pferd = pferde.get(p);
			p.teleport(pferd);
			pferd.setPassenger(p);
			e.getDismounted().setPassenger(p);
		}else{
			return;
		}
	}
	@EventHandler
	public void onVehicleExit(VehicleExitEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onInvOpen(InventoryOpenEvent e){
		if(e.getInventory().getHolder() instanceof Horse){
			e.setCancelled(true);
		}
	}
	private List<Player> winners = new ArrayList<>();
	private HashMap<Player, Location> locs = new HashMap<>();
	private HashMap<Player, Boolean> state = new HashMap<>();
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR){
			locs.put(e.getPlayer(), e.getPlayer().getLocation());
		}
		Player p = e.getPlayer();
		Horse hr = pferde.get(p);
		if(hr.getLocation().getY() < 0){
			hr.setHealth(0.0);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if(state.get(p)){
						return;
					}
					spawnHorse(p);
					state.put(p, true);
					new BukkitRunnable() {
						
						@Override
						public void run() {
							state.put(p, false);
							
						}
					}.runTaskLater(MiniGames.getInstance(), 40);
					
				}
			}.runTaskLater(MiniGames.getInstance(), 40);
			
		}
		if(hr.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STONE){
			//.out.println(p.getDisplayName() + " won!");
			if(winners.contains(p)){
				return;
			}else{
				winners.add(p);
				for(Player all : Bukkit.getOnlinePlayers()){
					all.sendTitle("§2" + p.getCustomName(), "§6hat gewonnen!");
				}
			}
			if(winners.size() >= 3){
				if(ended){
					return;
				}
				ended = true;
				new BukkitRunnable() {
					
					@Override
					public void run() {
						//.out.println("sollte enden >:>");
						PFERDERENNEN.getInstance().winner.add(winners.get(0));
						PFERDERENNEN.getInstance().winner.add(winners.get(1));
						PFERDERENNEN.getInstance().winner.add(winners.get(2));
						PFERDERENNEN.getInstance().end();
						
					}
				}.runTaskLater(MiniGames.getInstance(), 60);
				
				
				return;
			}
		}else{
			return;
		}
		
	}
	private boolean ended = false;
	public static PferdeListener getInstance(){
		return pl;
	}
}
