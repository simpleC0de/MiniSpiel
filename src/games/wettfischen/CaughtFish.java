package games.wettfischen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minesucht.main.MiniGames;

public class CaughtFish extends GameListener {

	private List<ItemStack> items = new ArrayList<ItemStack>();

	public CaughtFish() {
		addItems();
		addMsg();
		angelZeit();
		for(Player all : Bukkit.getOnlinePlayers()){
			states.put(all, false);
		}
		}

	public void angelZeit() {
		new BukkitRunnable() {
			int i = 180;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (i == 180) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "Ihr könnt noch 3 Minuten Angeln!");
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						all.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
					}
				}
				if (i == 120) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "Ihr könnt noch 2 Minuten Angeln!");
				}
				if (i == 60) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "Ihr könnt noch 1 Minuten Angeln!");
				}
				if (i == 30) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "Ihr könnt noch 30 Sekunden Angeln!");
				}
				if (i == 15) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "Ihr könnt noch 15 Sekunden Angeln!");
				}
				if (i < 5) {
					Bukkit.broadcastMessage(WETTFISCHEN.getInstance().getGame().getPrefix() + "" + i + "...");
				}
				i--;
				if (i == 0) {
					fishingTime = true;
					equipTime();
					for(Player all : Bukkit.getOnlinePlayers()){
						all.getInventory().remove(new ItemStack(Material.FISHING_ROD));
					}
					cancel();
				}
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	private boolean schutzZeit = true;

	public void equipTime() {
		new BukkitRunnable() {
			int i = 60;

			@Override
			public void run() {
				i--;
				if (i == 59) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "1 Minute um euch auszurüsten!");
				}
				if (i == 30) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "In 30 Sekunden gehts los!");
				}
				if (i < 5) {
					Bukkit.broadcastMessage(WETTFISCHEN.getInstance().getGame().getPrefix() + "" + i + "...");
				}
				if (i == 0) {
					secondtimer();
					cancel();
				}
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}
	
	public void secondtimer(){
		new BukkitRunnable() {
			int i = 15;

			@Override
			public void run() {
				i--;
				if (i == 14) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "Verteilt euch!");
				}
				if (i < 5) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "PvP in " + i + "Sekunden!");
				}
				if (i == 0) {
					Bukkit.broadcastMessage(
							WETTFISCHEN.getInstance().getGame().getPrefix() + "In die Schlacht!");
					schutzZeit = false;
					cancel();
				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	public void addItems() {
		ItemStack a = new ItemStack(Material.DIAMOND_AXE);
		ItemStack b = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemStack c = new ItemStack(Material.DIAMOND_LEGGINGS);
		ItemStack d = new ItemStack(Material.DIAMOND_BOOTS);
		ItemStack e = new ItemStack(Material.IRON_SWORD);
		ItemStack f = new ItemStack(Material.DIAMOND);
		ItemStack g = new ItemStack(Material.IRON_INGOT);
		ItemStack h = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack i = new ItemStack(Material.IRON_BOOTS);
		ItemStack j = new ItemStack(Material.IRON_HELMET);
		ItemStack k = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack l = new ItemStack(Material.STICK);
		ItemStack m = new ItemStack(Material.STONE);
		ItemStack n = new ItemStack(Material.BAKED_POTATO);
		ItemStack o = new ItemStack(Material.GOLDEN_APPLE);
		ItemStack p = new ItemStack(Material.APPLE);
		ItemStack q = new ItemStack(Material.GOLD_INGOT);
		ItemStack r = new ItemStack(Material.COBBLESTONE);
		ItemStack s = new ItemStack(Material.BOW);
		ItemStack t = new ItemStack(Material.ARROW);
		items.add(a);
		items.add(b);
		items.add(c);
		items.add(d);
		items.add(e);
		items.add(e);
		items.add(e);
		items.add(f);
		items.add(f);
		items.add(f);
		items.add(g);
		items.add(g);
		items.add(g);
		items.add(g);
		items.add(g);
		items.add(h);
		items.add(h);
		items.add(h);
		items.add(i);
		items.add(i);
		items.add(j);
		items.add(j);
		items.add(j);
		items.add(j);
		items.add(j);
		items.add(k);
		items.add(k);
		items.add(k);
		items.add(k);
		items.add(l);
		items.add(l);
		items.add(l);
		items.add(l);
		items.add(l);
		items.add(l);
		items.add(m);
		items.add(m);
		items.add(m);
		items.add(m);
		items.add(n);
		items.add(n);
		items.add(n);
		items.add(n);
		items.add(o);
		items.add(o);
		items.add(o);
		items.add(o);
		items.add(p);
		items.add(p);
		items.add(p);
		items.add(p);
		items.add(q);
		items.add(q);
		items.add(q);
		items.add(q);
		items.add(r);
		items.add(r);
		items.add(r);
		items.add(r);
		items.add(s);
		items.add(s);
		items.add(s);
		items.add(s);
		items.add(t);
		items.add(t);
		items.add(t);
		items.add(t);
	}

	private boolean fishingTime = false;
	private Random r = new Random();

	private HashMap<Player, Boolean> states = new HashMap<>();
	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if (fishingTime) {
			e.setCancelled(true);
			return;
		}
		if(e.getState() == State.FAILED_ATTEMPT){

		}
		if(e.getState() == State.FISHING){
			new BukkitRunnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if(states.get(e.getPlayer())){
						return;
					}
					states.put(e.getPlayer(), true);
					e.getPlayer().sendTitle("§" + r.nextInt(8) + "Zieh ein!", "");
					setBiteTime(e.getHook(), 20);
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DIG_SAND,1,1);
					new BukkitRunnable() {
						
						@Override
						public void run() {
							states.put(e.getPlayer(), false);
							
						}
					}.runTaskLater(MiniGames.getInstance(), 80);
					
					
				}
			}.runTaskLater(MiniGames.getInstance(), r.nextInt(240));
			
		}
		if (e.getState() == State.CAUGHT_FISH) {
			Item item = (Item) e.getCaught();
			item.setItemStack(items.get(r.nextInt(items.size())));
			e.setExpToDrop(0);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (schutzZeit) {
			e.setCancelled(true);
		} else {
			e.setCancelled(false);
		}
	}

	private HashMap<Player, Player> attacks = new HashMap<>();

	@EventHandler
	public void onDamagebyEntity(EntityDamageByEntityEvent e) {
		if (schutzZeit) {
			e.setCancelled(true);
		} else {
			if(MiniGames.getInstance().getGameManager().getIngamePlayers().contains(e.getDamager())){
				e.setCancelled(true);
				return;
			}
			if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
				Player damager = (Player) e.getDamager();
				Player p = (Player) e.getEntity();
				attacks.put(p, damager);
			} else {
				return;
			}
		}
	}

	private List<String> deathMsg = new ArrayList<>();

	public void addMsg() {
		deathMsg.add("§c brachte sich selbst um!");
		deathMsg.add("§chatte keine Lust mehr!");
		deathMsg.add("§cwar Suizidgefährdet..");
		deathMsg.add("§cstarb an den Folgen seiner Krankheit");
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		MiniGames.getInstance().getGameManager().setLocalSpectator(e.getEntity());
		if(MiniGames.getInstance().getGameManager().getIngamePlayers().size() <= 1){
			Player p = MiniGames.getInstance().getGameManager().getIngamePlayers().get(0);
			for(Player all : Bukkit.getOnlinePlayers()){
				
				all.sendTitle("§c" + p.getCustomName(), "§2hat gewonnen!");
			}
			 new BukkitRunnable() {
				
				@Override
				public void run() {
					WETTFISCHEN.getInstance().end();
					
				}
			}.runTaskLater(MiniGames.getInstance(), 80);
		}
		if (attacks.containsKey(e.getEntity())) {
			Player killer = attacks.get(e.getEntity());
			Player dead = e.getEntity();
			
			dead.sendTitle("§cDu bist raus!", "");
			if (WETTFISCHEN.getInstance().stats.containsKey(killer)) {
				int kills = WETTFISCHEN.getInstance().stats.get(killer);
				WETTFISCHEN.getInstance().stats.put(killer, kills++);
			} else {
				WETTFISCHEN.getInstance().stats.put(killer, 1);
			}
			e.setDeathMessage("§c" + killer.getCustomName() + "§2 hat §5" + dead.getCustomName() + "§4 erledigt! §8[§c"
					+ WETTFISCHEN.getInstance().stats.get(killer) + "§8]");
		} else {
			e.setDeathMessage("§8" + e.getEntity().getCustomName() + deathMsg.get(r.nextInt(deathMsg.size())));
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Location respawn = MiniGames.getInstance().getMapManager().getGameSpawns(WETTFISCHEN.getInstance().getGame())
				.get(0);
		respawn.add(0, 10, 0);
		e.setRespawnLocation(respawn);
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e){
		if(schutzZeit){
			e.setCancelled(false);
		}else{
			e.setCancelled(false);
			e.getItemDrop().remove();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onItemClick(InventoryClickEvent e){
		e.setCancelled(false);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onItmeMove(InventoryMoveItemEvent e){
		e.setCancelled(false);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void Drag(InventoryDragEvent e){
		e.setCancelled(false);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void pickUp(InventoryPickupItemEvent e){
		e.setCancelled(false);
	}
	
	
	private void setBiteTime(FishHook hook, int time) {
		if(!fishingTime){
			return;
		}
		net.minecraft.server.v1_8_R3.EntityFishingHook hookCopy = (EntityFishingHook) ((CraftEntity) hook).getHandle();
	    
	    Field fishCatchTime = null;

	    try {
	        fishCatchTime = net.minecraft.server.v1_8_R3.EntityFishingHook.class.getDeclaredField("av");
	    } catch (NoSuchFieldException e) {
	        e.printStackTrace();
	    } catch (SecurityException e) {
	        e.printStackTrace();
	    }

	    fishCatchTime.setAccessible(true);

	    try {
	        fishCatchTime.setInt(hookCopy, time);
	    } catch (IllegalArgumentException e) {
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
	        e.printStackTrace();
	    }

	    fishCatchTime.setAccessible(false);
	}

}
