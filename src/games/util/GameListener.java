package games.util;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import net.minesucht.main.MiniGames;

public class GameListener implements Listener{

	public GameListener(){
		register();
	}
	
	private void register(){
		MiniGames.getInstance().getServer().getPluginManager().registerEvents(this, MiniGames.getInstance());
	}
	
	public void unregister()
	{
		HandlerList.unregisterAll(this);
	}
}
