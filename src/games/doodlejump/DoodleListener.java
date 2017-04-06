package games.doodlejump;
 
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;
 
public class DoodleListener extends GameListener{
 
    private List<Player> winners = new ArrayList<>();
    private boolean gameDone = false;
   
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
       
    	if(e.getPlayer().getLocation().getY() < 30){
    		e.getPlayer().teleport(MiniGames.getInstance().getMapManager().getGameSpawns(Games.DOODLEJUMP).get(0));
    	}
       
        if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LOG && e.getPlayer().getLocation().getY() > MiniGames.getInstance().getMapManager().getGameSpawns(Games.DOODLEJUMP).get(0).getY() + 15){
         	e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
        	if(winners.size() < 3){
            	if(winners.contains(e.getPlayer())){
            		return;
            	}else{
            		   winners.add(e.getPlayer());
                       Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "DOODLEJUMP" + ChatColor.AQUA + "]" + ChatColor.GRAY + e.getPlayer().getName() + ChatColor.BLUE + " hat das Ziel erreicht!");    
                      for(Player all : Bukkit.getOnlinePlayers()){
                    	  all.sendTitle("�c" + e.getPlayer().getCustomName(), "�2hat das Ziel erreicht!");
                      }
                       return;
            	}
             }else{
            	 if(gameDone){
            		 return;
            	 }else{
            		 gameDone = true;
            		 endGame();
                     return; 
            	 }
            }
        }
       
        if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SLIME_BLOCK){
            if(e.getPlayer().getActivePotionEffects().isEmpty()){
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, 15));
            }
        }else{
        	return;
           // e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
        }
       
       
    }
   
   @EventHandler
   public void onDamage(EntityDamageEvent e){
	   e.setCancelled(true);
   }
    public void endGame(){
        new BukkitRunnable() {
            int i = 10;
            @Override
            public void run() {
                i--;
                if(i == 0){
                    DOODLEJUMP.getInstance().end();
                    cancel();
                }else{
                    if(i == 5){
                        Bukkit.broadcastMessage(ChatColor.GOLD + "Platz #1 " + ChatColor.BLUE + winners.get(0).getName());
                        MiniGames.getInstance().getPunkteManager().addPunkte(winners.get(0), 15);
                    }if(i == 4){
                        Bukkit.broadcastMessage(ChatColor.GOLD + "Platz #2 " + ChatColor.BLUE + winners.get(1).getName());
                        MiniGames.getInstance().getPunkteManager().addPunkte(winners.get(1), 12);
                    }if(i == 3){                   
                        Bukkit.broadcastMessage(ChatColor.GOLD + "Platz #3 " + ChatColor.BLUE + winners.get(2).getName());
                        MiniGames.getInstance().getPunkteManager().addPunkte(winners.get(2), 9);
                    }
                    for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
                    	 MiniGames.getInstance().getPunkteManager().addPunkte(all, 6);
                    }
                }
               
            }
        }.runTaskTimer(MiniGames.getInstance(), 0, 20);
    }
}