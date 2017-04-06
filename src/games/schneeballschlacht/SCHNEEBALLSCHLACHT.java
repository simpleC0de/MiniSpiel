package games.schneeballschlacht;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class SCHNEEBALLSCHLACHT implements Game {

	private boolean aus = false;
	private static SCHNEEBALLSCHLACHT instance;
	SchneeBallListener sbl;
	private boolean hotend = false;
	private Games game;
	private static String prefix;

	@Override
	public void start() {
		instance = this;
		game = Games.valueOf(this.getClass().getSimpleName());
		prefix = game.getPrefix();
		registerListener();

		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {

				if (i == 10 || (i <= 5 && i != 0)) {
					Bukkit.broadcastMessage(game.getPrefix() + " �6startet in �b" + i + " �6Sekunden!");
				}
				i--;
				if (i == 0) {
					List<Location> locs = MiniGames.getInstance().getMapManager()
							.getGameSpawns(Games.SCHNEEBALLSCHLACHT);
					Location spawnlocation = locs.get(0);
					spawnlocation.setWorld(
							MiniGames.getInstance().getMapManager().getCurrentGameWorld(Games.SCHNEEBALLSCHLACHT));
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.teleport(spawnlocation);
					}
					MiniGames.getInstance().getGameManager().setisIngame(true);
					SchneeBallListener.getInstance().schutzzeitTimer();
					MiniGames.getInstance().getGameManager().updatePlayers();

					cancel();
				}

			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}

	@Override
	public void end() {
		if (hotend) {
			return;
		}if(aus){
			return;
		}
		Bukkit.broadcastMessage(game.getPrefix() + "�d" + game.toString() + " �6ist zuende!");

		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				aus = true;
				hotend = true;
				Bukkit.broadcastMessage(
						game.getPrefix() + "Ein neues Spiel wird in �c" + i + " �6Sekunden ausgesucht!");
				if (i == 10 || (i <= 5 && i != 0)) {

					MiniGames.getInstance().getGameManager().createNewGame();
					MiniGames.getInstance().getGameManager().setisIngame(false);
					MiniGames.getInstance().getGameManager().updatePlayers();
					MiniGames.getInstance().getGameManager().setLocalSpectatorToIngame();
					for (Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()) {
						MiniGames.getInstance().getPunkteManager().addPunkte(all, 4);
					}
					unregisterListener();
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
		sbl = new SchneeBallListener();

	}

	@Override
	public void unregisterListener() {
		sbl.unregister();

	}

	@Override
	public void registerPoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerWinner() {
		// TODO Auto-generated method stub

	}

	public static SCHNEEBALLSCHLACHT getInstance() {
		return instance;
	}

	public static String getGamePrefix() {
		return prefix;
	}

}
