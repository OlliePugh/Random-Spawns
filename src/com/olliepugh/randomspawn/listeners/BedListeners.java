package com.olliepugh.randomspawn.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.material.Bed;

import com.olliepugh.randomspawn.Main;
import com.olliepugh.randomspawn.fileinteractions.SpawnFile;

public class BedListeners implements Listener {
	
	public BedListeners() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin()); // tell the plugin to listen for this event
	}
	
	@EventHandler
	public void onBedPlace(BlockPlaceEvent event) {
		Block blockPlaced = event.getBlock();
		if (blockPlaced.getType().toString().equals(Material.BED_BLOCK.toString())) { // if the block was a bed
			if (Main.getPlugin().getConfig().getBoolean("bed-place-set-spawn")) { // if the plugin does not require the player to sleep
				Player placer = event.getPlayer();
				SpawnFile.setBedSpawn(placer, event.getBlock());
			}
		}
	}
	
	@EventHandler
	public void onBedBreak(BlockBreakEvent event) {
		Block blockBroken = event.getBlock();
		if (blockBroken.getType().toString().equals(Material.BED_BLOCK.toString())) { // if the block was a bed
			if (Main.getPlugin().getConfig().getBoolean("bed-place-set-spawn")) { // if the plugin does not require the player to sleep
				SpawnFile.removeBedSpawn(blockBroken, (Bed) blockBroken.getState().getData());
			}
		}
	}
	
	@EventHandler
	public void onBedExplode(EntityExplodeEvent event) {
		List<Block> explodedBlocks = event.blockList();
		
		for (Block blockBroken : explodedBlocks) {
			if (blockBroken.getType().toString().equals(Material.BED_BLOCK.toString())) { // if the block was a bed
				if (Main.getPlugin().getConfig().getBoolean("bed-place-set-spawn")) { // if the plugin does not require the player to sleep
					SpawnFile.removeBedSpawn(blockBroken, (Bed) blockBroken.getState().getData());
				}
			}
		}
	}
}
