package net.minesucht.enums;

import org.bukkit.ChatColor;

public enum GameStates {

	LOBBY(ChatColor.BLACK + "[" + ChatColor.GREEN + "LOBBY" + ChatColor.BLACK + "]"),
	STARTEND(ChatColor.BLACK + "[" + ChatColor.GOLD + "STARTING..." + ChatColor.BLACK + "]"),
	INGAME(ChatColor.BLACK + "[" + ChatColor.RED + "INGAME" + ChatColor.BLACK + "]"),
	ENDING(ChatColor.BLACK + "[" + ChatColor.GRAY + "ENDING..." + ChatColor.BLACK + "]"),;
	
	
	private String prefix;
	
	GameStates(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;	
	}
	
}
