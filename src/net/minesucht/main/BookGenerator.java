package net.minesucht.main;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class BookGenerator {

	public static ItemStack getBookLobbyBook(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta book_meta = (BookMeta) book.getItemMeta();
		book_meta.setAuthor("§6Minesucht.net");
		book_meta.setDisplayName("§2Infos");
		book_meta.addPage(
				//Seite 1.
				"§bMineSucht.net" +
				wordwrap(2) +
				"§61. Wie funktioniert dieses Spiel?" + 
				wordwrap(1) + 
				"2. Welche Spielmodi gibt es?" +
				wordwrap(2)
				+
				"3. Weitere Informationen",
				
				//Seite 2.
				
				"§bWie funktioniert dieses Spiel?" + 
				wordwrap(2)+ 
				"§6Es gibt insgesamt 6 unterschiedliche Spielmodis, die jeder Spieler durchlaufen muss." + 
				wordwrap(1) + 
				"Dabei ist es wichtig bei jedem MiniSpiel möglichst viele Coins zu sammeln, um das Spiel zu gewinnen!",
				
				//Seite 3.
				
				"§bWeitere Informationen:" + 
				wordwrap(1) +
				"§6Maximale Spieleranzahl: §420" + 
				wordwrap(1) + 
				"§6Minimale Spieleranzahl: §24" + 
				wordwrap(2), 
				"§bInfos \n\n§6 Version: 1.0 \n\n Pre Alpha \n\n Author: Mr128Bit, GoRoK \n\n Maps: Alenchen, EndlessAlive \n\n Mitwirkende: DosenBierBaron, ShiiShuu, ShinyZone");
		
		book.setItemMeta(book_meta);
		
		return book;
	}
	
	private static String wordwrap(int wraps){
		String wrap = "";
		for(int i = 0; i < wraps; i++){
			wrap = wrap + "\n";
		}
		return wrap;
	}
	
}
