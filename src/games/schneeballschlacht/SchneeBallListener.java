
package games.schneeballschlacht;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.main.MiniGames;

public class SchneeBallListener extends GameListener {

	private static SchneeBallListener instance;
	private boolean schutzzeit = true;

	public SchneeBallListener() {
		instance = this;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
			return;
		} else {
			e.setCancelled(false);
			return;
		}
	}

	@EventHandler
	public void hitEvent(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball) {
			Snowball s = (Snowball) e.getEntity();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void entityDamagebyEntity(EntityDamageByEntityEvent e) {
		if (schutzzeit) {
			e.setCancelled(true);
			return;
		} else {

			Entity snowball = e.getDamager();

			if (snowball instanceof Snowball) {
				e.setCancelled(false);
				Player hit = (Player) e.getEntity();
				if (((Snowball) snowball).getShooter() instanceof Player) {
					Player shooter = (Player) ((Snowball) snowball).getShooter();
					if (shooter.equals(hit)) {
						e.setCancelled(true);
						return;
					} else {
						e.setCancelled(false);
						System.out.println("Z46 Werfer: " + shooter.getCustomName() + " Hitted: "
								+ hit.getCustomName() + " Entity: " + snowball.toString());
						shooter.getInventory().removeItem(new ItemStack(Material.SNOW_BALL, 1));
						hit.getInventory().clear();
						MiniGames.getInstance().getGameManager().setLocalSpectator(hit);
						hit.sendTitle("§cDu bist ausgeschieden!", "§4Killer: §6" + shooter.getCustomName());
						hit.playSound(hit.getLocation(), Sound.ANVIL_LAND, 1, 1);
						if (MiniGames.getInstance().getGameManager().getIngamePlayers().size() <= 1) {
							giveSnows = false;
							if (MiniGames.getInstance().getGameManager().getIngamePlayers().size() == 0) {
								endGame();
							}
							Player winner = MiniGames.getInstance().getGameManager().getIngamePlayers().get(0);
							Bukkit.broadcastMessage(SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.BLUE
									+ winner.getName() + " hat gewonnen!");
							for (Player all : Bukkit.getOnlinePlayers()) {
								all.getInventory().clear();
							}
							endGame();
							return;
						}
						System.out.println("Werfer: " + shooter.getCustomName() + " Hitted: " + hit.getCustomName()
								+ " Entity: " + snowball.toString());

						System.out.println(
								"Spielerliste " + MiniGames.getInstance().getGameManager().getIngamePlayers().size());
						Bukkit.broadcastMessage(
								SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.GOLD + shooter.getName() + ChatColor.AQUA
										+ " hat " + ChatColor.RED + hit.getName() + ChatColor.AQUA + " ausgeschaltet");
						hit.getLocation().getWorld().createExplosion(hit.getLocation(), 3);
						hit.getInventory().clear();
						if (MiniGames.getInstance().getGameManager().getIngamePlayers().size() <= 1) {
							endGame();
							return;
						}
						shooter.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
						shooter.playSound(shooter.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
					}

				} else {

					return;
				}
			} else {
				System.out.println("Kein Schneeball");
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void blockExplode(BlockExplodeEvent e) {
		e.blockList().clear();
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		e.setDeathMessage("");
	}

	@EventHandler
	public void launch(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() instanceof Player) {
			if (e.getEntity() instanceof Snowball) {

				if (MiniGames.getInstance().getGameManager().getIngamePlayers().size() == 1) {
					Player winner = MiniGames.getInstance().getGameManager().getIngamePlayers().get(0);
					Bukkit.broadcastMessage(
							SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.BLUE + winner.getName() + " hat gewonnen!");
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.getInventory().clear();
					}
					endGame();
					return;

				}
			} else {
				return;
			}

		} else {
			return;
		}
	}

	public void schutzzeitTimer() {
		new BukkitRunnable() {
			int i = 20;

			@Override
			public void run() {
				i--;
				if (i <= 5) {
					Bukkit.broadcastMessage(SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.GOLD
							+ "Die Schutzzeit endet in " + ChatColor.AQUA + i + ChatColor.GOLD + " Sekunden!");
				}
				if (i == 1) {
					Bukkit.broadcastMessage(
							SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.GOLD + "Die Schutzzeit ist vorbei!");
					schutzzeit = false;
					setItemstoPlayer();
					giveSnowball();
					cancel();
				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	public void setItemstoPlayer() {
		for (int i = 0; i < MiniGames.getInstance().getGameManager().getIngamePlayers().size(); i++) {
			Player p = MiniGames.getInstance().getGameManager().getIngamePlayers().get(i);
			p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 5));
		}
	}

	public void endGame() {
		Player winner = MiniGames.getInstance().getGameManager().getIngamePlayers().get(0);
		new BukkitRunnable() {
			int i = 15;

			@Override
			public void run() {
				i--;
				if (i == 14) {
					Bukkit.broadcastMessage(
							SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.GOLD + "" + ChatColor.UNDERLINE
									+ winner.getName() + ChatColor.RESET + ChatColor.GOLD + " hat das Spiel gewonnen!");
				}
				if (i < 10) {
					Bukkit.broadcastMessage(SCHNEEBALLSCHLACHT.getGamePrefix() + ChatColor.GOLD + "Das Spiel endet in "
							+ ChatColor.GRAY + i + ChatColor.GOLD + " Sekunden!");
				}

				if (i == 1) {
					MiniGames.getInstance().getPunkteManager().addPunkte(winner, 9);
					SCHNEEBALLSCHLACHT.getInstance().end();
					cancel();
				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	public static SchneeBallListener getInstance() {
		return instance;
	}

	private boolean giveSnows = true;

	public void giveSnowball() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (!giveSnows) {
					cancel();
				}
				for (Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()) {
					all.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20 * 3);
	}

}
