package games.hotpotato;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.main.MiniGames;

public class EntityClickListener extends GameListener {

	private static EntityClickListener pizza;
	private Random r = new Random();

	private ItemStack pot = new ItemStack(Material.POTATO);
	private ItemMeta potmeta = pot.getItemMeta();

	public EntityClickListener() {
		potmeta.setDisplayName("§cOUCH!");
		pot.setItemMeta(potmeta);
		pizza = this;
		selectPotatoes();
		startTimer();
	}

	private boolean ended = false;

	private List<Player> kartoffeln = new ArrayList<>();

	private void selectPotatoes() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (MiniGames.getInstance().getGameManager().getIngamePlayers().size() <= 1) {
					Bukkit.broadcastMessage(HOTPOTATO.getGamePrefix()
							+ MiniGames.getInstance().getGameManager().getIngamePlayers().get(0).getCustomName()
							+ " hat das Spiel gewonnen!");
					ended = true;
					new BukkitRunnable() {

						@Override
						public void run() {
							HOTPOTATO.getInstance().end();

						}
					}.runTaskLater(MiniGames.getInstance(), 80);
				}else{
					int ingamePlayers = MiniGames.getInstance().getGameManager().getIngamePlayers().size();
					double durch = 2;
					double ergebnis = (double) ingamePlayers / durch;
					Bukkit.broadcastMessage(
							HOTPOTATO.getGamePrefix() + " Es werden " + (int) ergebnis + " Kartoffel(n) verteilt!");
					for (int i = 0; i < (int) ergebnis; i++) {
						Player kartoffel = MiniGames.getInstance().getGameManager().getIngamePlayers()
								.get(r.nextInt(ingamePlayers));
						if (kartoffeln.contains(kartoffel)) {
							i--;
						} else {
							addPotato(kartoffel);
						}
						kartoffelSpeed(kartoffel);
					}
				}
				

			}
		}.runTaskLater(MiniGames.getInstance(), 40);

	}

	private void removePotato(Player kartoffel) {
		kartoffel.getInventory().clear();
		if (kartoffeln.contains(kartoffel)) {
			kartoffeln.remove(kartoffel);
			normalSpeed(kartoffel);
			kartoffel.getInventory().clear();
		} else {
			return;
		}
	}

	private void addPotato(Player clicked) {
		
		if (kartoffeln.contains(clicked)) {
			return;
		} else {
			addItems(clicked);
			kartoffeln.add(clicked);
			clicked.playSound(clicked.getLocation(), Sound.FIRE_IGNITE, 1, 1);
			kartoffelSpeed(clicked);
		}
	}

	private void addItems(Player p) {
		p.getInventory().clear();
		for (int i = 0; i < 9; i++) {
			p.getInventory().setItem(i, pot);
		}
	}

	@SuppressWarnings("deprecation")
	private void destroyInfected() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (kartoffeln.contains(all)) {
				MiniGames.getInstance().getGameManager().setLocalSpectator(all);
				all.sendTitle("§cDas war wohl zu heiss!", "§4Du bist raus!");
				all.getWorld().createExplosion(all.getLocation(), (float) 1);
			}
		}
	}

	private void kartoffelSpeed(Player kartoffel) {
		kartoffel.getActivePotionEffects().clear();
		kartoffel.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 3));
	}

	private void normalSpeed(Player normal) {
		normal.getActivePotionEffects().clear();
		normal.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
	}

	private void startTimer() {
		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				i--;
				if (ended) {
					cancel();
				}
				if(i < 6){
					Bukkit.broadcastMessage(HOTPOTATO.getGamePrefix()+"Die Kartoffeln platzen in " + i + " Sekunden!");
				}
				if (i == 0) {
					// Lasse Spieler ausscheiden -> Neue Kartoffeln aussuchen
					destroyInfected();
					selectPotatoes();
					i = r.nextInt(25);
				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	@EventHandler
	public void onEntityHit(PlayerInteractEntityEvent e) {
		if (e.getPlayer() instanceof Player && e.getRightClicked() instanceof Player) {
			Player clicked = (Player) e.getRightClicked();
			Player clicker = (Player) e.getPlayer();
			addPotato(clicked);
			removePotato(clicker);

		} else {
			return;
		}
	}
	
	@EventHandler
	public void onEntityClick(PlayerInteractAtEntityEvent e){
		if (e.getPlayer() instanceof Player && e.getRightClicked() instanceof Player) {
			Player clicked = (Player) e.getRightClicked();
			Player clicker = (Player) e.getPlayer();
			addPotato(clicked);
			removePotato(clicker);

		} else {
			return;
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		e.blockList().clear();
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDrag(InventoryDragEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onClic(InventoryClickEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onSwitch(InventoryMoveItemEvent e){
		e.setCancelled(true);
	}

	public static EntityClickListener getInstance() {
		return pizza;
	}
}