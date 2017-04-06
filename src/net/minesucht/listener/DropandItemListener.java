package net.minesucht.listener;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import games.util.Game;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class DropandItemListener implements Listener{
	public DropandItemListener(MiniGames plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onItemMove(InventoryMoveItemEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onItemClick(InventoryClickEvent e){
		if(e.getInventory().getName() == "§8[§2-Spiele-§8]"){
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
		
	}
	@EventHandler
	public void onItem(InventoryDragEvent e){
		if(e.getInventory().getName() == "§8[§2-Spiele-§8]"){
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onItemMov(InventoryPickupItemEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onInvClose(InventoryCloseEvent e){
		System.out.println(e.getInventory().getName());
		if(e.getInventory().getName() == "§8[§2-Spiele-§8]"){
			updateGames(e.getInventory());
			
		} 
	}
	
	private void updateGames(Inventory inv){
			ArrayList<Games> newGames = new ArrayList<Games>();
			
			System.out.println(inv.getContents().length);
			Games g;
			for(int i = 0; i<=5; i++){
				g = Games.valueOf(inv.getItem(i).getItemMeta().getDisplayName().toUpperCase());
				newGames.add(g);
			}
			
			MiniGames.getInstance().getGameManager().setGames(newGames);
		
	}
}
