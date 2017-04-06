package games.knockbackffa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import net.minesucht.main.MiniGames;

public class KnockBackFFAListener extends GameListener {

	private ItemStack stick = new ItemStack(Material.STICK, 1);
	ItemMeta met = stick.getItemMeta();
	private KNOCKBACKFFA plugin;
	private double maxY;
	private boolean ended;
	private static KnockBackFFAListener instance;

	public KnockBackFFAListener() {
		ended = false;
		instance = this;
		ArrayList<Location> loc = MiniGames.getInstance().getMapManager()
				.getGameSpawns(KNOCKBACKFFA.getInstance().getGame());
		Location location = loc.get(0);
		maxY = location.getY() - 10;
		KNOCKBACKFFA.getInstance().addSticksforAll();

	}

	public static KnockBackFFAListener getInstance() {
		return instance;
	}

	public void startRoundTimer() {
		new BukkitRunnable() {
			int i = 180;

			@Override
			public void run() {

				i--;
				if (i == 120) {
					ended = false;
					Bukkit.broadcastMessage(KNOCKBACKFFA.getInstance().getGame().getPrefix() + ChatColor.AQUA
							+ "Das Spiel endet in " + ChatColor.GRAY + "2 Minuten " + ChatColor.AQUA);
				}
				if (i == 60) {
					ended = false;
					Bukkit.broadcastMessage(KNOCKBACKFFA.getInstance().getGame().getPrefix() + ChatColor.AQUA
							+ "Das Spiel endet in " + ChatColor.GRAY + "1 Minute " + ChatColor.AQUA);
				}
				if (i == 30) {
					ended = false;
					Bukkit.broadcastMessage(KNOCKBACKFFA.getInstance().getGame().getPrefix() + ChatColor.AQUA
							+ "Das Spiel endet in " + ChatColor.GRAY + "30 Sekunden " + ChatColor.AQUA);
				}
				if (i < 11) {
					ended = false;
					Bukkit.broadcastMessage(KNOCKBACKFFA.getInstance().getGame().getPrefix() + ChatColor.AQUA
							+ "Das Spiel endet in " + ChatColor.GRAY + i + " Sekunden" + ChatColor.AQUA);
				}
				if (i == 0) {
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.teleport(MiniGames.getInstance().getMapManager()
								.getGameSpawns(KNOCKBACKFFA.getInstance().getGame()).get(0));
					}
					ended = true;
					// Sorthashmap get best players return to lobby
					try {
						Map<Player, Integer> winners = sortMapByValue(KNOCKBACKFFA.getInstance().getStats());
						new BukkitRunnable() {

							@Override
							public void run() {
								System.out.println("Run 1");

								Entry<Player, Integer> first = null;
								Entry<Player, Integer> second = null;
								Entry<Player, Integer> third = null;

								for (Entry<Player, Integer> en : winners.entrySet()) {
									if (first == null) {
										first = en;
									} else if (second == null) {
										second = en;
									} else if (third == null) {
										third = en;
									} else
										break;
								}

								Bukkit.broadcastMessage(MiniGames.getInstance().getGameManager().getPluginPrefix()
										+ "Platz #01" + " " + first.getKey().getCustomName());
								Bukkit.broadcastMessage(MiniGames.getInstance().getGameManager().getPluginPrefix()
										+ "Platz #02" + " " + second.getKey().getCustomName());
								Bukkit.broadcastMessage(MiniGames.getInstance().getGameManager().getPluginPrefix()
										+ "Platz #03" + " " + third.getKey().getCustomName());

								System.out.println("Run 2");
								MiniGames.getInstance().getPunkteManager().addPunkte(first.getKey(), 15);
								if (winners.size() >= 2) {
									MiniGames.getInstance().getPunkteManager().addPunkte(second.getKey(), 12);
								}
								if (winners.size() >= 3) {
									MiniGames.getInstance().getPunkteManager().addPunkte(third.getKey(), 9);
								}
								// ArrayIndexoutOfBVounds
								System.out.println("Run 4");
								MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
								for (Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()) {

									MiniGames.getInstance().getPunkteManager().addPunkte(all, 4);
								}

								System.out.println("Run 5");

								KNOCKBACKFFA.getInstance().end();

							}
						}.runTaskLater(MiniGames.getInstance(), 80);
						cancel();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 20, 20);
	}

	public static Map<Player, Integer> sortMapByValue(Map<Player, Integer> unsortMap) {
		List<Entry<Player, Integer>> list = new LinkedList<Entry<Player, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Entry<Player, Integer>>() {
			public int compare(Entry<Player, Integer> o1, Entry<Player, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		Map<Player, Integer> sortedMap = new LinkedHashMap<Player, Integer>();
		for (Entry<Player, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private HashMap<Player, Player> attacks = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (MiniGames.getInstance().getGameManager().getLocalSpectators().contains(e.getEntity())
				|| MiniGames.getInstance().getGameManager().getGlobalSpectators().contains(e.getEntity())) {
			e.setCancelled(true);
			e.getEntity()
					.sendMessage(KNOCKBACKFFA.getInstance().getGame().getPrefix() + " §eDu kannst niemanden schlagen!");
		}
		if (MiniGames.getInstance().getGameManager().getIngamePlayers().contains(e.getEntity())) {
			e.setCancelled(false);
		}
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			e.setCancelled(false);
			Player damager = (Player) e.getDamager();
			System.out.println(damager.getName() + "Zeile 138");
			if (damager.getItemInHand().getType() == Material.STICK) {
				System.out.println("Ist stick");
				attacks.put((Player) e.getEntity(), (Player) e.getDamager());
				System.out.println("Zeile 140");
				return;
			} else {
				System.out.println("Kein Stick");
				return;
			}

		} else {
			System.out.println("Kein Spieler");
			return;
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.setCancelled(true);
		event.getItemDrop().remove();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent e) {
		if (ended) {
			e.setCancelled(true);
		} else {
			e.setCancelled(false);
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (e.getCause() == DamageCause.VOID) {
					if (MiniGames.getInstance().getGameManager().getIngamePlayers().contains((Player) e.getEntity())) {
						p.setHealth(0.0);
					} else {
						e.getEntity().teleport(MiniGames.getInstance().getGameManager().getIngamePlayers().get(0));
						return;
					}
				} else {
					p.setHealth(20.0);
					return;
				}
			} else {
				return;
			}
		}

	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(
				MiniGames.getInstance().getMapManager().getGameSpawns(KNOCKBACKFFA.getInstance().getGame()).get(0));
	}

	private List<Player> alreadyIn = new ArrayList<>();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent e) {
		try {
			if (attacks.containsKey((Player) e.getEntity())) {
				if (alreadyIn.contains(e.getEntity())) {
					e.setDeathMessage("");
					return;
				} else {

					KNOCKBACKFFA.getInstance().getStats().put(attacks.get(e.getEntity()),
							KNOCKBACKFFA.getInstance().getStats().get(attacks.get(e.getEntity())) + 1);
					attacks.get(e.getEntity()).playSound(attacks.get(e.getEntity()).getLocation(), Sound.LEVEL_UP, 1,
							0.5F);
					performRespawn((Player) e.getEntity());
					KNOCKBACKFFA.getInstance().addItem(e.getEntity());
					e.setDeathMessage(KNOCKBACKFFA.getInstance().getGame().getPrefix() + ChatColor.AQUA
							+ attacks.get(e.getEntity()).getName() + " hat " + ChatColor.GRAY + e.getEntity().getName()
							+ ChatColor.AQUA + " getötet!" + ChatColor.DARK_GRAY + " [" + ChatColor.RED
							+ KNOCKBACKFFA.getInstance().getStats().get(attacks.get(e.getEntity()))
							+ ChatColor.DARK_GRAY + "]");
					alreadyIn.add(e.getEntity());
					remove(e.getEntity());
					return;
				}

			} else {
				performRespawn((Player) e.getEntity());
				tpToStartPoint(e.getEntity());
				KNOCKBACKFFA.getInstance().addItem(e.getEntity());
				e.setDeathMessage("else");
				return;
			}
		} catch (Exception ex) {
			e.setDeathMessage("");
			ex.printStackTrace();
		}

	}

	public void remove(Player p) {
		new BukkitRunnable() {

			@Override
			public void run() {
				alreadyIn.remove(p);

			}
		}.runTaskLater(MiniGames.getInstance(), 10);
	}

	public void performRespawn(Player p) {
		new BukkitRunnable() {

			@Override
			public void run() {
				((CraftPlayer) p).getHandle().playerConnection
						.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
				KNOCKBACKFFA.getInstance().addItem(p);
			}
		}.runTaskLater(MiniGames.getInstance(), 5);
	}

	public void tpToStartPoint(Player p) {
		ArrayList<Location> loc = MiniGames.getInstance().getMapManager()
				.getGameSpawns(KNOCKBACKFFA.getInstance().getGame());
		new BukkitRunnable() {

			@Override
			public void run() {
				p.teleport(loc.get(0));
				p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 0.5F);

			}
		}.runTaskLater(MiniGames.getInstance(), 10);
	}

	private List<Player> safe = new ArrayList<>();

	public void deleteFromSafe(Player p) {
		if (safe.contains(p)) {
			safe.remove(p);
		} else {
		}
	}

}
