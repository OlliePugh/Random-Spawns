# Random-Spawns
A minecraft plugin that spawns a player randomly in the world they would normally respawn in if unless they either have a bed as a respawn point or have set a respawn point using the /respawnpoint spawnpoint command.  
Should work for servers with multiple worlds (But not thoroughly tested).

If the player loses the permission for using their respawn point they will not longer respawn there and will go to the random spawning system.

## Commands

**/randomspawn spawnpoint:** Sets the players respawn point to their current location, Requires randomspawns.admin.  
**/randomspawn nospawnpoint:** Removes the players respawn point, Requires randomspawns.admin.

## Permissions

**randomspawns.admin:** All commands require this permission

## Extras

When changing the setting for use first bed to spawn you may need to empty the userspawns config file as spawning may choose some odd decisions.
