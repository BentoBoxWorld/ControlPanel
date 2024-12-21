# ControlPanel Addon
[![Discord](https://img.shields.io/discord/272499714048524288.svg?logo=discord)](https://discord.bentobox.world)
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=BentoBoxWorld/ControlPanel)](https://ci.codemc.org/job/BentoBoxWorld/job/ControlPanel/)

# The Control Panel Addon for BentoBox

The Control Panel addon for BentoBox provides a customizable interface for players to execute common commands in a convenient menu. Using the provided YAML configuration file, server administrators can define the layout and functionality of the control panel.

# Installation

1. Place the addon jar in the addons folder of the BentoBox plugin
1. Restart the server
2. After Control Panel is loaded it will make a default control panel file called `controlPanelTemplate.yml`
3. Open up the file with a text editor and customize it how you like it
1. Use the admin command to import it into the game if you changed it. e.g. `bsb cp import` 
  2. If you want to use a file different to the default one, you can name the file name
3. Open the control panel with the `cp` or `controlpanel` sub command ,e.g, `/is cp`

## How to Use the Configuration File

1. **Locate the Configuration File**:
   The default file, named `controlPanelTemplate.yml`, can be found in the `plugins/BentoBox/addons/ControlPanel` directory.

2. **Understand the Template Structure**:
   The configuration file defines panels, buttons, commands, and permissions. Key elements include:
   - **Panel Name**: The title of the control panel.
   - **Buttons**: Each button is assigned a slot and has attributes like name, material, description, and command.
   - **Permissions**: Control access to different panels based on user permissions.

3. **Modify Panels and Buttons**:
   - **Panel Definition**:
     Each panel starts under `panel-list` and includes settings like `defaultPanel`, `panelName`, and `permission`.
     ```yaml
     defaultPanel: true
     panelName: '&1Commands'
     permission: 'default'
     ```
   - **Button Definition**:
     Buttons are assigned slots (numbers) and include attributes:
     ```yaml
     0:
       name: 'Island'
       material: GRASS
       description:
         - '&1Go to &5 your island'
       command: '[label] go'
     ```
     - `name`: The button's display name.
     - `material`: The item representing the button.
     - `description`: Text description with support for color codes (`&`) and placeholders.
     - `command`: The command executed when the button is pressed.

4. **Command Placeholders**:
   - `[player]`: Replaced with the player's username.
   - `[server]`: Commands executed by the server console.
   - `[label]`: GameMode-specific commands.

5. **Add or Modify Buttons**:
   - Assign a slot number (0-53) for each button.
   - Update the attributes to match desired functionality.
   - Use placeholders and color codes as needed.

6. **Permissions**:
   - Permissions follow the format `[gamemode].controlpanel.panel.[suffix]`.
   - Players with multiple panel permissions will open the first panel marked as default.

7. **Save and Reload**:
   After making changes:
   - Save the file.
   - Reload the controlpanel by importing it using the `/bsb cp import <name of file>` command to apply changes.

## Example Configuration

Below is an example of a control panel with several commands:
```yaml
panel-list:

## Information

More information can be found in [Wiki Pages](https://github.com/BentoBoxWorld/ControlPanel/wiki).
