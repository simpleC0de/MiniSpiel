package net.minesucht.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.minesucht.main.MiniGames;
import net.minesucht.managers.PunkteManager;

public class QuitListener implements Listener{

	public QuitListener(MiniGames plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		MiniGames.getInstance().getGameManager().getIngamePlayers().remove(e.getPlayer());
		if(MiniGames.getInstance().getGameManager().getEnded() == false && MiniGames.getInstance().getGameManager().getStarted()){
			MiniGames.getMySQL().updateAborted(e.getPlayer().getUniqueId().toString());
			MiniGames.getInstance().getGameManager().removePoints(e.getPlayer());
			
		}else{
			return;
		}
		
		
	}
}
