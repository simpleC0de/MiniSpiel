package net.minesucht.managers;

import java.io.File;

import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.sqlite.SQLiteConfig.JournalMode;

import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class MapManager {

	private World lobby; // Lobby Map
	private Location lobbySpawn; // Lobby Spawn Location
	private HashMap<Games, World> worlds; // Welten zu den jeweiligen Spielen
	private HashMap<Games, ArrayList<Location>> mapLocations; // Locations zu
																// den
																// jeweiligen
																// Maps (Bsp:
																// Spawn
																// Locations

	public MapManager() {
		this.mapLocations = new HashMap<Games, ArrayList<Location>>(); // Init.
																		// MapLocations
																		// HashMap
		this.worlds = new HashMap<Games, World>(); // Init. GameWorld Locations
		registerLobby();
		registerLobbySpawn();
	}

	private void registerLobby() {
		this.lobby = Bukkit.getWorld(MiniGames.getInstance().getConfig().getString("Lobby.Worldname"));
	}

	public void registerGameWorlds() {

		File file;
		String[] directories = null;
		String world;
		Random rnd = new Random();
		WorldCreator creator;
		World w;

		for (int i = 0; i < MiniGames.getInstance().getGameManager().getGames().size(); i++) {

			file = new File(MiniGames.getInstance().getDataFolder() + File.separator
					+ MiniGames.getInstance().getGameManager().getGames().get(i).toString());
			directories = file.list(new FilenameFilter() {

				@Override
				public boolean accept(File current, String name) {
					return new File(current, name).isDirectory();
				}

			});

			world = directories[rnd.nextInt(directories.length)];
			try {
				// FileUtils.copyDirectory(new
				// File(MiniGames.getInstance().getDataFolder() + File.separator
				// +
				// MiniGames.getInstance().getGameManager().getGames().get(i).toString()
				// + File.separator + world), new File(new
				// File(".").getAbsolutePath()));
				File from = new File(MiniGames.getInstance().getDataFolder() + File.separator
						+ MiniGames.getInstance().getGameManager().getGames().get(i).toString() + File.separator
						+ world);
				File to = new File(MiniGames.getInstance().getServer().getWorldContainer().getAbsolutePath()
						+ File.separator + world);
				to.mkdir();

				FileUtils.copyDirectory(from, to);
			} catch (IOException e) {
				e.printStackTrace();
			}
			creator = new WorldCreator(world);

			w = creator.createWorld();

			w.setMonsterSpawnLimit(0);
			w.setDifficulty(Difficulty.PEACEFUL);

			worlds.put(MiniGames.getInstance().getGameManager().getGames().get(i), w);

		}

	}

	
	public void moveToWorld(Player p, Games game, String map) {

		File from = new File(
				MiniGames.getInstance().getDataFolder() + File.separator + game.toString() + File.separator + map);

		File to = new File(
				MiniGames.getInstance().getServer().getWorldContainer().getAbsolutePath() + File.separator + map);
		to.mkdir();

		try {
			FileUtils.copyDirectory(from, to);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		p.teleport(MiniGames.getInstance().getServer().createWorld(new WorldCreator(map)).getSpawnLocation());
	}

	public void registerGameSpawns() {
		ArrayList<Location> loc = new ArrayList<Location>();
		ArrayList<String> splitList;
		MutableInt mi = new MutableInt();
		for (int i = 0; i < worlds.size(); i++) {

			mi.setValue(i);
			splitList = (ArrayList<String>) MiniGames.getInstance().getFileConfigurationManager()
					.getGlobalConfig(MiniGames.getInstance().getGameManager().getGames().get(mi.intValue()),
							MiniGames.getInstance().getMapManager().getWorlds()
									.get(MiniGames.getInstance().getGameManager().getGames().get(mi.intValue())))
					.getFileConfiguration().getStringList("spawn");

			splitList.forEach(toSplit -> {
				loc.add(new Location(worlds.get(MiniGames.getInstance().getGameManager().getGames().get(mi.intValue())),
						Double.valueOf(toSplit.split(",")[0]), Double.valueOf(toSplit.split(",")[1]),
						Double.valueOf(toSplit.split(",")[2]), Float.valueOf(toSplit.split(",")[3]),
						Float.valueOf(toSplit.split(",")[4])));
				// System.out.println(worlds.get(MiniGames.getInstance().getGameManager().getGames().get(mi.intValue())).toString()
				// + " " + Double.valueOf(toSplit.split(",")[0]) + " " +
				// Double.valueOf(toSplit.split(",")[1]) + " " +
				// Double.valueOf(toSplit.split(",")[2]) + " " +
				// Float.valueOf(toSplit.split(",")[3]) + " " +
				// Float.valueOf(toSplit.split(",")[4]));
			});

			mapLocations.put(MiniGames.getInstance().getGameManager().getGames().get(mi.intValue()),
					new ArrayList<Location>(loc));

			loc.clear();
			splitList.clear();
		}

	}

	private void registerLobbySpawn() {
		String split = MiniGames.getInstance().getConfig().getString("Lobby.Spawn");
		if (split != "none") {
			String[] parse = MiniGames.getInstance().getConfig().getString("Lobby.Spawn").split(",");
			Double x = Double.parseDouble(parse[0]);
			Double y = Double.parseDouble(parse[1]);
			Double z = Double.parseDouble(parse[2]);
			Float yaw = Float.parseFloat(parse[3]);
			Float pitch = Float.parseFloat(parse[4]);

			lobbySpawn = new Location(lobby, x, y, z, yaw, pitch);
		} else {
			lobbySpawn = lobby.getSpawnLocation();
		}
	}

	public World getLobby() {
		return this.lobby;
	}

	public Location getLobbySpawn() {
		return this.lobbySpawn;
	}

	public HashMap<Games, World> getWorlds() {
		return worlds;
	}

	public ArrayList<Location> getGameSpawns(Games game) {
		return mapLocations.get(game);
	}

	public World getCurrentGameWorld(Games game) {
		return worlds.get(game);
	}
}
