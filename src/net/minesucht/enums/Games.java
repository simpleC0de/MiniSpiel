package net.minesucht.enums;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public enum Games {

	SCHNEEBALLSCHLACHT(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "Schneeballschlacht" + ChatColor.DARK_GRAY + "]", (byte) 1, new ItemStack(Material.SNOW_BLOCK)), // Einfaches GunGame 2:00 Minuten Spielzeit - Höchstes Level gewinnt
	HOTPOTATO(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Hotpotato" + ChatColor.DARK_GRAY + "] ", (byte) 2, new ItemStack(Material.TNT)), // Meiste Kills gewinnt
	 THEFLASH("§8[§eTheFlash§8]§6 " , (byte) 3, new ItemStack(Material.NETHERRACK)), // Kleinere Map - 2 Leben - Tod in Schutzphase = - 1 Item, Schwerter und Rüstung ausgenommen - Letzter Überlebender gewinnt
	//ROCKETFLIGHT(ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "RocketFlight" + ChatColor.DARK_GRAY + "] ", (byte) 4, new ItemStack(Material.MELON_BLOCK)), // Fliegt auf Rakete und muss Kometen / Planeten ausweichen, sollte man einen Block treffen zurück zum Start - höchste Blockanzahl speichern = Höchste gewinnt
	SPLEEF(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Sp" + ChatColor.WHITE+  "lee" + ChatColor.RED + "f"  + "] ", (byte) 5, new ItemStack(Material.SAND)), // Kleinere Maps
	//PFERDERENNEN(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "HorseRace" + ChatColor.DARK_GRAY + "] " , (byte) 6, new ItemStack(Material.HAY_BLOCK)), // 
	WETTFISCHEN(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "WettFischen" + ChatColor.DARK_GRAY + "] ", (byte) 7, new ItemStack(Material.LAPIS_BLOCK)), // Angel verbesseren durch Items die != Fisch sind, jeder Fisch = + 1 punkt
	FREEFALL( ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "FreeFall" + ChatColor.DARK_GRAY + "] " , (byte) 8, new ItemStack(Material.WEB)), // Springe ins wasser
	 BLOCKPARTY(ChatColor.DARK_GRAY + "[" + ChatColor.LIGHT_PURPLE + "B" + ChatColor.LIGHT_PURPLE + "l" + ChatColor.GREEN + "o" + ChatColor.AQUA + "c" + ChatColor.BLUE + "k" + ChatColor.RED + "P" + ChatColor.YELLOW + "a" + ChatColor.WHITE + "r" + ChatColor.GOLD + "y" + ChatColor.DARK_GRAY + "] ", (byte)  9, new ItemStack(Material.CLAY, 1, (short)2)), // Spieler muss auf richtigen Block stehen (Farbe)
	//MINEQUIZ(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "Mine" + ChatColor.AQUA + "Quiz" + ChatColor.DARK_GRAY + "] ", (byte)10, new ItemStack(137)), // FRAGEN ÜBER DEN SERVER
	//CHALLENGES(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Challenges" + ChatColor.DARK_GRAY + "] ", (byte)11, new ItemStack(Material.REDSTONE_BLOCK)), // Mehrere Challenges, Muster bauen, Parkour rennen etc
	//MEMORY(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "Mem" + ChatColor.BLACK + "ory" + ChatColor.DARK_GRAY + "] ", (byte)12, new ItemStack(Material.SEA_LANTERN)), //
	KNOCKBACKFFA(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Knockback" + ChatColor.YELLOW + " FFA" + ChatColor.DARK_GRAY + "] ", (byte)13, new ItemStack(Material.GLASS)),
	//ONELINE(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "OneLine" + ChatColor.DARK_GRAY + "] ", (byte)14, new ItemStack(Material.STAINED_CLAY)),
	//SUPPORTGAMES(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Support" + ChatColor.AQUA + "Game" + ChatColor.DARK_GRAY + "] ", (byte)15, new ItemStack(Material.EMERALD_BLOCK)),
	JUMPNRUN(ChatColor.DARK_GRAY + "[" + ChatColor.WHITE + "Jumpn'Run" + ChatColor.DARK_GRAY + "] ", (byte)16, new ItemStack(Material.FENCE)), //Mit Schematics
	DOODLEJUMP(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "DoodleJump"  + ChatColor.DARK_GRAY + "] ", (byte)17, new ItemStack(Material.SLIME_BLOCK)); // Mit Slimigen Schleimblöcken
	//ZOMBIERUN(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Zombie" + ChatColor.DARK_GREEN + "Run" + ChatColor.DARK_GRAY + "] ", (byte)18, new ItemStack(Material.SKULL, 1, (short)0)), // Eine Horde Zombies verfolgt alle Spieler, sollte ein Zombie einen Spieler berühren ist er raus (Wie TempleRun)
	//VULCAN(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Vulcan" + ChatColor.DARK_GRAY + "] ", (byte)19, new ItemStack(Material.SKULL)), // Spawn in Vulkan - Lava steigt immer höher nach dem die Lava ausgebrochen ist, fliesst die Lava 2x so schnell und man muss so schnell es geht den Vulkan nach unten rennen
	//CAPTURETHEZONE(ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "C" + ChatColor.GREEN + "T" + ChatColor.GOLD + "Z" + ChatColor.DARK_GRAY + "] ", (byte)20, new ItemStack(Material.BEACON)); // Drei Zonen, einnehmen durch 10 Sek draufstehen (3 Teams) welches Team nach 3 Minuten die meisten Zonen (2) hat gewinnt.
	
	// Die Modis die auf dem Server bereits vertreten sind, sollen raus.
	// The Flash Speed 4 
	private Games(String prefix, Byte gameId, ItemStack gameItem){
		ItemMeta im = gameItem.getItemMeta();
		im.setDisplayName(this.toString());
		gameItem.setItemMeta(im);
		
		Object clsInstance = null;
		Class cls = null;
		try {
			cls = Class.forName("games." + this.toString().toLowerCase() + "." + this.toString().toUpperCase());
			clsInstance = (Object) cls.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		this.instance = clsInstance;
		
		this.prefix = prefix;
		this.gameId = gameId;
		this.gameItem = gameItem;
		
	}
	
	private String prefix;
	private Byte gameId;
	private ItemStack gameItem;
	private Object instance;
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public Byte getGameID(){
		return this.gameId;
	}
	
	public ItemStack getGameItem(){
		return gameItem;
	}
	
	public Object getInstance(){
		return instance;
		
	}
	
}