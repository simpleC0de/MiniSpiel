package games.blockparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import games.util.GameListener;
import net.minesucht.main.MiniGames;

public class BlockListener extends GameListener{

	
	
	private static BlockListener instance;
	private boolean isActivated;
	private HashMap<Player, Block> lastBlock;
	private ArrayList<DyeColor> blockList;
	private Random r;
	public BlockListener(){
		r = new Random();
		blockList = new ArrayList<>();
	}
	public void selectScheduler(){
		new BukkitRunnable() {
			int i = 5;
			@Override
			public void run() {
				i--;
				int randomi = r.nextInt(blockList.size());
				if(i == 1){
					isActivated = true;
				}
				if(i == 0){
					isActivated = false;
					byte dc = blockList.get(randomi).getData();
					ItemStack blockis = new ItemStack(Material.STAINED_CLAY, 1, dc);
					
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						all.getInventory().clear();
						
						for(int o = 0; o < 9; o ++){
							all.getInventory().setItem(o, blockis);
						}
					}
					
					counterScheduler(blockis, dc);
					cancel();
				}
				
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}
	
	
	
	
	
	
	public void counterScheduler(ItemStack data, byte dc){
		new BukkitRunnable() {
			int i = 5;
			@Override
			public void run() {
				i--;
				for(Player all : Bukkit.getOnlinePlayers()){
					all.setLevel(i);
				}
				if(i == 0){
					
					for(Player all : MiniGames.getInstance().getGameManager().getIngamePlayers()){
						if(all.getLocation().getBlock().getRelative(BlockFace.DOWN).getData() == dc){
							return;
						}else{
							MiniGames.getInstance().getGameManager().setLocalSpectator(all);
							Bukkit.broadcastMessage(BLOCKPARTY.getInstance().getGame().getPrefix() + all.getCustomName() + " ist ausgeschieden!");
						}
					}	
					
					
					if(MiniGames.getInstance().getGameManager().getIngamePlayers().size() == 1){
						BLOCKPARTY.getInstance().end();
						return;
					}else{
						selectScheduler();
					}
					cancel();
					
				}
				
			}
		}.runTaskTimer(MiniGames.getInstance(), 0, 20);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(isActivated){
			Block b =  e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
			if(b.getType() == Material.AIR){
				if(b.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR){
					lastBlock.put(e.getPlayer(), b.getLocation().subtract(0, 1, 0).getBlock().getRelative(BlockFace.DOWN));
				return;
				}else{
					lastBlock.put(e.getPlayer(), b);
					return;
				}
			}else{
				lastBlock.put(e.getPlayer(), b);
				return;
			}
		}else{
			return;
		}
	}
	
	public void addallBlocks(){
		blockList.add(DyeColor.RED);
		blockList.add(DyeColor.BLACK);
		blockList.add(DyeColor.BLUE);
		blockList.add(DyeColor.BROWN);
		blockList.add(DyeColor.CYAN);
		blockList.add(DyeColor.GRAY);
		blockList.add(DyeColor.GREEN);
		blockList.add(DyeColor.LIGHT_BLUE);
		blockList.add(DyeColor.LIME);
		blockList.add(DyeColor.MAGENTA);
		blockList.add(DyeColor.ORANGE);
		blockList.add(DyeColor.PINK);
		blockList.add(DyeColor.PURPLE);
		blockList.add(DyeColor.SILVER);
		blockList.add(DyeColor.WHITE);
		blockList.add(DyeColor.YELLOW);
	}
	
	
	
	public static BlockListener getInstance(){
		return instance;
	}
}
