package net.minesucht.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

public class FileConfig {

	private FileConfiguration fileConfiguration;
	private File file;
	
	public FileConfig(FileConfiguration fg, File f){
		if(!f.exists()){
			try {
				f.createNewFile();
				fg.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} 
		
		this.fileConfiguration = fg;
		this.file = f;
	}
	
	public FileConfiguration getFileConfiguration(){
		return this.fileConfiguration;
	}
	
	public void saveConfig(){
		try {
			fileConfiguration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
