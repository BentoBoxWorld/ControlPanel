# 🎛️ ControlPanel Addon for BentoBox

[![Discord](https://img.shields.io/discord/272499714048524288.svg?logo=discord)](https://discord.bentobox.world)
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=BentoBoxWorld/ControlPanel)](https://ci.codemc.org/job/BentoBoxWorld/job/ControlPanel/)

Give your players a slick, clickable GUI menu to run their most-used island commands — no typing required. ControlPanel is a [BentoBox](https://github.com/BentoBoxWorld/BentoBox) addon that lets server admins build fully customizable control panels using a simple YAML file.

---

## ✨ Features

- 🖱️ **Click-to-run** — left-click, right-click, and shift-click can each trigger a different command
- 🗂️ **Multiple panels** — define as many panels as you want per gamemode; assign them to players via permissions
- 🎨 **Rich icons** — use any vanilla material, BentoBox `ItemParser` format, or [ItemsAdder](https://github.com/LoneDev6/ItemsAdder) custom items
- 📋 **Live placeholders** — button descriptions support PlaceholderAPI, color codes, and `[gamemode]` substitution
- 📦 **Console commands** — prefix a command with `[server]` to run it as the console
- 🔢 **Slot ranges** — fill a row of slots with a single button definition using `"0-8"`
- 🌍 **13 localisations** — cs, de, en-US, es, fr, id, ko, lv, pl, ru, zh-CN, zh-HK, zh-TW
- 🔌 **Works with** AcidIsland, BSkyBlock, CaveBlock, SkyGrid, AOneBlock

---

## 📦 Installation

1. Drop the `ControlPanel` JAR into `plugins/BentoBox/addons/`.
2. Restart the server — ControlPanel creates a default `controlPanelTemplate.yml` in `plugins/BentoBox/addons/ControlPanel/`.
3. Edit `controlPanelTemplate.yml` to build your panels (see below).
4. Run the import command in-game to load your panels:
   ```
   /bsb cp import
   ```
   Supply a custom filename to import from a different file:
   ```
   /bsb cp import myCustomPanels
   ```
   > ⚠️ Importing when panels already exist will ask for confirmation and replace them.

---

## 🎮 Commands

### Player command
```
/is cp
/is controlpanel
```
Opens the player's assigned control panel. Players without a specific panel permission see the `defaultPanel`.

### Admin command
```
/{admin} cp import [filename]
/{admin} cp import
```
| Command | Description |
|---|---|
| `/{admin} cp import` | Import panels from `controlPanelTemplate.yml` (default) |
| `/{admin} cp import myFile` | Import panels from `myFile.yml` in the addon data folder |

> Replace `{admin}` with your gamemode's admin command, e.g. `bsb` for BSkyBlock or `oa` for AOneBlock.

---

## 🔑 Permissions

| Permission | Default | Description |
|---|---|---|
| `[gamemode].controlpanel` | `true` | Open the control panel |
| `[gamemode].controlpanel.admin` | `op` | Use admin import command |
| `[gamemode].controlpanel.panel.default` | `true` | Access the default panel |
| `[gamemode].controlpanel.panel.<suffix>` | — | Access a custom panel named `<suffix>` |

Replace `[gamemode]` with the gamemode name in lowercase, e.g. `bskyblock`, `acidisland`, `aoneblock`.

---

## 🛠️ Configuration File Reference

The template file lives at `plugins/BentoBox/addons/ControlPanel/controlPanelTemplate.yml`.

### Top-level structure

```yaml
panel-list:
  <panel-key>:
    defaultPanel: true|false
    panelName: '<title>'
    permission: '<suffix>'
    buttons:
      <slot>:
        name: '<display name>'
        material: MATERIAL_NAME
        icon: 'namespace:item_id'
        itemsadder: 'namespace:item_id'
        description: |-
          line one
          line two
        command: '<left-click command>'
        right_click_command: '<right-click command>'
        shift_click_command: '<shift+left-click command>'
```

### Panel fields

| Field | Description |
|---|---|
| `defaultPanel` | `true` = shown to players with no specific panel permission |
| `panelName` | Title of the inventory GUI. Supports `&` color codes. |
| `permission` | Suffix for `[gamemode].controlpanel.panel.<suffix>`. Players with that permission see this panel. |

### Button fields

| Field | Description |
|---|---|
| `slot` | Inventory slot (0–53). Also accepts a range like `"0-8"` to fill multiple slots. |
| `name` | Button display name. Supports `&` color codes and command placeholders. |
| `material` | Vanilla Minecraft material (e.g. `GRASS_BLOCK`). Resolved with `Material.match`. |
| `icon` | BentoBox `ItemParser` string (e.g. `minecraft:diamond`). **Takes priority over `material`.** |
| `itemsadder` | ItemsAdder custom item ID (e.g. `iasurvival:ruby`). Requires the ItemsAdder plugin. |
| `description` | Lore lines shown under the button name. Supports `&` color codes, multi-line `\|`, PlaceholderAPI `%placeholders%`, and `[gamemode]`. |
| `command` | Command run on **left-click** (and any click with no specific override). Supports placeholders. |
| `right_click_command` | Command run on **right-click** or **shift+right-click**. Falls back to `command` if omitted. |
| `shift_click_command` | Command run on **shift+left-click**. Falls back to `command` if omitted. |

### Click type summary

| Click | Command used |
|---|---|
| Left click | `command` |
| Right click | `right_click_command` → `command` |
| Shift + Right click | `right_click_command` → `command` |
| Shift + Left click | `shift_click_command` → `command` |
| Any other click | `command` |

### Command placeholders

| Placeholder | Replaced with |
|---|---|
| `[label]` | The gamemode's player command label (e.g. `island`, `ob`) |
| `[player]` | The clicking player's username |
| `[server]` | Runs the command as the server console instead of the player |

### Description placeholders

| Placeholder | Replaced with |
|---|---|
| `[gamemode]` | Lowercase gamemode name (e.g. `bskyblock`, `aoneblock`) |
| `%PlaceholderAPI_placeholder%` | Any registered PlaceholderAPI placeholder |

---

## 📄 Example Configuration

A complete, real-world example for an active **BSkyBlock / AOneBlock** server. It shows two panels — a default panel for all players and a VIP panel for donors — along with all button features.

```yaml
panel-list:

  # ── Default panel — shown to all players ──────────────────────────────────
  default:
    defaultPanel: true
    panelName: '&8&l⚙ &r&6&l Island Control Panel'
    permission: 'default'
    buttons:

      # Row 0: decorative glass pane border
      "0-8":
        name: ' '
        material: BLACK_STAINED_GLASS_PANE
        description: ''
        command: ''

      # ── Navigation ────────────────────────────────────────────────────────
      9:
        name: '&a&l 🏝 Go to Island'
        icon: minecraft:grass_block
        description: |-
          &7 Left-click: teleport to your island.
          &7 Right-click: go to your island nether.
          &7 Shift+click: set your home here.
          &7 Island level: &e%Level_[gamemode]_island_level%
        command: '[label] go'
        right_click_command: '[label] go nether'
        shift_click_command: '[label] sethome'

      10:
        name: '&e&l 🏠 Set Home'
        icon: minecraft:white_bed
        description: |-
          &7 Set your island home
          &7 to your current location.
        command: '[label] sethome'

      11:
        name: '&b&l 👥 Team'
        icon: minecraft:player_head
        description: |-
          &7 View and manage
          &7 your island team.
        command: '[label] team'

      12:
        name: '&d&l ✉ Invite'
        icon: minecraft:paper
        description: |-
          &7 Invite a player to
          &7 your island.
          &7 Usage: &f/[label] invite <player>
        command: '[label] invite'

      13:
        name: '&6&l ⚙ Settings'
        icon: minecraft:anvil
        description: |-
          &7 Configure your island
          &7 protection settings.
        command: '[label] settings'

      # ── Stats & Leaderboards ──────────────────────────────────────────────
      18:
        name: '&e&l ⭐ Island Level'
        icon: minecraft:experience_bottle
        description: |-
          &7 Calculate your island level.
          &7 Current level: &e%Level_[gamemode]_island_level%
          &7 Current rank:  &6%Level_[gamemode]_island_rank%
        command: '[label] level'

      19:
        name: '&6&l 🏆 Top Islands'
        icon: minecraft:gold_block
        description: |-
          &7 View the top 10 islands
          &7 on this server.
        command: '[label] top'

      20:
        name: '&a&l 🌿 Warps'
        icon: minecraft:ender_eye
        description: |-
          &7 Browse player warps.
        command: '[label] warps'

      21:
        name: '&5&l 🔮 Challenges'
        icon: minecraft:enchanting_table
        description: |-
          &7 Complete challenges
          &7 for great rewards!
        command: '[label] challenges'

      22:
        name: '&b&l 🌊 Biomes'
        icon: minecraft:oak_sapling
        description: |-
          &7 Change the biome
          &7 on your island.
        command: '[label] biomes'

      # ── World / Utility ───────────────────────────────────────────────────
      27:
        name: '&f&l 🌍 Spawn'
        icon: minecraft:bedrock
        description: |-
          &7 Teleport to world spawn.
        command: '[label] spawn'

      28:
        name: '&c&l 🛒 Shop'
        icon: minecraft:emerald
        description: |-
          &7 Open the server shop.
        command: 'shop'

      29:
        name: '&e&l 💰 Balance'
        icon: minecraft:gold_nugget
        description: |-
          &7 Check your current balance.
          &7 Balance: &6%vault_balance%
        command: 'balance'

      # Console command example — runs as server, not player
      35:
        name: '&c&l 🔔 Report Bug'
        icon: minecraft:writable_book
        description: |-
          &7 Report an issue to admins.
          &7 Opens a support ticket.
        command: '[server] ticket create [player] bug-report'

      # Row 5: decorative border
      "36-44":
        name: ' '
        material: BLACK_STAINED_GLASS_PANE
        description: ''
        command: ''

  # ── VIP panel — assigned via permission bskyblock.controlpanel.panel.vip ─
  vip:
    defaultPanel: false
    panelName: '&8&l⚙ &r&d&l VIP Control Panel'
    permission: 'vip'
    buttons:

      "0-8":
        name: ' '
        material: PURPLE_STAINED_GLASS_PANE
        description: ''
        command: ''

      9:
        name: '&a&l 🏝 Go to Island'
        icon: minecraft:grass_block
        description: |-
          &7 Teleport to your island.
          &7 Island level: &e%Level_[gamemode]_island_level%
        command: '[label] go'

      10:
        name: '&e&l 🏠 Set Home'
        icon: minecraft:white_bed
        description: |-
          &7 Set your island home.
        command: '[label] sethome'

      11:
        name: '&b&l 👥 Team'
        icon: minecraft:player_head
        description: |-
          &7 View and manage your team.
        command: '[label] team'

      12:
        name: '&5&l 🔮 Challenges'
        icon: minecraft:enchanting_table
        description: |-
          &7 Complete challenges!
        command: '[label] challenges'

      # VIP exclusive — grants a kit via console
      13:
        name: '&d&l 🎁 VIP Kit'
        icon: minecraft:chest
        description: |-
          &d VIP exclusive!
          &7 Claim your weekly VIP kit.
        command: '[server] kit vipweekly [player]'

      # VIP exclusive — ItemsAdder custom icon example
      14:
        name: '&6&l ✨ VIP Perks'
        itemsadder: 'iasurvival:vip_star'
        description: |-
          &7 Browse all your VIP perks.
        command: 'vipperks'

      18:
        name: '&e&l ⭐ Island Level'
        icon: minecraft:experience_bottle
        description: |-
          &7 Current level: &e%Level_[gamemode]_island_level%
          &7 Current rank:  &6%Level_[gamemode]_island_rank%
        command: '[label] level'

      19:
        name: '&6&l 🏆 Top Islands'
        icon: minecraft:gold_block
        description: |-
          &7 View the top 10 islands.
        command: '[label] top'

      20:
        name: '&f&l 🌍 Spawn'
        icon: minecraft:bedrock
        description: |-
          &7 Teleport to world spawn.
        command: '[label] spawn'

      "36-44":
        name: ' '
        material: PURPLE_STAINED_GLASS_PANE
        description: ''
        command: ''
```

---

## 💡 Tips

- **Alternative clicks** — add `right_click_command` and/or `shift_click_command` to a button to run different commands on right-click and shift+left-click. Omitting them falls back to `command`.
- **Slot layout** — a standard chest row is slots 0–8. A 6-row chest (the max) uses slots 0–53.
- **Slot ranges** — use `"0-8"` (quoted) to place the same button across a range of slots. Great for border decorations.
- **Blank buttons** — set `command: ''` and `name: ' '` to create a visual spacer or border.
- **Console commands** — prefix `[server]` to run a command silently as the console, e.g. for kits, economy rewards, or admin actions players wouldn't have permission to run directly.
- **Multiple panels** — give a player the permission `[gamemode].controlpanel.panel.<suffix>` to show them a different panel. Handy for staff, donors, or ranked players.
- **ItemsAdder icons** — use the `itemsadder:` key (instead of `icon:` or `material:`) to display a custom-textured item. Requires ItemsAdder to be installed; falls back gracefully if it isn't.
- **Reloading** — after editing the YAML, run `/{admin} cp import` again to reload. Existing panels will be replaced after confirmation.

---

## 🌐 Information

More information can be found in the [Wiki Pages](https://github.com/BentoBoxWorld/ControlPanel/wiki).

Need help? Join the [BentoBox Discord](https://discord.bentobox.world).
