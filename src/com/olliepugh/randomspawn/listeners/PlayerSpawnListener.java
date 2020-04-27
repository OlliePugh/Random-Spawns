package com.olliepugh.randomspawn.listeners;

import java.util.List;
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
			
			if (Main.getPlugin().userSpawns.isSet(player.getWorld().getUID()+"."+player.getUniqueId()+".command-spawn") && player.hasPermission("randomspawns.admin")) { // if the user has a spawn point set and has permission
				Vector posVector = Main.getPlugin().userSpawns.getVector(world.getUID()+"."+player.getUniqueId()+".command-spawn");
				event.setRespawnLocation(new Location(world, posVector.getX(), posVector.getY(), posVector.getZ()));
				return;
			}
			else if (Main.getPlugin().userSpawns.isSet(player.getWorld().getUID()+"."+player.getUniqueId()+".bed-spawns")) {
				@SuppressWarnings("unchecked")
				List<Vector> bedSpawns = (List<Vector>) Main.getPlugin().userSpawns.getList(world.getUID()+"."+player.getUniqueId()+".bed-spawns");
				if (!bedSpawns.isEmpty()) { // if there are bed spawns available
					Vector posVector = bedSpawns.get(0);
					event.setRespawnLocation(new Location(world, posVector.getX(), posVector.getY(), posVector.getZ()));
					return;
				}
			}
			
			Random random = new Random();
			
			int minDistanceFromBorder = 20; // how close the player can spawn to the border
			int maxCoord = (int) Math.round((world.getWorldBorder().getSize()/2) - minDistanceFromBorder); // the max safe area before the world border

			int x;
			int y;
			int z;
			
			Location newRespawnLocation = null;
			
			int searchCount = 0;
			
			if (world.getEnvironment().equals(World.Environment.NORMAL)) {
				while (newRespawnLocation == null) { // if the player was going to spawn above water or lava
					x = (random.nextInt(maxCoord) * (random .nextBoolean() ? -1 : 1)) + (int) world.getWorldBorder().getCenter().getX(); // add the center of the zone to the spawn point
					z = (random.nextInt(maxCoord) * (random .nextBoolean() ? -1 : 1)) + (int) world.getWorldBorder().getCenter().getZ();
					
					y = world.getHighestBlockAt(x, z).getY();
					
					newRespawnLocation = new Location(world, x, y, z);
					Location groundLocation = new Location(world, x, y-2,z);
					
					if (groundLocation.getBlock().isLiquid() && !newRespawnLocation.getBlock().isLiquid()) { // if the players feet will be in liquid
						newRespawnLocation = null; // reset to null to make the server find another location
					}
					if (++searchCount >= 10) {
						newRespawnLocation = event.getRespawnLocation(); // just use the default spawn point if no location can be found
					}
				}
				event.setRespawnLocation(newRespawnLocation); // set the user new location of spawning
			}
		}
	}
}
	
