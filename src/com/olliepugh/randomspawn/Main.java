package com.olliepugh.randomspawn;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.olliepugh.randomspawn.commands.RandomSpawn;
import com.olliepugh.randomspawn.listeners.PlayerSpawnListener;

public class Main extends JavaPlugin{

	public static Main plugin;
	
	public File userSpawnsFile;
	public FileConfiguration userSpawns;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		if(!getDataFolder().exists()) { // if the data folder does not exist
			getDataFolder().mkdirs(); // create the folder
		}
		
		userSpawnsFile = new File(getDataFolder(), "userspawns.yml"); // get the file with the user spawns in
		if (!userSpawnsFile.exists()) { // if the file does not exist
			try {
				userSpawnsFile.createNewFile(); // create the userspawns.yml file
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		userSpawns = YamlConfiguration.loadConfiguration(userSpawnsFile); // load the configuration 
		
		new PlayerSpawnListener();
		new RandomSpawn();
	}
	
	@Override
	public void onDisable() {
		saveUserSpawns(); 
	}
	
	public void saveUserSpawns() { // update the file with the new values
        try {
        	this.userSpawns.save(userSpawnsFile); //saves the FileConfiguration to its File
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static Main getPlugin() {
		return Main.plugin;
	}
}
