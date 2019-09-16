package world.bentobox.controlpanel.commands.admin;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.util.Util;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.utils.Constants;


/**
 * This class process "cp import" command for admins.
 */
public class ImportCommand extends CompositeCommand
{
	public ImportCommand(Addon addon, CompositeCommand cmd)
	{
		super(addon, cmd, "import");
	}


	@Override
	public void setup()
	{
		this.setPermission("controlpanel.admin.import");
		this.setParametersHelp(Constants.COMMANDS + "admin.import.parameters");
		this.setDescription(Constants.COMMANDS + "admin.import.description");
	}


	@Override
	public boolean execute(User user, String label, List<String> args)
	{
		((ControlPanelAddon) this.getAddon()).getAddonManager().importControlPanels(user,
			this.getWorld(),
			!args.isEmpty() && args.get(0).equalsIgnoreCase("overwrite"));

		return true;
	}


	@Override
	public Optional<List<String>> tabComplete(User user, String alias, List<String> args)
	{
		String lastArg = !args.isEmpty() ? args.get(args.size() - 1) : "";
		return Optional.of(Util.tabLimit(Arrays.asList("overwrite"), lastArg));
	}
}
