package games.capturethezone;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import games.util.GameListener;

public class CTZListener extends GameListener{

	public CTZListener(){
		super();
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		
	}
	
}
