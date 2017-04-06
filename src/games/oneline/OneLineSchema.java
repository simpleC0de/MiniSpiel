package games.oneline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWolf;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import net.minesucht.enums.Games;
import net.minesucht.main.MiniGames;

class OneLineSchema {

	private List<Material> allowedBlocks;
	private ArrayList<Location> schema;
	private Location player1Location, player2Location;
	
	public OneLineSchema(){
		allowedBlocks = Arrays.asList(Material.STAINED_CLAY, Material.CLAY_BRICK, Material.QUARTZ_BLOCK, Material.SNOW_BLOCK, Material.PACKED_ICE);
	}
	
	public void createNewSchema(Location spawn){
		 Random rnd = new Random();
		 Material material = allowedBlocks.get(rnd.nextInt(allowedBlocks.size()));
		 int size;
		 int spezi;
		 do{
			 size = rnd.nextInt(7) + 10;
		 }while(size % 2 == 0);
		 
		 schema.add(spawn);
		 
		 for(int i = 0; i < size/2; i++){
			 schema.add(spawn.add(1, 0, 0));
			 schema.add(spawn.subtract(1, 0, 0));
		 }
		 
		 player1Location = spawn.add((size/2), 0, 0);
		 player2Location = spawn.subtract((size/2), 0 , 0);
		 
		 spezi = rnd.nextInt(4);
		 
		 switch(spezi){
		 case 0: {
			 schema.add(spawn.add((size/2)+1, 1 , 1));
			 schema.add(spawn.subtract((size/2)+1, 0 , 1).add(0, 1, 0));
		 }
		 break;
		 case 1: {
			 schema.add(spawn.add((size/2)+1, 0 , 1));
			 schema.add(spawn.subtract((size/2)+1, 0 , 1));
		 }
		 break;
		 case 2: { }
		 break;
		 case 3: {
			 schema.add(spawn.add(0,0,1));
			 schema.add(spawn.subtract(0,0,1));
			 schema.add(spawn.add((size/2), 0, 1));
			 schema.add(spawn.add((size/2), 0, 0).subtract(0,0,1));
			 schema.add(spawn.subtract((size/2), 0, 1));
			 schema.add(spawn.subtract((size/2), 0, 0).add(0,0,1));
		 }
		 break;
		 }
		 
		 Bukkit.getServer().broadcastMessage("" + "Die OneLine wird nun generiert!");
		 
		 new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Location loc : schema){
					
					Bukkit.getOnlinePlayers().forEach(all -> all.playSound(loc, Sound.SUCCESSFUL_HIT, 2, 2));
					
					loc.getWorld().getBlockAt(loc).setType(material);
					
					
					try {
						this.wait(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				Bukkit.getOnlinePlayers().forEach(all -> all.playSound(spawn, Sound.LEVEL_UP, 2, 2));
				Bukkit.getServer().broadcastMessage("" + "Die OneLine ist bereit!");
			}
		}.runTaskAsynchronously(MiniGames.getInstance());
	}
	
	public void reset(){
		schema.forEach(location -> location.getWorld().getBlockAt(location).setType(Material.AIR));
		schema.clear();
	}

	public Location getPlayer1Spawn(){
		return player1Location;
	}
	
	public Location getPlayer2Spawn(){
		return player2Location;
	}
}
