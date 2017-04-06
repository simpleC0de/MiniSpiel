package net.minesucht.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.minesucht.main.MiniGames;

public class BlockEvent implements Listener{

	public BlockEvent(MiniGames plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		e.setCancelled(true);	
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e){
		e.setCancelled(true);
	}
}
