package net.minesucht.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.minesucht.main.MiniGames;

public class MoveListener implements Listener{
	
	public MoveListener(MiniGames plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if(MiniGames.getInstance().getGameManager().isCreatingNewGame()){
			e.getPlayer().setWalkSpeed(0);
		}
		else
		{
			e.getPlayer().setWalkSpeed((float) 0.2);
		}
	}
}
