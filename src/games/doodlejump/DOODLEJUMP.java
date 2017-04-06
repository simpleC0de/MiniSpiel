package games.doodlejump;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class DOODLEJUMP implements Game {

	private boolean aus = false;
	private static DOODLEJUMP instance;
	private Games game;

	DoodleListener dl;

	@Override
	public void start() {
		game = Games.valueOf(this.getClass().getSimpleName());
		instance = this;
		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				i--;

				if (i == 10 || (i <= 5 && i != 0)) {
					Bukkit.broadcastMessage(game.getPrefix() + " §6startet in §b" + i + " §6Sekunden!");
				}

				if (i == 0) {
					List<Location> locs = MiniGames.getInstance().getMapManager().getGameSpawns(game);
					Location spawnlocation = locs.get(0);
					spawnlocation.setWorld(MiniGames.getInstance().getMapManager().getCurrentGameWorld(game));
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.teleport(spawnlocation);
					}
					Bukkit.getOnlinePlayers().stream().forEach(name -> name.spigot().setCollidesWithEntities(false));

					registerListener();
					MiniGames.getInstance().getGameManager().updatePlayers();
					MiniGames.getInstance().getGameManager().setisIngame(true);
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
					for (Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()) {
						MiniGames.getInstance().getPunkteManager().addPunkte(all, 4);
					}

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
		unregisterListener();
		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				aus = true;
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.removePotionEffect(PotionEffectType.JUMP);
				}
				Bukkit.broadcastMessage(
						game.getPrefix() + "Ein neues Spiel wird in §c" + i + " §6Sekunden ausgesucht!");
				if (i == 10 || (i <= 5 && i != 0)) {
					MiniGames.getInstance().getGameManager().createNewGame();
					MiniGames.getInstance().getGameManager().setisIngame(false);
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();

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
		dl = new DoodleListener();

	}

	@Override
	public void unregisterListener() {
		dl.unregister();
	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub

	}

	public static DOODLEJUMP getInstance() {
		return instance;
	}

}
