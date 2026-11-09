# Mundo Maluco: A Paper Minecraft Plugin

Mundo Maluco is a Minecraft Java Paper plugin that adds a cosmic reality system with the Book of Reality, one time King blessings, divine weapon drops, named entities, persistent pets, night hunters, and the A Eternidade boss encounter. This project is a Paper plugin for Minecraft Java Edition and is not a Minecraft Bedrock add-on, behavior pack, or resource pack.

## Install

Requirements:

- Minecraft Java Edition server running Paper 1.21 or a compatible Paper build.
- Java 21.
- Maven for building the project.
- Permission to copy files into the server's `plugins` directory.

Installation steps:

1. Stop the Paper server.
2. Open a terminal in the project root.
3. Build the plugin:

   ```text
   mvn clean package
   ```

4. Copy `target/plugin.jar` into the server's `plugins` directory.
5. Start or restart the server.
6. Check the server console for plugin loading errors.
7. Join the server and confirm that the Book of Reality is added to the player's inventory.

The plugin does not use an external database or configuration file. Blessing choices are stored in player persistent data and remain after reconnecting.

## Use

When a player joins, the plugin applies temporary Darkness, creates an End Rod particle effect, sends a cosmic observation message, and gives the player a Book of Reality if they do not already have one.

Right-click the Book of Reality to open the Encyclopedia of Cosmic Beings. Players can choose one blessing from:

- Destino
- Sofia
- Chemite
- Apocalipse
- Samara

Each player can select only one blessing. The selection is saved permanently in player data. Choosing a blessing grants the implemented Resistance effect.

Commands:

```text
/mundomalco
/mm
```

These commands open the reality menu.

```text
/mundomalco <entity>
```

This spawns a supported named entity four blocks in front of the player. Supported names include:

```text
fran
denise
rene
valka
varkhen
azra
hacker
luciano
destino_corrompido
litoral
larissa
ze_do_po
idris
nox
white
snow
jade
reparador
gatuno
dorminhoco
acucarina
reflexo
joao_ninguem
operarios
```

The plugin also provides tab completion for entity names. The `operarios` option spawns the worker group.

A player who damages a living entity has a 3% chance to receive one randomly selected divine weapon:

- Book of Destiny: an enchanted book item.
- Scepter of Life: applies strong Regeneration and creates root particles when used.
- Sword of Paradox: a Netherite Sword item representing Chemite's katana.
- Scythe of Chaos: applies Wither to nearby players and creates a dark particle effect when used.
- Oracle of Wisdom: displays nearby living entities and their current and maximum health in the action bar when used.

Prepare an altar by standing on a solid block while carrying all five functional divine weapons. Run:

```text
/eternidade
```

The five weapons are consumed when the invocation succeeds. A Eternidade appears above the player with a purple boss bar and a configured health value of 690,000. Every 15 seconds, nearby players within 24 blocks are targeted by a cosmic attack that deals 999,999 damage and creates a lightning effect.

Pets can be tamed by right-clicking them while holding the required item:

- Most pets require Sweet Berries.
- Jade requires a Diamond or Emerald.

A tamed pet stores its owner's UUID and receives a glowing effect.

Valka, Varkhen, Azra, Hacker, and Luciano can spawn automatically during the Minecraft night, between 13,000 and 23,000 world ticks. They can also be spawned manually with `/mundomalco <entity>`.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/f7f59aca-584d-4cae-b023-bae636a2ea17?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=f7f59aca-584d-4cae-b023-bae636a2ea17
