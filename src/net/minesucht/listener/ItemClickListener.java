package net.minesucht.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

public class ItemClickListener implements Listener{

	public ItemClickListener(MiniGames plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onItemClick(PlayerInteractEvent e){
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getPlayer().getInventory().getItemInHand().getType() == Material.WOOD_DOOR){
				e.getPlayer().kickPlayer(" ");		
			}
			if(e.getPlayer().getItemInHand().getType() == Material.PAPER && !MiniGames.getInstance().getGameManager().isCreatingNewGame()){
				e.getPlayer().openInventory(createGameInv());
			}else{
				e.setCancelled(false);
				return;
			}
		}
	}
	
	private Inventory createGameInv(){
		Inventory inv = Bukkit.createInventory(null, 9, "§8[§2-Spiele-§8]");
		ItemStack[] is = new ItemStack[6];
		ItemStack i;
		int z = 0;
		for(Games g : MiniGames.getInstance().getGameManager().getGames()){
			i = g.getGameItem();
			is[z] = i;
			z++;
		}
		
		inv.setContents(is);
		return inv;
	}
	
}
