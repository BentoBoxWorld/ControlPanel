package world.bentobox.controlpanel.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettingsTest {

    private Settings settings;

    @BeforeEach
    void setUp() {
        settings = new Settings();
    }

    @Test
    void testDefaultDisabledGameModes() {
        assertNotNull(settings.getDisabledGameModes());
        assertTrue(settings.getDisabledGameModes().isEmpty());
    }

    @Test
    void testSetDisabledGameModes() {
        Set<String> modes = new HashSet<>();
        modes.add("BSkyBlock");
        modes.add("AcidIsland");
        settings.setDisabledGameModes(modes);
        assertEquals(2, settings.getDisabledGameModes().size());
        assertTrue(settings.getDisabledGameModes().contains("BSkyBlock"));
        assertTrue(settings.getDisabledGameModes().contains("AcidIsland"));
    }
}
