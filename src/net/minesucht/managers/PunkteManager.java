package net.minesucht.managers;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


import net.minesucht.main.MiniGames;
 
public class PunkteManager {
 
	private static PunkteManager instance;
    private HashMap<Player, Integer> allePunkte;
   
    public PunkteManager(){
    	instance = this;
        allePunkte = new HashMap<Player, Integer>();
    }
   
    public HashMap<Player, Integer>  getAllePunkte(){
        return allePunkte;
    }
   
    public void addPunkte(Player p, int punkte){
    	if(allePunkte.containsKey(p)){
    		 allePunkte.put(p, allePunkte.get(p) + punkte);
    	}else{
    		 allePunkte.put(p, punkte);
    	}
       
    }
   
    public Integer getPunkte(Player p){
        return allePunkte.get(p);
    }
   
    public boolean allPunkteContains(Player p){
        if(allePunkte.containsKey(p)){
            return true;
        }
        else{
            return false;
        }
    }
    
    public static PunkteManager getInstance(){
    	return instance;
    }
    
    public List<Player> getWinner(){
    	
    	Map<Player, Integer> winners = sortMapByValue(allePunkte);
    	Entry<Player, Integer> first = null;
		Entry<Player, Integer> second = null;
		Entry<Player, Integer> third = null;

		for (Entry<Player, Integer> en : winners.entrySet()) {
			if (first == null) {
				first = en;
			} else if (second == null) {
				second = en;
			} else if (third == null) {
				third = en;
			} else
				break;
		}
		
		Player eins = first.getKey();
		Player zwei = second.getKey();
		Player drei = third.getKey();
		
    	List<Player> gewinner = new ArrayList<>();
    	
    	gewinner.add(eins);
    	gewinner.add(zwei);
    	gewinner.add(drei);
    	
    	return gewinner;
    }
    
