package com.olliepugh.randomspawn.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.olliepugh.randomspawn.Main;

public class RandomSpawn implements CommandExecutor {
	
	public RandomSpawn() {
		Main.getPlugin().getCommand("randomspawn").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be executed by a player");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) { // if the user did not specify the command
			return false;
		}
		else if (args[0].equalsIgnoreCase("spawnpoint")) {
			Main.getPlugin().userSpawns.set(player.getWorld().getUID()+"."+player.getUniqueId(), player.getLocation().toVector()); // set the vector where the user is standing
			player.sendMessage("Respawn point set");
			return true;
		}
		else if (args[0].equalsIgnoreCase("nospawnpoint")) { 
			Main.getPlugin().userSpawns.set(player.getWorld().getUID()+"."+player.getUniqueId(), null); // set the respawn point to null
			player.sendMessage("Respawn point removed");
			return true;
		}
		return false;
	}
}
