package net.minesucht.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.minesucht.main.MiniGames;
import net.minesucht.managers.FIleLogger;

public class CMD_Upload implements CommandExecutor{

	private FIleLogger fl;
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		String fileName = MiniGames.getInstance().getFileName();
		if(cmd.getName().equalsIgnoreCase("upload")){
			try{
				fl = new FIleLogger();
				cs.sendMessage("§eDatei §c" +fileName+"§e erfolgreich hochgeladen!");
			}catch(Exception ex){
				cs.sendMessage("§cDateiupload fehlgeschlagen!");
				ex.printStackTrace();
			}
		}
		return true;
	}

}
