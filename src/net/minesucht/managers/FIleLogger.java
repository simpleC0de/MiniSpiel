package net.minesucht.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.commons.io.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.minesucht.main.MiniGames;

public class FIleLogger{

	private static FIleLogger instance;
	public FIleLogger() {
		instance = this;
		//vdS1ex2V7A
		
		
		
	}
	
	public void replaceLogs(){
		File st = new File(MiniGames.getInstance().getDataFolder(), "stacktrace.log");
		File tempFile = new File(MiniGames.getInstance().getDataFolder(), "nodata.log");
		FileInputStream stream = null;
		BufferedReader reader = null;
		OutputStreamWriter writer = null;
		try {
			check(tempFile);
			stream = new FileInputStream(st);
			reader = new BufferedReader(new InputStreamReader(stream, Charsets.UTF_8));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				builder.append(line);
				builder.append("\n");
			}
			String[] strings = builder.toString().split("\n");
			ArrayList<String> strings2 = new ArrayList<String>();
			for(String string : strings){
				if(!string.toLowerCase().contains("uuid of player ") && !string.toLowerCase().contains(" logged in with entity id ")){
					strings2.add(string);
				}
			}
			String[] strings3 = strings2.toArray(new String[strings2.size()-1]);
			writer = new OutputStreamWriter(new FileOutputStream(tempFile), Charsets.UTF_8);
			StringBuilder builder2 = new StringBuilder();
			for(String string : strings3){
				builder2.append(string);
				builder2.append("\n");
			}
			writer.write(builder2.toString());
		} catch (Exception e) {
			throw new RuntimeException("Generating file failed", e);
		} finally {
			try {
				stream.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void check(File file) throws Exception{
		if(!file.exists()){
			if(file.isDirectory()){
				file.mkdirs();
			}else{
				file.createNewFile();
			}
		}
	}
	
	
	public void uploadToFtp(){
		try{
			String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
			String host = "meine.6te.net";
			String user = "meine.6te.net";
			String pass = "Bellobello1";
			String datafolder = MiniGames.getInstance().getDataFolder().getAbsolutePath();
			String filePath = datafolder + "/nodata.log";
			String uploadPath = "/htdocs/" + MiniGames.getInstance().getFileName() + ".log";

			ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
			System.out.println("Upload URL: " + ftpUrl);

			try {
			    URL url = new URL(ftpUrl);
			    URLConnection conn = url.openConnection();
			    OutputStream outputStream = conn.getOutputStream();
			    FileInputStream inputStream = new FileInputStream(filePath);

			    byte[] buffer = new byte[4096];
			    int bytesRead = -1;
			    while ((bytesRead = inputStream.read(buffer)) != -1) {
			        outputStream.write(buffer, 0, bytesRead);
			    }
			  
			    
				
			    inputStream.close();
			    outputStream.close();

			    System.out.println("File uploaded");
			} catch (IOException ex) {
			    ex.printStackTrace();
			   
			}
		}catch(Exception ex){
			ex.printStackTrace();
		  
		}
	}
	
	
	public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
	
	
	public static FIleLogger getInstance(){
		return instance;
	}
    
	 
}
	
