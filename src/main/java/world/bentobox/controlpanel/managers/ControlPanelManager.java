//
// Created by BONNe
// Copyright - 2019
//


package world.bentobox.controlpanel.managers;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.Database;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.database.objects.ControlPanelObject;
import world.bentobox.controlpanel.database.objects.ControlPanelObject.ControlPanelButton;
import world.bentobox.controlpanel.utils.Constants;
import world.bentobox.controlpanel.utils.Utils;


/**
 * This class manages control panel addon data.
 */
public class ControlPanelManager
{
// ---------------------------------------------------------------------
// Section: Constructor
// ---------------------------------------------------------------------


	/**
	 * Default constructor.
	 * @param addon Likes Addon instance
	 */
	public ControlPanelManager(ControlPanelAddon addon)
	{
		this.addon = addon;

		this.controlFile = new File(this.addon.getDataFolder(), "controlPanels.yml");

		if (!this.controlFile.exists())
		{
			this.addon.saveResource("controlPanels.yml", false);
		}

		this.controlPanelCache = new HashMap<>();
		this.controlPanelDatabase = new Database<>(addon, ControlPanelObject.class);

		this.load();
	}


// ---------------------------------------------------------------------
// Section: Load Methods
// ---------------------------------------------------------------------


	/**
	 * This method loads all control panel objects.
	 */
	public void load()
	{
		this.controlPanelCache.clear();

		this.addon.getLogger().info("Loading control panels...");

		this.controlPanelDatabase.loadObjects().forEach(this::load);
	}


	/**
	 * This method loads given controlPanelObject inside cache.
	 * @param controlPanelObject Object that must be added to cache.
	 */
	private void load(ControlPanelObject controlPanelObject)
	{
		// Add object into cache
		this.controlPanelCache.put(controlPanelObject.getUniqueId(), controlPanelObject);
	}


	/**
	 * This method reloads all control panels from database to cache.
	 */
	public void reload()
	{
		this.controlPanelCache.clear();

		this.addon.getLogger().info("Reloading control panels...");

		this.controlPanelDatabase.loadObjects().forEach(this::load);
	}


// ---------------------------------------------------------------------
// Section: Save methods
// ---------------------------------------------------------------------


	/**
	 * This method saves all cached values into database.
	 */
	public void save()
	{
		this.controlPanelCache.values().forEach(this.controlPanelDatabase::saveObject);
	}


// ---------------------------------------------------------------------
// Section: Wipe methods
// ---------------------------------------------------------------------


	/**
	 * This method removes all data from database that referee to given world.
	 */
	public void wipeData(World world)
	{
		String gameMode = Utils.getGameMode(world);

		// Empty sorted cache
	}


// ---------------------------------------------------------------------
// Section: Import methods
// ---------------------------------------------------------------------


	/**
	 * This method imports control panels
	 *
	 * @param user - user
	 * @param world - world to import into
	 * @param overwrite - true if previous ones should be overwritten
	 * @return true if successful
	 */
	public void importControlPanels(User user, World world, boolean overwrite)
	{
		if (!this.controlFile.exists())
		{
			user.sendMessage(Constants.ERRORS + "no-file");
			return;
		}

		YamlConfiguration config = new YamlConfiguration();

		try
		{
			config.load(this.controlFile);
		}
		catch (IOException | InvalidConfigurationException e)
		{
			user.sendMessage(Constants.ERRORS + "no-load",
				"[message]",
				e.getMessage());
			return;
		}

		this.readControlPanel(config, user, Utils.getGameMode(world), overwrite);

		// Update biome order.
		this.addon.getAddonManager().save();
	}


	/**
	 * This method creates control panel object from config file.
	 * @param config YamlConfiguration that contains all control panels.
	 * @param user User who calls reading.
	 * @param gameMode GameMode where current panel works.
	 * @param overwrite Boolean that indicate if existing control panel should be overwritted.
	 */
	private void readControlPanel(YamlConfiguration config, User user, final String gameMode, boolean overwrite)
	{
		int newControlPanelCount = 0;

		ConfigurationSection reader = config.getConfigurationSection("panel-list");

		for (String keyReference : Objects.requireNonNull(reader).getKeys(false))
		{
			final String uniqueId = gameMode + "_" + keyReference;

			if (!this.controlPanelCache.containsKey(uniqueId) || overwrite)
			{
				ControlPanelObject controlPanel = new ControlPanelObject();
				controlPanel.setUniqueId(uniqueId);
				controlPanel.setGameMode(gameMode);

				ConfigurationSection panelSection = reader.getConfigurationSection(keyReference);

				if (panelSection != null)
				{
					controlPanel.setPanelName(panelSection.getString("panelName", "&1Commands"));
					controlPanel.setPermissionSuffix(panelSection.getString("permission", "default"));
					controlPanel.setDefaultPanel(panelSection.getBoolean("defaultPanel", false));

					List<ControlPanelButton> buttonList = new ArrayList<>();
					controlPanel.setPanelButtons(buttonList);

					ConfigurationSection buttonListSection = panelSection.getConfigurationSection("buttons");

					if (buttonListSection != null)
					{
						buttonListSection.getKeys(false).forEach(slotReference -> {
							ControlPanelButton button = new ControlPanelButton();
							button.setSlot(Integer.parseInt(slotReference));

							ConfigurationSection buttonSection = buttonListSection.getConfigurationSection(slotReference);

							if (buttonSection != null)
							{
								button.setCommand(buttonSection.getString("command", "[user_command]"));
								button.setDescription(buttonSection.getString("description", "").replace("[gamemode]", gameMode.toLowerCase()));
								button.setMaterial(Material.matchMaterial(buttonSection.getString("material", "GRASS")));

								buttonList.add(button);
							}
						});
					}

					// Save and load in cache.
					this.controlPanelDatabase.saveObject(controlPanel);
					this.load(controlPanel);

					newControlPanelCount++;
				}
			}
		}

		user.sendMessage(Constants.MESSAGE + "import-count",
			"[number]",
			String.valueOf(newControlPanelCount));
	}


// ---------------------------------------------------------------------
// Section: Processing methods
// ---------------------------------------------------------------------


	/**
	 * This method finds corresponding ControlPanel Object for user in given world.
	 * @param user User who wants to open panel
	 * @param world World where panel should be opened
	 * @param permissionPrefix Permission prefix.
	 * @return ControlPanelObject or null.
	 */
	public ControlPanelObject getUserControlPanel(User user, World world, String permissionPrefix)
	{
		String gameMode = Utils.getGameMode(world);

		String permission = Utils.getPermissionValue(user,
			permissionPrefix + "controlpanel.panel",
			null);

		if (permission == null || !this.controlPanelCache.containsKey(gameMode + "_" + permission))
		{
			// Find first default for current game mode.

			return this.controlPanelCache.values().stream().
				filter(panel -> panel.isDefaultPanel() && panel.getGameMode().equals(gameMode)).
				findFirst().orElse(null);
		}
		else
		{
			return this.controlPanelCache.get(gameMode + "_" + permission);
		}
	}


// ---------------------------------------------------------------------
// Section: Instance Variables
// ---------------------------------------------------------------------


	/**
	 * Control Panel Addon instance.
	 */
	private ControlPanelAddon addon;

	/**
	 * This database allows to access to all stored control panels.
	 */
	private Database<ControlPanelObject> controlPanelDatabase;

	/**
	 * This map contains all control panel object linked to their reference game mode.
	 */
	private Map<String, ControlPanelObject> controlPanelCache;

	/**
	 * Variable stores template.yml location
	 */
	private File controlFile;
}
