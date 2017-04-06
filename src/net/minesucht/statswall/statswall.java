package net.minesucht.statswall;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;


import com.google.gson.Gson;

import net.minesucht.main.MiniGames;

public class statswall {

	private static statswall instance;
	private Gson gs;
	public statswall(){
		gs = new Gson();
		instance = this;
	}
	public void setStatsWand(){
		String locstring = MiniGames.getInstance().getConfig().getString("Statswall.Coord");
		String locsplit[] = locstring.split(",");
		Location location = new Location(MiniGames.getInstance().getMapManager().getLobby(),
				Double.valueOf(locsplit[0]),
				Double.valueOf(locsplit[1]),
				Double.valueOf(locsplit[2]),
				Float.valueOf(locsplit[3]),
				Float.valueOf(locsplit[4]));
		
		List<String> playerNames = new ArrayList<>();
		playerNames = MiniGames.getMySQL().getTop();
		
		
				
				
				
				
				
				
				

		
		
		List<Location> LOC = new ArrayList<>();
		LOC.add(location);
		LOC.add(location.clone().add(1, 0, 0));
		LOC.add(location.clone().add(2, 0, 0));
		LOC.add(location.clone().add(3, 0, 0));
		LOC.add(location.clone().add(4, 0, 0));
		
		for(int i = 0; i < playerNames.size(); i++)
		{
			System.out.println(MiniGames.getInstance().getFetcher().toString());
			System.out.println("Loc size: " + LOC.size());
			System.out.println("Location: " + i + " " + LOC.get(i));
			int id = i+1;
			LOC.get(i).getBlock().setType(Material.SKULL);
			Skull s = (Skull)LOC.get(i).getBlock().getState();
			s.setSkullType(SkullType.PLAYER);
			String uuid = playerNames.get(i);
			UUID ud = UUID.fromString(uuid);
			System.out.println("UUID = " + uuid + " ID: "+  i++);
			OfflinePlayer off = Bukkit.getOfflinePlayer(ud);
			
			System.out.println("OfflinePlayer z69 " + MiniGames.getInstance().getFetcher().queryGameProfile("http://skinapi.martin-hess.net/" + uuid).getName() + " UUID dazu: " + uuid + " UD Object: " + ud.toString() + "\n"
					+ "OfflinePlayer UUID: " + off.getUniqueId().toString());
			
			
			s.setOwner(off.getName());
			s.setRotation(BlockFace.SOUTH);
			s.update();
			
			
			
			
			try
			{
				
				
				Location newloc = new Location(LOC.get(i).getWorld(), LOC.get(i).getX(), LOC.get(i).getY() -1, LOC.get(i).getZ());
				if(newloc.getBlock().getState() instanceof Sign)
				{
					double money = MiniGames.getMySQL().getPunkte(off.getUniqueId().toString());
					String dc = new BigDecimal(money).toPlainString();
					BlockState b = newloc.getBlock().getState();
					Sign S = (Sign)b;
					S.setLine(0, "Platz #" + id);
					S.setLine(1, MiniGames.getInstance().getFetcher().queryGameProfile("http://skinapi.martin-hess.net/" + uuid).getName());
					S.setLine(2, "Punkte");
					S.setLine(3, "" + dc);
					S.update();
					
					
					
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	
	public static statswall getInstance(){
		return instance;
	}
	
	
	
}
