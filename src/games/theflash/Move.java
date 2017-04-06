package games.theflash;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.main.MiniGames;
import net.minesucht.managers.PunkteManager;

public class Move extends GameListener {

	private static Move instance;
	private boolean ended;
	private double maxY;

	public Move() {
		ended = false;
		maxY = MiniGames.getInstance().getMapManager().getGameSpawns(THEFLASH.getInstance().getGame()).get(0).getY()
				- 5;
	}

	private boolean endd = false;
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (p.getLocation().getY() < maxY) {
			tptoStartPoint(p);
		}
		if (b.getType() == Material.DIAMOND) {
			if (THEFLASH.getInstance().getWinners().size() == 3) {
				setWinners();
				if(endd){
					return;
				}
				THEFLASH.getInstance().end();
				endd = true;
			} else {
				if (THEFLASH.getInstance().getWinners().contains(e.getPlayer())) {
					return;
				} else {
					THEFLASH.getInstance().getWinners().add(e.getPlayer());
				}
			}
		} else {
			return;
		}
	}

	public void setWinners() {
		if (ended) {
			return;
		} else {
			ended = true;
			PunkteManager pt = MiniGames.getInstance().getPunkteManager();
			pt.addPunkte(THEFLASH.getInstance().getWinners().get(0), 15);
			pt.addPunkte(THEFLASH.getInstance().getWinners().get(1), 12);
			pt.addPunkte(THEFLASH.getInstance().getWinners().get(2), 9);

			for (Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()) {
				if (all == THEFLASH.getInstance().getWinners().get(0)
						|| all == THEFLASH.getInstance().getWinners().get(1)
						|| all == THEFLASH.getInstance().getWinners().get(2)) {

				} else {
					pt.addPunkte(all, 6);
				}
			}

			new BukkitRunnable() {
				int i = 3;

				@Override
				public void run() {
					i--;
					if (i == 2) {
						Bukkit.broadcastMessage(THEFLASH.getInstance().getGame().getPrefix() + ChatColor.GOLD
								+ "Platz #1 " + ChatColor.DARK_BLUE + THEFLASH.getInstance().getWinners().get(0));
					}
					if (i == 1) {
						Bukkit.broadcastMessage(THEFLASH.getInstance().getGame().getPrefix() + ChatColor.GOLD
								+ "Platz #2 " + ChatColor.DARK_BLUE + THEFLASH.getInstance().getWinners().get(1));
					}
					if (i == 0) {
						Bukkit.broadcastMessage(THEFLASH.getInstance().getGame().getPrefix() + ChatColor.GOLD
								+ "Platz #3 " + ChatColor.DARK_BLUE + THEFLASH.getInstance().getWinners().get(2));
					}
					if (i == -1) {
						THEFLASH.getInstance().end();
						cancel();
					}
				}
			}.runTaskTimer(MiniGames.getInstance(), 0, 20);
		}

	}

	public void tptoStartPoint(Player p) {
		Location loc = MiniGames.getInstance().getMapManager().getGameSpawns(THEFLASH.getInstance().getGame()).get(0);
		p.teleport(loc);
	}

	public static Move getInstance() {
		return instance;
	}
}
