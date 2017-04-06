package games.util;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.minesucht.main.MiniGames;

public class GamePlayer extends CraftPlayer{

	private static final HashMap<String, GamePlayer> PLAYERS = new HashMap<>();
	    
	  private final Player PLAYER;
	  
	  private GamePlayer(Player player) {
	    super((CraftServer) Bukkit.getServer(), ((CraftPlayer) player).getHandle());
	    PLAYERS.put(player.getName().toLowerCase(), this);
	    PLAYER = player;
	  }

	  // Beispiel-Methode
	  public boolean teleportToSpawn(World world) {
	    teleport(world.getSpawnLocation());
	    sendMessage("Du wurdest zum Spawn von Welt " + world.getName() + " geportet!");
	    return true;
	  }

	  @Override
	  public boolean hasPermission(String str) {
	    return PLAYER.hasPermission(str);
	  }

	  @Override
	  public boolean hasPermission(Permission perm) {
	    return PLAYER.hasPermission(perm);
	  }

	  /**
	   * Do not call this!
	   */
	  public void onLeave() {
	    if (PLAYERS.containsKey(getName().toLowerCase())) {
	      PLAYERS.remove(getName().toLowerCase());
	    }
	  }
	  public void onJoin(Player p) {
		  if(PLAYERS.containsKey(getName().toLowerCase())){
			  return;
		  }
		  PLAYERS.put(getName().toLowerCase(), new GamePlayer(p));
	  }

	  public static GamePlayer getPlayer(String player) {
	    if (PLAYERS.containsKey(player.toLowerCase())) {
	      return PLAYERS.get(player.toLowerCase());
	    } else {
	      Player p = Bukkit.getPlayer(player);
	      return p == null ? null : new GamePlayer(p);
	    }
	 
	  }
	  
	  public void setLocalSpectator(Player p){
		  MiniGames.getInstance().getGameManager().setLocalSpectator(p);
	  }
	  
	  public void setGlobalSpectator(Player p){
		  MiniGames.getInstance().getGameManager().setGlobalSpectator(p);
	  }
	  
	  public void setIngame(Player p){
		  MiniGames.getInstance().getGameManager().setIngame(p);
	  }
	  
	  public void update(){
		  if(MiniGames.getInstance().getGameManager().getLocalSpectators().contains(PLAYER)){
			  MiniGames.getInstance().getGameManager().setIngame(PLAYER);
		  }
	  }
	  
	  public void hideAllPlayers(){
		  for(Player p : Bukkit.getOnlinePlayers()){
			  if(p!=PLAYER) p.hidePlayer(p);
		  }
	  }

}