    public static Map<Player, Integer> sortMapByValue(Map<Player, Integer> unsortMap) {
		List<Entry<Player, Integer>> list = new LinkedList<Entry<Player, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Entry<Player, Integer>>() {
			public int compare(Entry<Player, Integer> o1, Entry<Player, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		Map<Player, Integer> sortedMap = new LinkedHashMap<Player, Integer>();
		for (Entry<Player, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
    
    
    /*
     * Rank 1 Nummer: 1
     * Rank 2 Nummer: 2
     * Rank 3 Nummer: 3
     * Rank 4 Nummer: 4
     * Rank 5 Nummer: 5
     * Rank 6 Nummer: 6
     * Rank 7 Nummer: 7
     * NoRank / Exception: -1
     * 
     * 
     */
    
    
    
    public String getRankName(int Rankvalue){
    	if(Rankvalue == 1){
    		String rank1 = "§dAnfänger";
    		return rank1;
    	}if(Rankvalue == 2) {
    		String rank2 = "§5Beginner";
    		return rank2;
    	}if(Rankvalue == 3){
    		String rank3 = "§1Fortgeschrittener";
    		return rank3;
    	}if(Rankvalue == 4){
    		String rank4 = "§aProfi";
    		return rank4;
    	}if(Rankvalue == 5){
    		String rank5 = "§2Smaragd";
    		return rank5;
    	}if(Rankvalue == 6){
    		String rank6 = "§eLegendär";
    		return rank6;
    	}if(Rankvalue == 7){
    		String rank7 = "§6Master";
    		return rank7;
    	}else{
    		String noRank = "§cKonnte keinen Rank finden!";
    		return noRank;
    	}
    	
    }
    public Integer getAllPoints(){
        int punkte = 0;
        for(Player all : Bukkit.getOnlinePlayers()){
            punkte =+ allePunkte.get(all);
        }
        double spiele = 6.0;
        double allPunkte = (double)punkte;
        allPunkte = allPunkte / spiele;
        punkte = (int)allPunkte;
        return punkte;
    }
   
    public void updatePunkte(String uuid, int punkte){
        MiniGames.getMySQL().updatePunkte(uuid, punkte);
    }
    public void updatePlayedGames(String uuid){
        MiniGames.getMySQL().updatePlayedGames(uuid);
    }
    public void updateWonGames(String uuid){
        MiniGames.getMySQL().updateWon(uuid);
    }
    public void updateRank(String uuid, int rank){
        MiniGames.getMySQL().updateRank(uuid, rank);
    }
    
    public Integer getDurchschnitt(){
    	int durchschnitt = 0;
    	for(Player p : MiniGames.getInstance().getGameManager().getIngamePlayers()){
    		System.out.println("Player: " + p.getCustomName() + " Points: " + allePunkte.get(p));
    		if(allePunkte.get(p) == null){
    			allePunkte.put(p, 0);
    			System.out.println("Musste geaddet werden");
    		}
    		if(!allePunkte.containsKey(p)){
    			allePunkte.put(p, 0);
    			System.out.println("Musste komplett geaddet werden");
    		}
    		durchschnitt = durchschnitt + allePunkte.get(p);
    	}
    	durchschnitt = durchschnitt / MiniGames.getInstance().getGameManager().getIngamePlayers().size();
    	return durchschnitt;
    }
    public boolean overorUnder(Player p){
    	if(allePunkte.get(p) < getDurchschnitt()){
    		return false;
    	}else{
    		return true;
    	}
    }
    public void addPoints(String uuid){
    	MiniGames.getMySQL().updatePunkte(uuid, allePunkte.get(Bukkit.getPlayer(UUID.fromString(uuid))));
    }
    public void substractPointS(String uuid){
    	MiniGames.getMySQL().substractPunkte(uuid, allePunkte.get(Bukkit.getPlayer(UUID.fromString(uuid))) / 2 - 2);
    }
    /*
    * 0 - 100
    * Rank 2:
    * 100 - 250
    * Rank 3:
    * 250- 500
    * Rank 4:
    * 500 - 1000
    * Rank 5:
    * 1000 - 2000
    * Rank 6:
    * 2000 - 4000
    * Rank 7:
    * 4000-8000
    */
    
    public void checkrankUp(String uuid){
    	int rank = MiniGames.getMySQL().getRank(uuid);
    	int punkte = MiniGames.getMySQL().getPunkte(uuid);
    	
    	
    	if(rank == 7 && punkte < 4000){
    		MiniGames.getMySQL().updateRank(uuid, 6);
    	}
    	if(rank == 6 && punkte < 2000){
    		MiniGames.getMySQL().updateRank(uuid, 5);
    	}
    	if(rank == 5 && punkte < 1000){
    		MiniGames.getMySQL().updateRank(uuid, 4);
    	}
    	if(rank == 4 && punkte < 500){
    		MiniGames.getMySQL().updateRank(uuid, 3);
    	}
    	if(rank == 3  && punkte < 250){
    		MiniGames.getMySQL().updateRank(uuid, 2);
    	}
    	if(rank == 2 && punkte < 100){
    		MiniGames.getMySQL().updateRank(uuid, 1);
    	}
    	
    	
    	if(rank == 1 && punkte > 100){
    		MiniGames.getMySQL().updateRank(uuid, 2);
    	}
    	if(rank == 2 && punkte > 250){
    		MiniGames.getMySQL().updateRank(uuid, 3);
    	}
    	if(rank == 3 && punkte > 500){
    		MiniGames.getMySQL().updateRank(uuid, 4);
    	}
    	if(rank == 4 && punkte > 1000){
    		MiniGames.getMySQL().updateRank(uuid, 5);
    	}
    	if(rank == 5 && punkte > 2000){
    		MiniGames.getMySQL().updateRank(uuid, 6);
    	}
    	if(rank == 6 && punkte > 4000){
    		MiniGames.getMySQL().updateRank(uuid, 7);
    	}
    	
    	
    	else{
    		return;
    	}
    }
}
