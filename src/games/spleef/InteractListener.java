package games.spleef;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.main.MiniGames;

public class InteractListener extends GameListener{

	private static InteractListener instance;
	public InteractListener(){
		instance = this;
	}
	private String prefix = SPLEEF.getInstance().getGame().getPrefix();
	public void schutzZeitTimer(){
		new BukkitRunnable() {
			int i = 10;
			@Override
			public void run() {
				i--;
				Bukkit.broadcastMessage(prefix + "Die Schutzzeit endet in " + i + " Sekunden!");
				if(i == 1){
					for(Player all : Bukkit.getOnlinePlayers()){
						all.playSound(all.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
						all.spigot().setCollidesWithEntities(false);
					}
					SPLEEF.getInstance().setItemtoPlayer();
					Bukkit.broadcastMessage(prefix + "Lasst die Schlacht beginnen!");
					SPLEEF.getInstance().setSave(false);
					cancel();
				}
				
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}
	
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(p.getItemInHand().getType() == SPLEEF.getInstance().getItem().getType() && SPLEEF.getInstance().getSave() == false){
			Class<Egg> s = Egg.class;
			p.launchProjectile(s);
			System.out.println("Blue stinkt");
		}else{
			System.out.println("Blue stinkt krass");
			return;
		}
	}
	
	
	
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e){
		if(e.getSpawnReason() == SpawnReason.EGG){
			e.setCancelled(true);
		}else{
			e.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(SPLEEF.getInstance().getSave()){
			
			e.getEntity().teleport(MiniGames.getInstance().getMapManager().getGameSpawns(SPLEEF.getInstance().getGame()).get(0));
			System.out.println("z70 game: " + SPLEEF.getInstance().getGame().toString());
			return;
		}else{
			if(e.getCause() == DamageCause.VOID && e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				p.getInventory().clear();
				e.setCancelled(false);
				p.sendTitle("§cDu bist raus!", "");
				MiniGames.getInstance().getGameManager().setLocalSpectator((Player)e.getEntity());
				if(MiniGames.getInstance().getGameManager().getIngamePlayers().size() == 1){
					Player winner = MiniGames.getInstance().getGameManager().getIngamePlayers().get(0);
					
					SPLEEF.getInstance().end();
				}
				e.getEntity().teleport(MiniGames.getInstance().getMapManager().getGameSpawns(SPLEEF.getInstance().getGame()).get(0));
		System.out.println("z81 game: " + SPLEEF.getInstance().getGame().toString());
			}else{
				e.setCancelled(true);
			}
		}
		
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(MiniGames.getInstance().getGameManager().getLocalSpectators().contains(e.getEntity()) || MiniGames.getInstance().getGameManager().getGlobalSpectators().contains(e.getEntity())){
				e.setCancelled(true);
			}
		}else{
			e.setCancelled(false);
		}
		
	}
	
	
	@EventHandler
	public void onHit(ProjectileHitEvent e){
		if(e.getEntity() instanceof Egg && SPLEEF.getInstance().getSave() == false){
			e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.SPLASH, 1, 1);
			destroyBlock(e.getEntity().getLocation().subtract(0, 1, 0).getBlock());
			destroyBlock(e.getEntity().getLocation().subtract(0, 1, 0).getBlock());
			destroyBlock(e.getEntity().getLocation().subtract(1, 1, 0).getBlock());
			destroyBlock(e.getEntity().getLocation().subtract(0, 1, 1).getBlock());
			
			destroyBlock(e.getEntity().getLocation().add(0, 1, 0).getBlock());
			destroyBlock(e.getEntity().getLocation().add(0, 1, 0).getBlock());
			destroyBlock(e.getEntity().getLocation().add(1, 1, 0).getBlock());
			destroyBlock(e.getEntity().getLocation().add(0, 1, 1).getBlock());
			
			System.out.println("Steinwerk");
		}else{
			System.out.println("Kohlefabrik");
			return;
		}
	}
	
	
	
	
	
	
	public Block destroyBlock(Block b){
		b.setType(Material.AIR);
		b.getState().update();
		return b;
	}
	
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		e.setRespawnLocation(MiniGames.getInstance().getMapManager().getGameSpawns(SPLEEF.getInstance().getGame()).get(0));
	}
	
	
	
	public static InteractListener getInstance(){
		return instance;
	}
}
