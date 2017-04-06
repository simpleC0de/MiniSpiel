package net.minesucht.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import games.util.GameLoader;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minesucht.enums.GameStates;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class GameManager {

	private String PluginPrefix = "§8[§bMiniGames§8]§6 "; // Message Prefix
	private List<Games> games, played; // Auto&Random generierte Liste mit
										// Spielen
	private ArmorStand generator; // Zeigt die nächsten Spiele an
	private boolean isCreatingNewGame; // Boolean ob nächstes Spiel generiert
										// wird
	private int itemChange_Task; // itemChange_Task Scheduler für Generator
	private MutableInt mi, control; // MutableInt für Runnables
	private Games nextGame; // Nächstes Spiel aus der "games" Liste
	private ArrayList<Player> localSpectators, globalSpectators, ingamePlayers; // Listen
																				// für
																				// die
																				// verschiedenen
																				// Stadien
																				// der
																				// Spieler
	private HashMap<Player, Integer> playerPoints; // Punkte die ein Spieler
													// erreicht hat
	private Random rnd; // Random Objekt
	private GameStates gs;
	private BukkitRunnable rotation;
	private double genX, genY, genZ;
	@SuppressWarnings("unused")
	private boolean ended, started, isIngame, genb, rot;
	private Location lobbySpawn;

	public GameManager() {
		genb = true;
		started = false;
		ended = false;
		isIngame = false;
		gs = GameStates.LOBBY;
		rnd = new Random(); // Init. Random Objekt
		localSpectators = new ArrayList<Player>(); // Init. Local Spectator
													// Liste
		globalSpectators = new ArrayList<Player>(); // Init. Global Spectator
													// Liste
		played = new ArrayList<Games>();
		ingamePlayers = new ArrayList<Player>(); // Init. Ingame Player Liste
		playerPoints = new HashMap<Player, Integer>(); // Init. Player Points
														// Liste
		games = new ArrayList<Games>(); // Register Games Liste
		mi = new MutableInt(); // Init. MutableInt Objekt
		control = new MutableInt();
		control.setValue(99);
		mi.setValue(10); // setze Value des MutableInts

		isCreatingNewGame = false; // Init. boolean "isCreatingNewGame"
		registerGames(); // Registriere Spiele
		// Erstelle Generator
		lobbySpawn = MiniGames.getInstance().getMapManager().getLobbySpawn();
	}

	private void registerGames() {
		// Erstelle Liste vom Typ: Integer und füge Index des Random erstellten
		// Spiels hinzu
		List<Integer> existing = new LinkedList<Integer>();
		int random;
		for (int ii = 0; ii < 6; ii++) {
			random = rnd.nextInt(Games.values().length);
			// Wenn Index aus dem Spiel schon vorhanden ist, dann gehe Schleife
			// einmal mehr durch.
			if (!existing.contains(random)) {
				games.add(Games.values()[random]);
				existing.add(random);
			} else {
				ii--;
			}

		}
	}

	public void startInstance() {
		createNewGame();
	}

	// Erstelle ArmorStand, der Spiele-Generator repräsentiert.
	public void createGenerator() {

		if (generator != null) {
			generator.remove();
		}

		MiniGames plugin = MiniGames.getInstance();
		String[] location = plugin.getConfig().getString("Lobby.Generator").split(",");
		Location loc = new Location(Bukkit.getWorld("world"), Double.valueOf(location[0]), Double.valueOf(location[1]),
				Double.valueOf(location[2]));

		generator = MiniGames.getInstance().getMapManager().getLobby().spawn(loc, ArmorStand.class);

		generator.setVisible(false);

		generator.setHelmet(new ItemStack(Material.BEACON));

		generator.setBasePlate(false);

		generator.setCustomName("§8[§bGames§8]");

		generator.setCustomNameVisible(true);

		generator.setGravity(false);

		generator.setCanPickupItems(false);

		generator.setArms(false);

		generator.setRemoveWhenFarAway(false);

		genX = generator.getHeadPose().getX();
		genY = generator.getHeadPose().getY();
		genZ = generator.getHeadPose().getZ();

	}

	public boolean isGeneratorNull() {
		return genb;
	}

	public void setGeneratorStatus(boolean b) {
		genb = b;
	}

	public void endGame() {
		Bukkit.broadcastMessage("§cSicherheitsschlüssel des Spiels: §8" + MiniGames.getInstance().getFileName());
		// Letzte Methode die den Server stoppt und Punkte verteilt.

		ended = true;

		Bukkit.broadcastMessage(PluginPrefix + " Der Punktedurchschnitt liegt bei " + ChatColor.RED
				+ ChatColor.UNDERLINE + ChatColor.BOLD + MiniGames.getInstance().getPunkteManager().getDurchschnitt()
				+ ChatColor.GOLD + " Punkten!");
		// Zeigt den Spielern den Durchschnitt.

		// PunkteManager Anfang
		setLocalSpectatorToIngame();
		for (Player p : getIngamePlayers()) {
			if (MiniGames.getInstance().getPunkteManager().getWinner().contains(p)) {
				MiniGames.getInstance().getPunkteManager().updateWonGames(p.getUniqueId().toString());
			} else {
				MiniGames.getInstance().getPunkteManager().updatePlayedGames(p.getUniqueId().toString());
			}

			MiniGames.getInstance().getPunkteManager().checkrankUp(p.getUniqueId().toString());

			if (MiniGames.getInstance().getPunkteManager().overorUnder(p)) { // Iteriert
																				// durch
																				// alle
																				// ingameplayer
																				// und
																				// checkt
																				// ob
																				// der
																				// Spieler
																				// unter
																				// oder
																				// über
																				// dem
																				// Durchschnitt
																				// ist
				MiniGames.getMySQL().updatePunkte(p.getUniqueId().toString(),
						MiniGames.getInstance().getPunkteManager().getPunkte(p));
				p.sendMessage(PluginPrefix + " Du hast " + ChatColor.GRAY
						+ MiniGames.getInstance().getPunkteManager().getPunkte(p) + ChatColor.GOLD
						+ " Punkte erhalten!"); // Spieler ist über durchschnitt
												// und bekommt Punkte
			} else {
				MiniGames.getMySQL().updatePunkte(p.getUniqueId().toString(),
						MiniGames.getInstance().getPunkteManager().getPunkte(p)); // Spieler
																					// ist
																					// unter
																					// durchschnitt
																					// und
																					// bekommt
																					// Punkte
																					// abgezogen
				p.sendMessage(PluginPrefix + " Dir wurden " + ChatColor.GRAY
						+ MiniGames.getInstance().getPunkteManager().getPunkte(p) + ChatColor.GOLD
						+ " Punkte abgezogen!");
			}
		}

		setPoints(); // Managed Gewinner und verlierer

		// PunkteManager ende

		new BukkitRunnable() {
			int i = 10;

			@Override
			public void run() {
				i--;
				Bukkit.broadcastMessage(PluginPrefix + " Der Server stoppt in " + i + " Sekunden!");
				if (i == 0) {
					for (Player all : Bukkit.getOnlinePlayers()) {
						all.kickPlayer(" ");
					}
					Bukkit.getServer().shutdown();
				}

			}
		};
	}

	
	/*
	private void register() {
		try {
			Field c = EntityTypes.class.getDeclaredField("c");
			Field f = EntityTypes.class.getDeclaredField("f");
			c.setAccessible(true);
			f.setAccessible(true);
			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(GameRocket.class,
					"FireworksRocketEntity");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(GameRocket.class, 22);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	private void startGeneratorRotation() {
		rot = true;
		EulerAngle ea = new EulerAngle(genX, genY, genZ);
		generator.setHeadPose(ea);

		rotation = new BukkitRunnable() {

			@Override
			public void run() {
				// Drehe Kopf jeder 1/20 Sekunde um 0.05 um sich selber
				/*
				 * for(Player all : Bukkit.getOnlinePlayers()){
				 * if(all.getWorld().getName().equalsIgnoreCase("world")){
				 * ParticleEffect.PORTAL.display((float)3, (float)3, (float)3,
				 * (float)3, 3, generator.getLocation(), all);
				 * ParticleEffect.SPELL.display((float)3, (float)3, (float)3,
				 * (float)3, 3, generator.getLocation(), all);
				 * 
				 * }else{ break; }
				 * 
				 * }
				 */

				EulerAngle ea = new EulerAngle(generator.getHeadPose().getX(), generator.getHeadPose().getY() + 0.05,
						generator.getHeadPose().getZ());
				generator.setHeadPose(ea);
				if (!isCreatingNewGame) {
					cancel();
				}
			}
		};

		rotation.runTaskTimer(MiniGames.getInstance(), 0, 1);

	}

	public void stopInstance() {

	}

	// Erstelle neuesSpiel.
	// Diese Methode sollte aufgerufen werden, wenn ein Spiel sich beendet
	// Diese Methode regelt den Teleport, das Generieren und das starten des
	// neues Spiel, während der Überbrückungszeit
	public void createNewGame() {

		isCreatingNewGame = true;
		updateSpawnBlocks();
		updatePlayers();
		mi.setValue(25);

		SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);

		meta.setOwner("Mr128Bit");
		meta.setDisplayName("§8[§b???§8]");

		ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

		stack.setItemMeta(meta);

		Bukkit.getOnlinePlayers().forEach(p -> {
			if (!played.isEmpty()) {
				played.forEach(game -> p.getInventory().addItem(game.getGameItem()));
				for (int i = played.size() - 1; i < games.size(); i++) {
					p.getInventory().setItem(i, stack);
					p.updateInventory();
				}
			} else {
				for (int i = 0; i < games.size(); i++) {
					p.getInventory().setItem(i, stack);
					p.updateInventory();
				}
			}

		});

		Bukkit.getOnlinePlayers().stream().forEach(pPlayer -> pPlayer.teleport(lobbySpawn));
		Bukkit.broadcastMessage(MiniGames.getInstance().getGameManager().getPluginPrefix()
				+ "Die nächste Runde startet mit dem Spiel...");
		createGenerator();
		startGeneratorRotation();

		new BukkitRunnable() {

			@Override
			public void run() {
				Bukkit.getScheduler().runTaskLater(MiniGames.getInstance(), new Runnable() {

					@Override
					public void run() {
						itemChange_Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(MiniGames.getInstance(),
								new Runnable() {

									@SuppressWarnings("deprecation")
									@Override
									public void run() {
										int random;
										System.out.println("266");
										if (control.intValue() == 99) {

											random = rnd.nextInt(games.size() + 1);
											System.out.println("Random: " + random + " games size: " + games.size());
											control.setValue(random);
										} else {
											do {
												random = rnd.nextInt(games.size() + 1);
												System.out.println(
														"277Random: " + random + " games size: " + games.size());
											} while (random == control.intValue());
											control.setValue(random);
											System.out.println(
													"Control: " + control.getValue() + " games size: " + games.size());
										}

										generator.setHelmet(games.get(random - 1).getGameItem());
										generator.setCustomName(games.get(random).getPrefix());
										for (Player all : Bukkit.getOnlinePlayers()) {
											all.getInventory().setItem(played.size(), games.get(random).getGameItem());
											all.playSound(generator.getLocation(), Sound.CHICKEN_WALK, 1, 1);
										}
										mi.increment();
										if (mi.intValue() >= 40) {

											for (Player all : Bukkit.getOnlinePlayers()) {
												all.playSound(generator.getLocation(), Sound.LEVEL_UP, 1, 1);
											}

											for (int i = 0; i < 3; i++) {
												Bukkit.getScheduler().scheduleSyncDelayedTask(MiniGames.getInstance(),
														new Runnable() {

															@Override
															public void run() {
																sendFirework();
															}

														}, i * 20);
											}

											nextGame = games.get(0);
											played.add(games.get(0));
											games.remove(0);
											generator.setHelmet(nextGame.getGameItem());
											generator.setCustomName(nextGame.getPrefix());
											Bukkit.getOnlinePlayers()
													.forEach(p -> p.sendTitle("§b" + nextGame.getPrefix(), ""));
											updatePlayers();
											GameLoader.loadGame(nextGame);
											isCreatingNewGame = false;
											updateSpawnBlocks();
											Bukkit.getScheduler().cancelTask(itemChange_Task);

										}

									}

								}, 0, mi.intValue());
					}

				}, 20 * 4);

			}
		}.runTaskLater(MiniGames.getInstance(), 40);

	}

	private void updateSpawnBlocks() {
		Material m;
		if (isCreatingNewGame) {
			m = Material.BARRIER;
		} else {
			m = Material.AIR;
		}

		lobbySpawn.clone().add(0, 2, 0).getBlock().setType(m);
		lobbySpawn.clone().add(1, 0, 0).getBlock().setType(m);
		lobbySpawn.clone().add(0, 0, 1).getBlock().setType(m);
		lobbySpawn.clone().subtract(1, 0, 0).getBlock().setType(m);
		lobbySpawn.clone().subtract(0, 0, 1).getBlock().setType(m);
	}

	public void updatePlayers() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.getActivePotionEffects().clear();
			all.getInventory().clear();
		}

		if (isCreatingNewGame) {
			Bukkit.getOnlinePlayers().stream().forEach(pp -> {
				MiniGames.getInstance().getGameManager().getIngamePlayers().forEach(p -> {
					if (p != pp)
						pp.hidePlayer(p);
				});
			});
		} else {
			for (Player local : Bukkit.getOnlinePlayers()) {
				MiniGames.getInstance().getGameManager().getLocalSpectators().forEach(pp -> pp.showPlayer(local));
				local.setPlayerListName("");
			}
			for (Player ingame : Bukkit.getOnlinePlayers()) {
				MiniGames.getInstance().getGameManager().getIngamePlayers().forEach(pp -> pp.showPlayer(ingame));
				ingame.spigot().setCollidesWithEntities(true);
				ingame.setPlayerListName(ingame.getDisplayName());
			}
		}

	}

	public void setPoints() {
		if (MiniGames.getInstance().getGameManager().getEnded()) {
			Player erster = PunkteManager.getInstance().getWinner().get(0);
			Player zweiter = PunkteManager.getInstance().getWinner().get(1);
			Player dritter = PunkteManager.getInstance().getWinner().get(2);

			MiniGames.getMySQL().updateWon(erster.getUniqueId().toString());
			MiniGames.getMySQL().updateWon(zweiter.getUniqueId().toString());
			MiniGames.getMySQL().updateWon(dritter.getUniqueId().toString());

			MiniGames.getInstance().getGameManager().getIngamePlayers().remove(erster);
			MiniGames.getInstance().getGameManager().getIngamePlayers().remove(zweiter);
			MiniGames.getInstance().getGameManager().getIngamePlayers().remove(dritter);

			MiniGames.getInstance().getGameManager().removePoints(erster);
			MiniGames.getInstance().getGameManager().removePoints(zweiter);
			MiniGames.getInstance().getGameManager().removePoints(dritter);

			for (int i = 0; i < MiniGames.getInstance().getGameManager().getIngamePlayers().size(); i++) {
				Player p = MiniGames.getInstance().getGameManager().getIngamePlayers().get(0);
				MiniGames.getInstance().getGameManager().getIngamePlayers().remove(p);
				MiniGames.getMySQL().updatePlayedGames(p.getUniqueId().toString());
				MiniGames.getMySQL().updatePunkte(p.getUniqueId().toString(),
						PunkteManager.getInstance().getAllePunkte().get(p));
				MiniGames.getInstance().getGameManager().removePoints(p);
			}
		}

	}

	private void sendFirework() {
		Location loc = generator.getLocation();
		new BukkitRunnable() {

			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {

					new BukkitRunnable() {

						@Override
						public void run() {
							generator.teleport(generator.getLocation().add(0, 0.1, 0));
						}
					}.runTaskLater(MiniGames.getInstance(), 10);
				}
			}
		}.runTaskLater(MiniGames.getInstance(), 30);

		Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);
		Random rnd = new Random();

		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder().flicker(false).trail(true)
				.with(Type.values()[rnd.nextInt(Type.values().length)]).withColor(getColor(rnd.nextInt(17) + 1))
				.withFade(Color.BLUE).build());
		fm.setPower(0);
		f.setFireworkMeta(fm);
	}

	private Color getColor(int i) {
		Color c = null;
		if (i == 1) {
			c = Color.AQUA;
		}
		if (i == 2) {
			c = Color.BLACK;
		}
		if (i == 3) {
			c = Color.BLUE;
		}
		if (i == 4) {
			c = Color.FUCHSIA;
		}
		if (i == 5) {
			c = Color.GRAY;
		}
		if (i == 6) {
			c = Color.GREEN;
		}
		if (i == 7) {
			c = Color.LIME;
		}
		if (i == 8) {
			c = Color.MAROON;
		}
		if (i == 9) {
			c = Color.NAVY;
		}
		if (i == 10) {
			c = Color.OLIVE;
		}
		if (i == 11) {
			c = Color.ORANGE;
		}
		if (i == 12) {
			c = Color.PURPLE;
		}
		if (i == 13) {
			c = Color.RED;
		}
		if (i == 14) {
			c = Color.SILVER;
		}
		if (i == 15) {
			c = Color.TEAL;
		}
		if (i == 16) {
			c = Color.WHITE;
		}
		if (i == 17) {
			c = Color.YELLOW;
		}

		return c;
	}

	// Sektion um Spieler zu Typisieren:
	// Typen: IngamePlayer, GlobalSpectator, LocalSpectator

	// Typiesere Spieler zum Ingame Player
	public void setIngame(Player p) {
		p.setFlying(false);
		p.setAllowFlight(false);
		if (ingamePlayers.contains(p)) {
			return;
		} else {
			ingamePlayers.add(p);
		}

	}

	// Boolean setzen
	public void setisIngame(boolean b) {
		isIngame = b;
	}

	// Setze alle Lokalen Spectator zu den Ingame Player / Beispiel nach
	// SpielEnde
	public void setLocalSpectatorToIngame() {
		for (Player p : localSpectators) {
			ingamePlayers.add(p);
			p.spigot().setCollidesWithEntities(true);
			p.setFlying(false);
			p.setAllowFlight(false);
		}
		localSpectators.clear();
	}

	// Setze Spieler als Globalen Spectator
	public void setGlobalSpectator(Player p) {

		hidePlayer(p);

		globalSpectators.add(p);
		p.spigot().setCollidesWithEntities(false);
		p.setAllowFlight(true);
		p.setFlying(true);
	}

	// Setze Spieler als Lokalen Spectator
	public void setLocalSpectator(Player p) {

		hidePlayer(p);

		ingamePlayers.remove(p);
		p.spigot().setCollidesWithEntities(false);
		localSpectators.add(p);
		p.setAllowFlight(true);
		p.setFlying(true);
	}

	public void giveGameControlItem(Player p) {
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta paper_meta = paper.getItemMeta();
		paper_meta.setDisplayName("§8[§2-Spiele-§8]");
		paper.setItemMeta(paper_meta);

		p.getInventory().setItem(4, paper);
	}

	public void sendActionBar(Player p, String msg) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
				PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
				
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20*3);
		
	}

	// Verstecke Spieler vor allen Spielern
	private void hidePlayer(Player p) {
		for (Player pp : Bukkit.getOnlinePlayers()) {
			pp.hidePlayer(p);
		}
	}

	public void setGameState(GameStates gs) {
		this.gs = gs;
	}

	// Getter Section
	public ArrayList<Player> getIngamePlayers() {
		return this.ingamePlayers;
	}

	public GameStates getGameState() {
		return gs;
	}

	public ArrayList<Player> getLocalSpectators() {
		return this.localSpectators;
	}

	public ArrayList<Player> getGlobalSpectators() {
		return this.globalSpectators;
	}

	public String getPluginPrefix() {
		return PluginPrefix;
	}

	public boolean isCreatingNewGame() {
		return isCreatingNewGame;
	}

	public Games getNextGame() {
		return this.nextGame;
	}

	public Integer getPoints(Player p) {
		return playerPoints.get(p);
	}

	public void setPoints(Player p, int points) {
		playerPoints.put(p, points);
	}

	public void removePoints(Player p) {
		playerPoints.remove(p);
	}

	public List<Games> getGames() {
		return games;
	}

	public void setGames(ArrayList<Games> games) {
		this.games = games;
	}

	public boolean getEnded() {
		return ended;
	}

	public boolean getStarted() {
		return started;
	}

	public boolean getIsIngame() {
		return isIngame;
	}

}