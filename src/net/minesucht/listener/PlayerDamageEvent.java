package net.minesucht.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.minesucht.main.MiniGames;

public class PlayerDamageEvent implements Listener{

	public PlayerDamageEvent(MiniGames plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e){
		if(MiniGames.getInstance().getGameManager().getIsIngame()){
			e.setCancelled(false);
		}else{
			e.setCancelled(true);
		}
	}
	
}
