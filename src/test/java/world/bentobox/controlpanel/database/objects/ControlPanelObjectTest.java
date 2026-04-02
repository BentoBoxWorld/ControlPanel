package world.bentobox.controlpanel.database.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import world.bentobox.controlpanel.database.objects.ControlPanelObject.ControlPanelButton;

class ControlPanelObjectTest {

    private ControlPanelObject cpo;

    @BeforeEach
    void setUp() {
        cpo = new ControlPanelObject();
    }

    @Test
    void testUniqueId() {
        assertNull(cpo.getUniqueId());
        cpo.setUniqueId("BSkyBlock_default");
        assertEquals("BSkyBlock_default", cpo.getUniqueId());
    }

    @Test
    void testGameMode() {
        assertNull(cpo.getGameMode());
        cpo.setGameMode("BSkyBlock");
        assertEquals("BSkyBlock", cpo.getGameMode());
    }

    @Test
    void testPermissionSuffix() {
        assertNull(cpo.getPermissionSuffix());
        cpo.setPermissionSuffix("default");
        assertEquals("default", cpo.getPermissionSuffix());
    }

    @Test
    void testPanelName() {
        assertNull(cpo.getPanelName());
        cpo.setPanelName("&1Commands");
        assertEquals("&1Commands", cpo.getPanelName());
    }

    @Test
    void testDefaultPanel() {
        assertFalse(cpo.isDefaultPanel());
        cpo.setDefaultPanel(true);
        assertTrue(cpo.isDefaultPanel());
    }

    @Test
    void testPanelButtons() {
        assertNull(cpo.getPanelButtons());
        List<ControlPanelButton> buttons = new ArrayList<>();
        cpo.setPanelButtons(buttons);
        assertNotNull(cpo.getPanelButtons());
        assertTrue(cpo.getPanelButtons().isEmpty());
    }

    @Test
    void testControlPanelButton() {
        ControlPanelButton button = new ControlPanelButton();

        assertEquals(0, button.getSlot());
        button.setSlot(5);
        assertEquals(5, button.getSlot());

        assertNull(button.getMaterial());
        button.setMaterial(Material.GRASS_BLOCK);
        assertEquals(Material.GRASS_BLOCK, button.getMaterial());

        assertNull(button.getIcon());

        assertNull(button.getName());
        button.setName("Test Button");
        assertEquals("Test Button", button.getName());

        assertNull(button.getCommand());
        button.setCommand("island go");
        assertEquals("island go", button.getCommand());

        assertNull(button.getRightClickCommand());
        button.setRightClickCommand("island settings");
        assertEquals("island settings", button.getRightClickCommand());

        assertNull(button.getShiftClickCommand());
        button.setShiftClickCommand("island team");
        assertEquals("island team", button.getShiftClickCommand());

        assertNull(button.getDescriptionLines());
        List<String> desc = new ArrayList<>();
        desc.add("Line 1");
        button.setDescriptionLines(desc);
        assertEquals(1, button.getDescriptionLines().size());
        assertEquals("Line 1", button.getDescriptionLines().get(0));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testControlPanelButtonDeprecatedDescription() {
        ControlPanelButton button = new ControlPanelButton();
        assertNull(button.getDescription());
        button.setDescription("old description");
        assertEquals("old description", button.getDescription());
    }
}
