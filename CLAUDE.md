# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ControlPanel is a BentoBox addon for Minecraft (Paper) that provides customizable GUI menus for players to execute common commands. It supports multiple gamemodes (AcidIsland, BSkyBlock, CaveBlock, SkyGrid, AOneBlock) and 13 localizations.

## Build Commands

```bash
# Build the project
mvn clean package

# Run tests
mvn clean test

# Run tests and SonarCloud analysis (as CI does)
mvn -B verify

# Run a single test class
mvn test -Dtest=UtilsTest

# Build without tests
mvn clean package -DskipTests
```

**Requires Java 21.** The output JAR is placed in `target/`.

## Testing

Tests use **JUnit 5 + Mockito 5 + MockBukkit**. All integration tests extend `CommonTestSetup` which provides pre-configured mocks for BentoBox, Bukkit, Player, World, Island, and other framework objects.

Key test infrastructure in `src/test/java/world/bentobox/controlpanel/`:
- `CommonTestSetup.java` — abstract base class with `@BeforeEach`/`@AfterEach` lifecycle, static mocks for `Bukkit` and `Util`
- `WhiteBox.java` — reflection utility for setting private static fields (e.g., BentoBox singleton)
- `TestWorldSettings.java` — stub `WorldSettings` implementation for tests

For tests requiring database access, mock `DatabaseSetup` statically and use `WhiteBox.setInternalState(Database.class, "databaseSetup", dbSetup)` to inject the mock handler.

## Architecture

This is a **BentoBox Addon** — it extends BentoBox's `Addon` class and follows its lifecycle (`onLoad` → `onEnable` → `onReload` → `onDisable`).

**Entry point:** `ControlPanelAddon.java` — registers commands, loads settings, initializes the manager.

**Key flow:**
1. `ControlPanelAddon` hooks `PlayerCommand` and `AdminCommand` into each registered BentoBox gamemode's command tree
2. `ControlPanelManager` loads/saves `ControlPanelObject` data from YAML files and caches them per-world
3. `ControlPanelGenerator` builds the GUI using BentoBox's `PanelBuilder` API, resolving button actions, permissions, and placeholders (`[player]`, `[server]`, `[label]`)

**Data model:** `ControlPanelObject` (implements `DataObject`) represents a single control panel with its button layout. The manager parses the YAML template (`controlPanelTemplate.yml`) into these objects.

**Configuration:** `Settings.java` uses BentoBox's `@StoreAt`/`@ConfigEntry` annotations for YAML-backed config. The `config.yml` controls which gamemodes the addon is disabled for.

**Localization:** 13 locale YAML files in `src/main/resources/locales/`. Translation key constants are centralized in `Constants.java`.

**Optional integration:** `ItemsAdderParse.java` provides soft-dependency support for the ItemsAdder plugin's custom items.

## CI

GitHub Actions on push to `develop` and PRs: builds with Maven + runs SonarCloud analysis (project: `BentoBoxWorld_ControlPanel`).
