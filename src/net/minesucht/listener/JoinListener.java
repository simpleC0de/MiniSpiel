package net.minesucht.listener;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GamePlayer;
import net.minesucht.enums.GameStates;
import net.minesucht.main.BookGenerator;
import net.minesucht.main.MiniGames;
import net.minesucht.managers.FIleLogger;
import net.minesucht.managers.GameManager;
import net.minesucht.managers.TitleManager;

public class JoinListener implements Listener{

	public JoinListener(MiniGames plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	private String prefix;
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		e.getPlayer().getInventory().clear();
		String ip = e.getPlayer().getAddress().toString();
		String name = e.getPlayer().getCustomName();
		String uuid = e.getPlayer().getUniqueId().toString();
		MiniGames.getInstance().getBlocked().add(ip);
		MiniGames.getInstance().getBlocked().add(name);
		MiniGames.getInstance().getBlocked().add(uuid);
		
		MiniGames.getInstance().getGameManager().giveGameControlItem(e.getPlayer());
		
		//TitleManager.sendTitle(e.getPlayer(), "§eViel Spass!", 15, 25, 15);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(MiniGames.getInstance().getGameManager().isGeneratorNull()){
					MiniGames.getInstance().getGameManager().setGeneratorStatus(false);
					MiniGames.getInstance().getGameManager().createGenerator();
				}
			}
		}.runTaskLater(MiniGames.getInstance(), 40);
		
		if(MiniGames.getInstance().getGameManager().getIsIngame()){
			e.setJoinMessage("");
			MiniGames.getInstance().getGameManager().setGlobalSpectator(e.getPlayer());
			e.getPlayer().sendMessage("§eDu wurdest den §cZuschauern §ezugewiesen!");
			e.getPlayer().spigot().setCollidesWithEntities(false);
			for(Player all : Bukkit.getOnlinePlayers()){
				all.hidePlayer(e.getPlayer());
			}
			e.getPlayer().setAllowFlight(true);
			e.getPlayer().setFlying(true);
		}else{
			
		}
		if(MiniGames.getMySQL().playerExists(e.getPlayer().getUniqueId().toString())){
			
		}else{
			MiniGames.getMySQL().addPlayer(e.getPlayer().getUniqueId().toString());
		}
		if(!MiniGames.getInstance().getGameManager().getIsIngame()){ 
			e.setJoinMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "§7" + e.getPlayer().getCustomName() + " hat das Spiel betreten §8[§2" + Bukkit.getServer().getOnlinePlayers().size() + "§8/§220§8]");
			givePlayerItems(e.getPlayer());
			e.getPlayer().getInventory().setItem(7, BookGenerator.getBookLobbyBook());
			e.getPlayer().teleport(MiniGames.getInstance().getMapManager().getLobbySpawn());
			if(MiniGames.getInstance().getGameManager().getIngamePlayers().contains(e.getPlayer())){
				
			}else{
				MiniGames.getInstance().getGameManager().setIngame(e.getPlayer());
			}
				if(Bukkit.getOnlinePlayers().size() == 10){
					MiniGames.getInstance().getCountDownManager().startingCountDown(90);
				}
		} else {
			e.setJoinMessage("");
			MiniGames.getInstance().getGameManager().setGlobalSpectator(e.getPlayer());
			e.getPlayer().sendMessage(MiniGames.getInstance().getGameManager().getPluginPrefix() + "§8§kDu wurdest den Zuschauern zugewiesen, da das Spiel bereits begonnen hat!");
			e.getPlayer().teleport(MiniGames.getInstance().getGameManager().getIngamePlayers().get(0));
		}
		if(MiniGames.getInstance().getGameManager().getIngamePlayers().size() == 4 && !MiniGames.getInstance().getCountDownManager().getRunningBoolean()){
			
		}else{
			
		}
	}

	private void givePlayerItems(Player p){
		ItemStack leave_door = new ItemStack(Material.WOOD_DOOR);
		ItemMeta leave_door_meta = leave_door.getItemMeta();
		leave_door_meta.setDisplayName("§bLobby");
		leave_door.setItemMeta(leave_door_meta);
		
		p.getInventory().setItem(8, leave_door);
	}
	
	
}
