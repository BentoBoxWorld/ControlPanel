package world.bentobox.controlpanel.panels;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import world.bentobox.bentobox.api.panels.builders.PanelBuilder;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.CommonTestSetup;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.database.objects.ControlPanelObject;
import world.bentobox.controlpanel.database.objects.ControlPanelObject.ControlPanelButton;

class ControlPanelGeneratorTest extends CommonTestSetup {

    @Mock
    private ControlPanelAddon addon;
    @Mock
    private Server mockServer;
    @Mock
    private ConsoleCommandSender consoleSender;

    private ControlPanelObject controlPanel;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        when(addon.getPlugin()).thenReturn(plugin);
        when(addon.getServer()).thenReturn(mockServer);
        when(mockServer.getConsoleSender()).thenReturn(consoleSender);

        // Set up a control panel with buttons
        controlPanel = new ControlPanelObject();
        controlPanel.setUniqueId("BSkyBlock_default");
        controlPanel.setGameMode("BSkyBlock");
        controlPanel.setPanelName("&1Commands");
        controlPanel.setDefaultPanel(true);
    }

    @Test
    void testOpenWithEmptyButtons() {
        controlPanel.setPanelButtons(Collections.emptyList());
        User user = User.getInstance(mockPlayer);

        try (MockedConstruction<PanelBuilder> mockedPB = Mockito.mockConstruction(PanelBuilder.class,
                (mock, context) -> {
                    when(mock.user(any())).thenReturn(mock);
                    when(mock.name(anyString())).thenReturn(mock);
                })) {
            ControlPanelGenerator.open(addon, user, controlPanel, "island");
            PanelBuilder pb = mockedPB.constructed().get(0);
            verify(pb).build();
        }
    }

    @Test
    void testOpenWithButton() {
        ControlPanelButton button = new ControlPanelButton();
        button.setSlot(0);
        button.setMaterial(Material.GRASS_BLOCK);
        button.setName("Go Home");
        button.setCommand("island go");
        button.setDescriptionLines(List.of("Go to your island"));

        List<ControlPanelButton> buttons = new ArrayList<>();
        buttons.add(button);
        controlPanel.setPanelButtons(buttons);

        User user = User.getInstance(mockPlayer);

        try (MockedConstruction<PanelBuilder> mockedPB = Mockito.mockConstruction(PanelBuilder.class,
                (mock, context) -> {
                    when(mock.user(any())).thenReturn(mock);
                    when(mock.name(anyString())).thenReturn(mock);
                    when(mock.item(any(int.class), any())).thenReturn(mock);
                    when(mock.item(any())).thenReturn(mock);
                    when(mock.slotOccupied(any(int.class))).thenReturn(false);
                })) {
            ControlPanelGenerator.open(addon, user, controlPanel, "island");
            PanelBuilder pb = mockedPB.constructed().get(0);
            verify(pb).build();
        }
    }

    @Test
    void testOpenWithButtonWithIcon() {
        ControlPanelButton button = new ControlPanelButton();
        button.setSlot(5);
        button.setIcon(new ItemStack(Material.DIAMOND));
        button.setName("Special [label]");
        button.setCommand("[label] help");
        button.setDescriptionLines(List.of("Description for [player]"));

        List<ControlPanelButton> buttons = new ArrayList<>();
        buttons.add(button);
        controlPanel.setPanelButtons(buttons);

        User user = User.getInstance(mockPlayer);

        try (MockedConstruction<PanelBuilder> mockedPB = Mockito.mockConstruction(PanelBuilder.class,
                (mock, context) -> {
                    when(mock.user(any())).thenReturn(mock);
                    when(mock.name(anyString())).thenReturn(mock);
                    when(mock.item(any(int.class), any())).thenReturn(mock);
                    when(mock.item(any())).thenReturn(mock);
                    when(mock.slotOccupied(any(int.class))).thenReturn(false);
                })) {
            ControlPanelGenerator.open(addon, user, controlPanel, "island");
            PanelBuilder pb = mockedPB.constructed().get(0);
            verify(pb).build();
        }
    }

    @Test
    void testOpenWithInvalidSlot() {
        ControlPanelButton button = new ControlPanelButton();
        button.setSlot(-1); // invalid slot
        button.setMaterial(Material.STONE);
        button.setName("Invalid");
        button.setCommand("test");
        button.setDescriptionLines(Collections.emptyList());

        List<ControlPanelButton> buttons = new ArrayList<>();
        buttons.add(button);
        controlPanel.setPanelButtons(buttons);

        User user = User.getInstance(mockPlayer);

        try (MockedConstruction<PanelBuilder> mockedPB = Mockito.mockConstruction(PanelBuilder.class,
                (mock, context) -> {
                    when(mock.user(any())).thenReturn(mock);
                    when(mock.name(anyString())).thenReturn(mock);
                    when(mock.item(any(int.class), any())).thenReturn(mock);
                    when(mock.item(any())).thenReturn(mock);
                    when(mock.slotOccupied(any(int.class))).thenReturn(false);
                })) {
            ControlPanelGenerator.open(addon, user, controlPanel, "island");
            PanelBuilder pb = mockedPB.constructed().get(0);
            verify(pb).build();
        }
    }
}
