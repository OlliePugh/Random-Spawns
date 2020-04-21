package com.olliepugh.randomspawn.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;

import com.olliepugh.randomspawn.Main;

public class PlayerSpawnListener implements Listener {
	
	public PlayerSpawnListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin()); // tell the plugin to listen for this event
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onRespawn(PlayerRespawnEvent event) {
		if (!event.isBedSpawn()) { // if the user is not spawning in their bed
			Player player = event.getPlayer();
			World world = event.getRespawnLocation().getWorld();
			
			if (Main.getPlugin().userSpawns.contains(player.getWorld().getUID()+"."+player.getUniqueId()) && player.hasPermission("randomspawns.admin")) { // if the user has a spawn point set and has permission
				Vector posVector = Main.getPlugin().userSpawns.getVector(world.getUID()+"."+player.getUniqueId());
				event.setRespawnLocation(new Location(world, posVector.getX(), posVector.getY(), posVector.getZ()));
				return;
			}
			
			Random random = new Random();
			
			int minDistanceFromBorder = 1000; // how close the player can spawn to the border
			int maxCoord = (int) Math.round((world.getWorldBorder().getSize()/2) - minDistanceFromBorder); // the max safe area before the world border

			int x;
			int y;
			int z;
			
			Location newRespawnLocation = null;
			
			if (world.getEnvironment().equals(World.Environment.NORMAL)) {
				while (newRespawnLocation == null) { // if the player was going to spawn above water or lava
					x = (random.nextInt(maxCoord) * (random .nextBoolean() ? -1 : 1));
					z = (random.nextInt(maxCoord) * (random .nextBoolean() ? -1 : 1));
					y = world.getHighestBlockAt(x, z).getY();
					
					newRespawnLocation = new Location(world, x, y, z);
					Location groundLocation = new Location(world, x, y-1,z);
					
					if (groundLocation.getBlock().isLiquid() && !newRespawnLocation.getBlock().isLiquid()) { // if the players feet will be in liquid
						newRespawnLocation = null; // reset to null to make the server find another location
					}
				}
				
				event.setRespawnLocation(newRespawnLocation); // set the user new location of spawning
			}
			/* IF THE CLIENT WANTS THE PLAYERS TO RESPAWN IN THE NETHER UNCOMMENT THIS 
			else if(world.getEnvironment().equals(World.Environment.NETHER)) {
				while (newRespawnLocation == null) {
					x = (random.nextInt(maxCoord) * (random .nextBoolean() ? -1 : 1));
					z = (random.nextInt(maxCoord) * (random .nextBoolean() ? -1 : 1));
					
					for (y = world.getMaxHeight(); y > 0; y--){ // while it is not at the complete bottom of the nether
						Location potLocation = new Location(world, x, y, z);
						Block headBlock = world.getBlockAt(x,y+1,z);
						Block groundBlock = world.getBlockAt(x,y-1,z);
						
						boolean ValidHead = headBlock.getType().equals(Material.AIR);
						boolean validFeet = potLocation.getBlock().getType().equals(Material.AIR);
						boolean validGround = groundBlock.getType().isSolid() && !groundBlock.getType().equals(Material.BEDROCK) && !groundBlock.isLiquid();
						
						if (ValidHead && validFeet && validGround) { // it is a valid place to spawn
							player.sendMessage(groundBlock.getType().toString());
							newRespawnLocation = potLocation;
							break;
						}
					}
				}
				event.setRespawnLocation(newRespawnLocation);
			}
			else if(world.getEnvironment().equals(World.Environment.THE_END)) {
				//event.setRespawnLocation(newRespawnLocation);
			}
			*/
		}
	}
}
	
