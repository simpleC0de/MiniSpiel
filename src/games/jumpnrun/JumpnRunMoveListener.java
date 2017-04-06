package games.jumpnrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.main.MiniGames;

public class JumpnRunMoveListener extends GameListener{

	private HashMap<Player, Location> locations = new HashMap<>();
	private static JumpnRunMoveListener instance;
	private ItemStack fastDye = new ItemStack(Material.RED_ROSE, 1);
	private ItemMeta met = fastDye.getItemMeta();

	public JumpnRunMoveListener(){
		instance = this;
		met.setDisplayName("§cZurück");
		fastDye.setItemMeta(met);
		
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getPlayer().getItemInHand().getType() == Material.RED_ROSE){
			if(!locations.containsKey(e.getPlayer())){
				e.getPlayer().teleport(MiniGames.getInstance().getMapManager().getGameSpawns(JUMPNRUN.getInstance().getGame()).get(0));
			}
		}
	}
	
	private List<Player> winners = new ArrayList<Player>();
	private boolean ended = false;
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_BLOCK){
			if(winners.contains(p)){
				if(winners.size() >= 3){
					if(ended){
						return;
					}else{
						new BukkitRunnable() {
							int i = 10;
							@Override
							public void run() {
								ended = true;
								i--;
								if(i == 9){
									for(Player all : Bukkit.getOnlinePlayers()){
										all.sendTitle("§ePlatz", "§c#1");
									}
								}if(i == 8){
									for(Player all : Bukkit.getOnlinePlayers()){
										all.playSound(all.getLocation(), Sound.LEVEL_UP,1,1);
										all.sendTitle("§2" + winners.get(0).getCustomName(), "");
									}
								}if(i == 6){
									for(Player all : Bukkit.getOnlinePlayers()){
										all.sendTitle("§ePlatz", "§c#2");
									}
								}if(i == 5){
									for(Player all : Bukkit.getOnlinePlayers()){
										all.playSound(all.getLocation(), Sound.LEVEL_UP,1,1);
										all.sendTitle("§2" + winners.get(1).getCustomName(), "");
									}
								}if(i == 3){
									for(Player all : Bukkit.getOnlinePlayers()){
										all.sendTitle("§ePlatz", "§c#3");
									}
								}if(i == 2){
									for(Player all : Bukkit.getOnlinePlayers()){
										all.playSound(all.getLocation(), Sound.LEVEL_UP,1,1);
										all.sendTitle("§2" + winners.get(2).getCustomName(), "");
									}
								}
								if(i == 0){
									cancel();
								}
							}
						}.runTaskTimer(MiniGames.getInstance(), 0, 20);
						JUMPNRUN.getInstance().end();
					}	
				}
				
				return;
			}else{
				winners.add(e.getPlayer());
				for(Player all : Bukkit.getOnlinePlayers()){
					all.sendTitle("§2" + p.getCustomName(), "§cHat das Ziel erreicht!");
				}
			}
		}
	}
	
	public static JumpnRunMoveListener getInstance(){
		return instance;
	}
}
