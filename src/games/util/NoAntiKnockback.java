package games.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.minesucht.main.MiniGames;

public class NoAntiKnockback  extends GameListener implements Listener{

	private HashMap<Player, Integer> hasAK;
	
	public NoAntiKnockback(){
		super();
		hasAK = new HashMap<Player, Integer>();
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e){
		  if(e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow)){
			  checkPosition(e.getEntity().getLocation(), (Player)e.getEntity()); 
		  }
	}
	
	private void checkPosition(Location loc, Player p){
		Bukkit.getScheduler().scheduleSyncDelayedTask(MiniGames.getInstance(), new Runnable(){

			@Override
			public void run() {
				if(loc.equals(p.getLocation()) && p.getLocation().add(0, 2, 0).getBlock().getType() == Material.AIR){
						if(!p.isFlying()){
							if(hasAK.containsKey(p)){
								if(hasAK.get(p) == 3){
									hasAK.remove(p);
									p.kickPlayer(MiniGames.getInstance().getGameManager().getPluginPrefix() + "§cDu wurdest wegen AntiKnockback vom Spiel ausgeschlossen!");	
								} else {
									hasAK.replace(p, hasAK.get(p)+1);
								}
								
							} else {
								hasAK.put(p, 1);
							}
							
						}
							
				}
			}
			
		}, 3);
	}
}
