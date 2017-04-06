package net.minesucht.listener;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import net.minesucht.main.MiniGames;

public class EntityInteractListener implements Listener{

	public EntityInteractListener(MiniGames plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e){
		if(e.getRightClicked() instanceof ArmorStand){
			e.setCancelled(true);
		}
	}
	
}
