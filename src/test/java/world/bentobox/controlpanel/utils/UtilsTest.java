package world.bentobox.controlpanel.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.junit.jupiter.api.Test;

import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.controlpanel.CommonTestSetup;

class UtilsTest extends CommonTestSetup {

    @Test
    void testGetPermissionValueWithMatch() {
        User user = User.getInstance(mockPlayer);
        PermissionAttachmentInfo pai = mock(PermissionAttachmentInfo.class);
        when(pai.getPermission()).thenReturn("bskyblock.controlpanel.panel.vip");
        when(mockPlayer.getEffectivePermissions()).thenReturn(Set.of(pai));

        String result = Utils.getPermissionValue(user, "bskyblock.controlpanel.panel", null);
        assertEquals("vip", result);
    }

    @Test
    void testGetPermissionValueWithWildcard() {
        User user = User.getInstance(mockPlayer);
        PermissionAttachmentInfo pai = mock(PermissionAttachmentInfo.class);
        when(pai.getPermission()).thenReturn("bskyblock.controlpanel.panel.*");
        when(mockPlayer.getEffectivePermissions()).thenReturn(Set.of(pai));

        String result = Utils.getPermissionValue(user, "bskyblock.controlpanel.panel", "default");
        assertEquals("default", result);
    }

    @Test
    void testGetPermissionValueNoMatch() {
        User user = User.getInstance(mockPlayer);
        when(mockPlayer.getEffectivePermissions()).thenReturn(Collections.emptySet());

        String result = Utils.getPermissionValue(user, "bskyblock.controlpanel.panel", "fallback");
        assertEquals("fallback", result);
    }

    @Test
    void testGetPermissionValueTrailingDot() {
        User user = User.getInstance(mockPlayer);
        PermissionAttachmentInfo pai = mock(PermissionAttachmentInfo.class);
        when(pai.getPermission()).thenReturn("bskyblock.controlpanel.panel.admin");
        when(mockPlayer.getEffectivePermissions()).thenReturn(Set.of(pai));

        String result = Utils.getPermissionValue(user, "bskyblock.controlpanel.panel.", null);
        assertEquals("admin", result);
    }

    @Test
    void testGetGameModeFromWorld() {
        GameModeAddon gma = mock(GameModeAddon.class);
        AddonDescription desc = new AddonDescription.Builder("main", "BSkyBlock", "1.0").build();
        when(gma.getDescription()).thenReturn(desc);
        when(iwm.getAddon(any(World.class))).thenReturn(Optional.of(gma));

        String result = Utils.getGameMode(world);
        assertEquals("BSkyBlock", result);
    }

    @Test
    void testGetGameModeFromWorldNoAddon() {
        when(iwm.getAddon(any(World.class))).thenReturn(Optional.empty());

        String result = Utils.getGameMode(world);
        assertEquals("", result);
    }

    @Test
    void testGetGameModeFromAddon() {
        GameModeAddon gma = mock(GameModeAddon.class);
        AddonDescription desc = new AddonDescription.Builder("main", "AcidIsland", "1.0").build();
        when(gma.getDescription()).thenReturn(desc);

        String result = Utils.getGameMode(gma);
        assertEquals("AcidIsland", result);
    }

    @Test
    void testGetNextValue() {
        String[] values = {"a", "b", "c"};
        assertEquals("b", Utils.getNextValue(values, "a"));
        assertEquals("c", Utils.getNextValue(values, "b"));
        assertEquals("a", Utils.getNextValue(values, "c")); // wrap around
    }

    @Test
    void testGetNextValueNotFound() {
        String[] values = {"a", "b", "c"};
        assertEquals("d", Utils.getNextValue(values, "d")); // returns current if not found
    }

    @Test
    void testGetPreviousValue() {
        String[] values = {"a", "b", "c"};
        assertEquals("c", Utils.getPreviousValue(values, "a")); // wrap around
        assertEquals("a", Utils.getPreviousValue(values, "b"));
        assertEquals("b", Utils.getPreviousValue(values, "c"));
    }

    @Test
    void testGetPreviousValueNotFound() {
        String[] values = {"a", "b", "c"};
        assertEquals("d", Utils.getPreviousValue(values, "d")); // returns current if not found
    }

    @Test
    void testReadIntArrayWithIntegers() {
        int[] result = Utils.readIntArray(List.of(1, 3, 5));
        assertArrayEquals(new int[]{1, 3, 5}, result);
    }

    @Test
    void testReadIntArrayWithRange() {
        int[] result = Utils.readIntArray(List.of("1-3"));
        assertArrayEquals(new int[]{1, 2, 3}, result);
    }

    @Test
    void testReadIntArrayWithMixed() {
        int[] result = Utils.readIntArray(List.of(0, "2-4", 7));
        assertArrayEquals(new int[]{0, 2, 3, 4, 7}, result);
    }

    @Test
    void testReadIntArrayEmpty() {
        int[] result = Utils.readIntArray(Collections.emptyList());
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void testReadIntArraySingleStringNumber() {
        int[] result = Utils.readIntArray(List.of("5"));
        assertArrayEquals(new int[]{5}, result);
    }
}
