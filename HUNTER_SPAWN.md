# Night Hunter Spawn Configuration

## Current behavior

The plugin automatically spawns one randomly selected night hunter near an online player when the world's Minecraft time is between 13,000 and 23,000 ticks. The eligible hunters are Valka, Varkhen, Azra, Hacker, and Luciano.

The check runs approximately once every 1.2 seconds, with a 1-in-90 random roll on an eligible check. If the roll succeeds, the plugin chooses a random online player in the eligible world and selects a spawn position with a random horizontal offset of -7 to +7 blocks from that player's location. The spawn is attempted only if the block at that position is passable. The plugin currently does not search for a safe floor, check light level, enforce a minimum distance, or verify that the location is outdoors.

Minecraft time is used, not real-world clock time. Daylight cycle settings therefore affect when the night interval is reached. The automatic system does not spawn hunters in a world with no online players.

## Is there a configuration file?

No. The current plugin has no `config.yml` options for hunter spawns. Spawn interval, random chance, time window, spawn radius, and hunter list are defined in the plugin code. Editing `server.properties` does not change these plugin-specific values.

## Manual spawning

Players can manually spawn a hunter four blocks in front of themselves with `/mundomalco <name>`:

```text
/mundomalco valka
/mundomalco varkhen
/mundomalco azra
/mundomalco hacker
/mundomalco luciano
```

The command requires a player sender. The declared command currently has no permission node, so any player who can use commands can invoke it. Administrators can restrict access using their server's permissions-management tooling, though this plugin does not define a dedicated permission.

## Administrator controls and limitations

- To stop automatic spawns, stop the server and disable or change the spawn logic in the plugin source, then rebuild and install the updated JAR. There is no in-game toggle.
- To change spawn timing, probability, radius, or the hunter roster, those values must be changed in `RaidSystems.java` and the plugin rebuilt.
- To remove a spawned hunter, use standard server entity-management commands or other moderation tools.
- The spawn implementation does not provide per-world settings, a cap on total hunters, a configurable safe-location search, or a reload command.

Keep a backup before changing the code or deploying changes to a production server. This documentation describes the current Paper plugin; it is not a Bedrock add-on configuration guide.
