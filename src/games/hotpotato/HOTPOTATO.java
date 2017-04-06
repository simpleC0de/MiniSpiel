package games.hotpotato;

import java.util.ArrayList;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class HOTPOTATO implements Game {

	private boolean aus = false;
	EntityClickListener events;
	private static HOTPOTATO Kartoffel;
	private static String prefix;
	private Games game;
	private MutableInt mi;

	@Override
	public void start() {
		game = Games.valueOf(this.getClass().getSimpleName());
		mi = new MutableInt();
		Kartoffel = this;
		prefix = game.getPrefix();
		mi.setValue(10);

		new BukkitRunnable() {

			@Override
			public void run() {
				mi.decrement();
				Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.RED + "HotPotato" + ChatColor.GOLD + "]"
						+ ChatColor.BLUE + "Die Kartoffeln sind in " + ChatColor.GRAY + mi.intValue() + ChatColor.BLUE
						+ " Sekunden fertig");
				if (mi.intValue() == 1) {

					MiniGames.getInstance().getGameManager().updatePlayers();
					registerListener();
					ArrayList<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(Games.HOTPOTATO);
					World gameWorld = MiniGames.getInstance().getMapManager().getCurrentGameWorld(Games.HOTPOTATO);
					for (Player all : Bukkit.getOnlinePlayers()) {
						Location loc = locs.get(0);
						loc.setWorld(gameWorld);
						all.teleport(loc);
						all.playSound(all.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 0.5F);
						all.spigot().setCollidesWithEntities(true);
					}

					MiniGames.getInstance().getGameManager().setisIngame(true);
					cancel();
				}
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);

	}

	private int end = 0;

	@Override
	public void end() {
		if(aus){
			return;
		}
		if (end == 1) {
			return;
		}
		Bukkit.broadcastMessage(game.getPrefix() + "§d" + game.toString() + " §6ist zuende!");
		unregisterListener();
		end = 1;
		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				aus = true;
				Bukkit.broadcastMessage(
						game.getPrefix() + "Ein neues Spiel wird in §c" + i + " §6Sekunden ausgesucht!");
				if (i == 10 || (i <= 5 && i != 0)) {
					System.out.println("Kartoffel 1");
					MiniGames.getInstance().getGameManager().updatePlayers();
					System.out.println("Kartoffel 2");
					MiniGames.getInstance().getGameManager().createNewGame();
					System.out.println("Kartoffel 3");
					MiniGames.getInstance().getGameManager().setisIngame(false);
					System.out.println("Kartoffel 4");
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
					System.out.println("Kartoffel 5");
					for (Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()) {
						MiniGames.getInstance().getPunkteManager().addPunkte(all, 4);
					}
					System.out.println("Kartoffel 6");

					for (Player all : Bukkit.getOnlinePlayers()) {
						try {
							int points = MiniGames.getInstance().getPunkteManager().getPunkte(all);
							System.out.println("Spieler " + all.getCustomName() + " hat " + points
									+ " Punkte, diese Runde  bekommen: 4");
						} catch (Exception ex) {
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
		events = new EntityClickListener();

	}

	@Override
	public void unregisterListener() {
		events.unregister();

	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub

	}

	public static HOTPOTATO getInstance() {
		return Kartoffel;
	}

	public static String getGamePrefix() {
		return prefix;
	}

}
