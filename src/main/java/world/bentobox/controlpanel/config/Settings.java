package world.bentobox.controlpanel.config;


import java.util.HashSet;
import java.util.Set;

import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.ConfigObject;
import world.bentobox.bentobox.api.configuration.StoreAt;


/**
 * Settings that implements ConfigObject is powerful and dynamic Config Objects that
 * does not need custom parsing. If it is correctly loaded, all its values will be available.
 *
 * Without Getter and Setter this class will not work.
 *
 * To specify location for config object to be stored, you should use @StoreAt(filename="{config file name}", path="{Path to your addon}")
 * To save comments in config file you should use @ConfigComment("{message}") that adds any message you want to be in file.
 */
@StoreAt(filename="config.yml", path="addons/ControlPanel")
@ConfigComment("ControlPanelAddon Configuration [version]")
@ConfigComment("This config file is dynamic and saved when the server is shutdown.")
@ConfigComment("")
public class Settings implements ConfigObject
{
	// ---------------------------------------------------------------------
	// Section: Getters and Setters
	// ---------------------------------------------------------------------


	/**
	 * This method returns the disabledGameModes value.
	 *
	 * @return the value of disabledGameModes.
	 */
	public Set<String> getDisabledGameModes()
	{
		return disabledGameModes;
	}


	/**
	 * This method sets the disabledGameModes value.
	 *
	 * @param disabledGameModes the disabledGameModes new value.
	 */
	public void setDisabledGameModes(Set<String> disabledGameModes)
	{
		this.disabledGameModes = disabledGameModes;
	}


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


	@ConfigComment("")
	@ConfigComment("This list stores GameModes in which Likes addon should not work.")
	@ConfigComment("To disable addon it is necessary to write its name in new line that starts with -. Example:")
	@ConfigComment("disabled-gamemodes:")
	@ConfigComment(" - BSkyBlock")
	@ConfigEntry(path = "disabled-gamemodes")
	private Set<String> disabledGameModes = new HashSet<>();
}
