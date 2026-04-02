package world.bentobox.controlpanel.commands.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.CommonTestSetup;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.database.objects.ControlPanelObject;
import world.bentobox.controlpanel.managers.ControlPanelManager;
import world.bentobox.controlpanel.panels.ControlPanelGenerator;

class PlayerCommandTest extends CommonTestSetup {

    private PlayerCommand command;

    @Mock
    private ControlPanelAddon addon;
    @Mock
    private CompositeCommand parentCommand;
    @Mock
    private ControlPanelManager manager;
    @Mock
    private ControlPanelObject panelObject;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        when(parentCommand.getSubCommandAliases()).thenReturn(new HashMap<>());
        when(parentCommand.getWorld()).thenReturn(world);
        when(parentCommand.getAddon()).thenReturn(addon);
        when(parentCommand.getTopLabel()).thenReturn("island");
        when(parentCommand.getPermissionPrefix()).thenReturn("bskyblock.");

        when(addon.getAddonManager()).thenReturn(manager);

        command = new PlayerCommand(addon, parentCommand);
    }

    @Test
    void testCanExecuteNoPanelNonOp() {
        User user = User.getInstance(mockPlayer);
        when(mockPlayer.isOp()).thenReturn(false);
        when(manager.getUserControlPanel(any(), any(World.class), anyString())).thenReturn(null);

        assertFalse(command.canExecute(user, "controlpanel", Collections.emptyList()));
        checkSpigotMessage("controlpanel.errors.no-valid-panels");
    }

    @Test
    void testCanExecuteNoPanelOp() {
        User user = User.getInstance(mockPlayer);
        when(mockPlayer.isOp()).thenReturn(true);
        when(manager.getUserControlPanel(any(), any(World.class), anyString())).thenReturn(null);

        // Set up IWM to return a GameModeAddon with admin command
        GameModeAddon gma = mock(GameModeAddon.class);
        CompositeCommand adminCmd = mock(CompositeCommand.class);
        when(adminCmd.getTopLabel()).thenReturn("bsb");
        when(gma.getAdminCommand()).thenReturn(Optional.of(adminCmd));
        when(iwm.getAddon(any())).thenReturn(Optional.of(gma));

        assertFalse(command.canExecute(user, "controlpanel", Collections.emptyList()));
    }

    @Test
    void testCanExecuteWithPanel() {
        User user = User.getInstance(mockPlayer);
        when(manager.getUserControlPanel(any(), any(World.class), anyString())).thenReturn(panelObject);

        assertTrue(command.canExecute(user, "controlpanel", Collections.emptyList()));
    }

    @Test
    void testExecute() {
        User user = User.getInstance(mockPlayer);
        when(manager.getUserControlPanel(any(), any(World.class), anyString())).thenReturn(panelObject);
        command.canExecute(user, "controlpanel", Collections.emptyList());

        try (MockedStatic<ControlPanelGenerator> mockedGen = Mockito.mockStatic(ControlPanelGenerator.class)) {
            assertTrue(command.execute(user, "controlpanel", Collections.emptyList()));
            mockedGen.verify(() -> ControlPanelGenerator.open(any(), any(), any(), anyString()));
        }
    }
}
