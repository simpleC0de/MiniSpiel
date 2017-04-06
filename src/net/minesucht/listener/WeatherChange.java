package net.minesucht.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import net.minesucht.main.MiniGames;

public class WeatherChange implements Listener{

	
	
	public WeatherChange(MiniGames miniGames) {
		miniGames.getServer().getPluginManager().registerEvents(this, miniGames);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e){
		e.getWorld().setStorm(false);
		e.setCancelled(true);
	}
}
