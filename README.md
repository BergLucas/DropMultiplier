# DropMultiplier

DropMultiplier is a plugin for Paper/Spigot, which allows to multiply the amount of drops of some blocks.
These blocks must not have been placed by a player beforehand.

## Requirements

DropMultiplier requires:

-   Paper/Spigot 1.17+
-   Java 16

## Download

You can find the downloads for each version with their release notes in the [releases page](https://github.com/BergLucas/DropMultiplier/releases).

## Config

The config file looks like this :

```yaml
Multiplier: 2 # The multiplier which will increase the amount of drops.

# https://papermc.io/javadocs/paper/1.17/org/bukkit/Material.html
Blocks: # The blocks you want
    # Block: Probability (between 0.0 and 1.0)
    OAK_LOG: 0.2 # Example
```

## Commands

Available commands are :

| Command               | Permission            | Description                     |
| --------------------- | --------------------- | ------------------------------- |
| dropmultiplier        | dropmultiplier        | Displays info about the plugin. |
| dropmultiplier reload | dropmultiplier.reload | Reloads the config.             |

## Developers

### Contributors

You can find all contributors [here](https://github.com/BergLucas/DropMultiplier/graphs/contributors).

### Compiling

DropMultiplier uses Gradle for compilation. Navigate to DropMultiplier's source directory then run the following command to compile:

```bash
./gradlew clean build # on UNIX-based systems (mac, linux)
gradlew clean build # on Windows
```

### Javadoc

The javadoc can be generated with Gradle by running the following command:

```bash
./gradlew javadoc # on UNIX-based systems (mac, linux)
gradlew javadoc # on Windows
```

### License

All code is licensed for others under MIT License (see LICENSE).
