package com.olliepugh.randomspawn.fileinteractions;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Bed;
import org.bukkit.util.Vector;

import com.olliepugh.randomspawn.Main;

public class SpawnFile {
	
	public static boolean useFirstBed = Main.getPlugin().getConfig().getBoolean("choose-first-bed-not-most-recent");
	
	public static void setCommandSpawn(Player player) { // set the spawn point if the user has used the command
		Main.getPlugin().userSpawns.set(player.getWorld().getUID()+"."+player.getUniqueId()+".command-spawn", player.getLocation().toVector()); // set the vector where the user is standing
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("respawn-set-message")));
	}
	
	@SuppressWarnings("unchecked")
	public static void setBedSpawn(Player player, Block block) { // set a new bed spawn point
		
		List<Vector> usersSpawns = new ArrayList<Vector>();
		
		if (Main.getPlugin().userSpawns.isSet(player.getWorld().getUID()+"."+player.getUniqueId()+".bed-spawns")) {
			usersSpawns = (List<Vector>) Main.getPlugin().userSpawns.getList(player.getWorld().getUID()+"."+player.getUniqueId()+".bed-spawns");
		};
		
		if (useFirstBed) {
			usersSpawns.add(block.getLocation().toVector()); // add the new bed position to the list
		}
		else {
			usersSpawns.add(0, block.getLocation().toVector()); // add the new bed position to the list
		}
		
		Main.getPlugin().userSpawns.set(player.getWorld().getUID()+"."+player.getUniqueId()+".bed-spawns", usersSpawns); // set the vector where the user is standing
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("respawn-set-message")));
	}
	
	public static void removeCommandSpawn(Player player) { // remove the spawn point set from a command
		Main.getPlugin().userSpawns.set(player.getWorld().getUID()+"."+player.getUniqueId()+".command-spawn", null); // set the respawn point to null
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("respawn-removed-message")));
	}
	
	public static void removeBedSpawn(Block block, Bed bed) { // remove a spawn point that relates to a bed
		
		Vector targetLocation = getFootOfBed(block, bed);
		
		Map<String, List<Vector>> players = getAllBedSpawnsInWorld(block.getWorld());
		
		for(Entry<String, List<Vector>> entry : players.entrySet()) {
		    UUID playerId = UUID.fromString(entry.getKey()); // get the current players id
		    List<Vector> spawns = entry.getValue(); // get all the spawns that belong to the player in this world
		    
		    for (Vector spawn : spawns) { // go through each spawn
		    	if (spawn.equals(targetLocation)) { // if the spawn point is at the desired location
			    	UUID ownerId = playerId;
			    	spawns.remove(spawn); // remove the spawn
			    	
			    	Main.getPlugin().userSpawns.set(block.getWorld().getUID()+"."+playerId+".bed-spawns", spawns); // set the spawns in the yml file
			    	
			    	Bukkit.getPlayer(ownerId).sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("respawn-removed-message")));
			    	return;
			    }
		    }
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Vector> getBedSpawns(String playerId, World world){ // get a players bed spawns from their player id
		if (Main.getPlugin().userSpawns.isSet(world.getUID()+"."+playerId+".bed-spawns")) {
			return (List<Vector>) Main.getPlugin().userSpawns.getList(world.getUID()+"."+playerId+".bed-spawns");
		}
		else {
			return null;
		}
		
	}
	
	public static Map<String, List<Vector>> getAllBedSpawnsInWorld(World world) { // get all bed spawns in that world
		Set<String> playersWithSpawnsInWorld= Main.getPlugin().userSpawns.getConfigurationSection(world.getUID().toString()).getKeys(false);
		Map<String, List<Vector>> playerSpawns = new HashMap<String, List<Vector>>();
		
		for (String playerId : playersWithSpawnsInWorld) {
			playerSpawns.put(playerId, getBedSpawns(playerId, world));
		}
		
		return playerSpawns;
	}

	public static Vector getFootOfBed(Block block, Bed bed) { // get the position of the foot of the bed
		EnumMap<BlockFace, Vector> facings = new EnumMap<BlockFace, Vector>(BlockFace.class);
		facings.put(BlockFace.NORTH, new Vector(0,0,-1));
		facings.put(BlockFace.EAST, new Vector(1,0,0));
		facings.put(BlockFace.SOUTH, new Vector(0,0,1));
		facings.put(BlockFace.WEST, new Vector(-1,0,0));
		
		Vector footOfBed = block.getLocation().toVector();
		
		if (bed.isHeadOfBed()) {
			BlockFace facing = bed.getFacing();
			footOfBed = footOfBed.subtract(facings.get(facing));
		}
		
		return footOfBed;
	}
	
}
