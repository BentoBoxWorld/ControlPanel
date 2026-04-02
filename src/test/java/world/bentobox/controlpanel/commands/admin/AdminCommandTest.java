package world.bentobox.controlpanel.commands.admin;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.CommonTestSetup;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.managers.ControlPanelManager;

class AdminCommandTest extends CommonTestSetup {

    private AdminCommand command;

    @Mock
    private ControlPanelAddon addon;
    @Mock
    private CompositeCommand parentCommand;
    @Mock
    private ControlPanelManager manager;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        when(parentCommand.getSubCommandAliases()).thenReturn(new HashMap<>());
        when(parentCommand.getWorld()).thenReturn(world);
        when(parentCommand.getAddon()).thenReturn(addon);
        when(parentCommand.getTopLabel()).thenReturn("bsb");
        when(parentCommand.getPermissionPrefix()).thenReturn("bskyblock.");

        when(addon.getAddonManager()).thenReturn(manager);

        command = new AdminCommand(addon, parentCommand);
    }

    @Test
    void testExecuteShowsHelp() {
        User user = User.getInstance(mockPlayer);
        assertTrue(command.execute(user, "controlpanel", Collections.emptyList()));
    }
}
