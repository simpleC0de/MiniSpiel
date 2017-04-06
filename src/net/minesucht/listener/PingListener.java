package net.minesucht.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.minesucht.enums.GameStates;
import net.minesucht.main.MiniGames;

public class PingListener implements Listener{

	GameStates gs;
	public PingListener(MiniGames plugin)
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e) 
	{
		String motd = gs.getPrefix();
		if(e.getMotd() != motd)
		{
			e.setMotd(motd);
			return;
		}
		else
		{
			return;
		}
		
	}
}
