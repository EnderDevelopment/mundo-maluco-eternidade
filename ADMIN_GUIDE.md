# Mundo Maluco Administrator Guide

This guide is for server administrators who install and operate Mundo Maluco on a Paper server.

## Platform and requirements

Mundo Maluco is a Minecraft Java Edition Paper plugin. It is not a Bedrock add-on, behavior pack, or resource pack.

The server requires:

- Paper 1.21 or a compatible Paper build.
- Java 21.
- Permission to stop the server and copy files into its `plugins` directory.

The plugin has no external database and no configuration file. Player blessing choices are stored in player persistent data and survive reconnects.

## Installation and update procedure

1. Stop the Paper server.
2. Build the project from its root directory with `mvn clean package`.
3. Copy `target/plugin.jar` into the server's `plugins` directory.
4. Remove an older Mundo Maluco JAR if it has a different filename and would load alongside the new one.
5. Start the server and inspect the console for the plugin enable message or an error stack trace.
6. Join with an operator account and run `/mundomalco` to verify that the reality menu opens.

For an update, stop the server before replacing the JAR. Restart the server after the replacement. Do not reload the plugin while players are using its menus or boss encounters.

## First-join behavior

On a player's first join after the plugin is enabled, the plugin:

- Applies Darkness for five seconds.
- Displays end-rod particles around the player.
- Sends a cosmic observation message.
- Adds the Book of Reality if the player does not already have one.

If the inventory is full, the book may remain as a dropped item. The plugin does not automatically create a replacement command for lost books; an administrator can use `/mundomalco` to open the menu directly.

## Commands

### `/mundomalco` and `/mm`

Opens the Encyclopedia of Cosmic Beings menu. A player can choose one blessing from:

- Destino
- Sofia
- Chemite
- Apocalipse
- Samara

The choice is written to player data and cannot be selected again through the menu. The command can also be used with an entity name to spawn a supported character in front of the player.

Examples:

```text
/mundomalco
/mm
/mundomalco fran
/mundomalco jade
/mundomalco operarios
```

Supported spawn names include `fran`, `denise`, `rene`, `valka`, `varkhen`, `azra`, `hacker`, `luciano`, `destino_corrompido`, `litoral`, `larissa`, `ze_do_po`, `idris`, `nox`, `white`, `snow`, `jade`, `reparador`, `gatuno`, `dorminhoco`, `acucarina`, `reflexo`, `joao_ninguem`, and `operarios`.

The command has tab completion for the supported names. Unknown names open the reality menu instead of spawning an entity.

### `/eternidade`

Attempts to summon A Eternidade at the executing player's location. The player must stand directly above a solid block and carry one of every divine weapon:

- Book of Destiny
- Scepter of Life
- Sword of Paradox
- Scythe of Chaos
- Oracle of Wisdom

The five weapons are consumed when the invocation succeeds. The altar is represented by the solid block beneath the player; no special altar structure is required by the current implementation.

The boss has a configured maximum health of 690,000, a purple boss bar, and a cosmic attack every 15 seconds. Players within 24 blocks are hit for 999,999 damage. Plan the arena with escape routes and keep valuable builds away from the fight.

Both commands currently require a player sender. The server console cannot execute them because they depend on a player's location and inventory.

## Divine weapon administration

A player who damages a living entity has a 3% chance to cause one random divine weapon to drop at the player's location. The drop is handled by the plugin and does not require a loot table.

The active weapon behaviors are:

- Book of Destiny: an enchanted book item.
- Scepter of Life: right-click applies strong Regeneration and displays protective root particles.
- Sword of Paradox: a Netherite Sword item representing Chemite's katana.
- Scythe of Chaos: right-click applies Wither to nearby players and displays dark particles.
- Oracle of Wisdom: right-click shows nearby living entities and their health in the action bar.

The plugin does not currently expose an administrator command for directly granting a divine weapon. Administrators can use the normal Minecraft `/give` command only for the base material, but that will not create a functional plugin weapon because the plugin checks its stored item data. The reliable acquisition path is the 3% drop system.

## Entities and encounter notes

The entity command creates named vanilla mobs with stored plugin data. The current implementation supports these notable behaviors:

- Fran begins as a shielded base host. Projectile hits are cancelled, the projectile is reflected, and three reflected projectiles remove the shield. When the base host would die, an ancestral Fran is created with Nox.
- Denise is created with two Crimson Flame entities. Destroying a flame can restore part of Denise's health.
- Renê is created with White, Snow, and the Bonequinho Chibi.
- Valka, Varkhen, Azra, Hacker, and Luciano can appear during the night and can also be spawned manually.
- Litoral is invulnerable and does not attack players. It reacts to nearby experience orbs.
- Larissa applies Nausea and Slowness when she attacks a player.
- Zé do Pó produces ash and can turn nearby dirt or stone into sand while moving.
- Idris can place fire near himself while moving.

Night hunters may spawn automatically while a world is between ticks 13,000 and 23,000. This is a periodic random event, so administrators should not promise a fixed spawn rate or exact location.

## Pets

Spawned pets are persistent entities until removed by normal server administration. Tame them by right-clicking while holding the required item:

- Pequeno Reparador da Realidade and the standard pets: Sweet Berries.
- Jade: a Diamond or Emerald.

After successful taming, the pet stores its owner's UUID and glows. The current implementation does not provide a pet management command, equipment GUI, or owner transfer command.

## Permissions and moderation

The declared commands currently have no permission nodes. Any player can run them, spawn supported entities, attempt the Eternidade invocation, and choose their own blessing.

Use standard Paper or Minecraft administration tools to moderate the feature:

- Use `/kill` to remove a runaway boss or entity.
- Use `/difficulty` and world protections appropriate to the event arena.
- Use region protection tools if the server uses a protection plugin.
- Keep backups before enabling the plugin on a production world.

The plugin does not provide a reload command. Restart the server after installation or replacement.

## Troubleshooting

**The plugin does not load:** verify Java 21, Paper compatibility, and the first error in the server console. Confirm that the JAR is directly inside `plugins`.

**A command is unknown:** confirm the server was restarted after the JAR was copied and that only one Mundo Maluco JAR is present.

**A player cannot choose a blessing:** the player already has a stored blessing. Reconnecting does not reset it. Resetting that choice requires editing player data with an external administrative tool and should be done only with a backup.

**The menu does not open from the book:** verify that the player is holding the Book of Reality supplied by the plugin. A normal enchanted book is not the same item.

**A Eternidade does not appear:** ensure the executing player is standing on a solid block and has all five functional divine weapons in the inventory. Ordinary items with the same visible names do not satisfy the item check.

**The server becomes busy during an encounter:** remove the boss and spawned entities with normal administration commands, then inspect the server console. The boss has a very high configured health value and its attack affects all nearby players.

## Operational limitations

The current project is a Paper Java plugin. It does not include Bedrock 1.26.40 packaging, custom models, camera-control effects, true inverted controls, custom textures, or a separate administrator permission system. Several characters use existing vanilla mob types as their visual representation.
