package net.minesucht.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.minesucht.main.MiniGames;

public class CMD_Key implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		cs.sendMessage("§8Spielschlüssel: \n §1" + MiniGames.getInstance().getFileName());
		return true;
	}

}
