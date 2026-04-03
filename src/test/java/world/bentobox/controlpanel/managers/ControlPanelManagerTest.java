package world.bentobox.controlpanel.managers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.bukkit.permissions.PermissionAttachmentInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.AbstractDatabaseHandler;
import world.bentobox.bentobox.database.Database;
import world.bentobox.bentobox.database.DatabaseSetup;
import world.bentobox.controlpanel.CommonTestSetup;
import world.bentobox.controlpanel.ControlPanelAddon;
import world.bentobox.controlpanel.WhiteBox;
import world.bentobox.controlpanel.database.objects.ControlPanelObject;
import world.bentobox.controlpanel.database.objects.ControlPanelObject.ControlPanelButton;
import world.bentobox.controlpanel.utils.Utils;

@SuppressWarnings("unchecked")
class ControlPanelManagerTest extends CommonTestSetup {

    private ControlPanelManager manager;

    @Mock
    private ControlPanelAddon addon;

    private MockedStatic<Utils> mockedUtils;
    private MockedStatic<DatabaseSetup> mockedDbSetup;
    private AbstractDatabaseHandler<Object> handler;

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        when(addon.getPlugin()).thenReturn(plugin);
        when(addon.getLogger()).thenReturn(Logger.getAnonymousLogger());

        File dataFolder = new File("addons/ControlPanel");
        dataFolder.mkdirs();
        when(addon.getDataFolder()).thenReturn(dataFolder);

        // Create the template file so the constructor doesn't try to save resources
        File templateFile = new File(dataFolder, "controlPanelTemplate.yml");
        if (!templateFile.exists()) {
            templateFile.createNewFile();
        }

        // Mock Utils static methods
        mockedUtils = Mockito.mockStatic(Utils.class);
        mockedUtils.when(() -> Utils.getGameMode(any(org.bukkit.World.class))).thenReturn("BSkyBlock");
        mockedUtils.when(() -> Utils.getGameMode(any(GameModeAddon.class))).thenReturn("BSkyBlock");
        mockedUtils.when(() -> Utils.getPermissionValue(any(), anyString(), any())).thenCallRealMethod();
        mockedUtils.when(() -> Utils.readIntArray(any())).thenCallRealMethod();

        // Mock DatabaseSetup to return a mock handler
        handler = mock(AbstractDatabaseHandler.class);
        when(handler.loadObjects()).thenReturn(Collections.emptyList());
        when(handler.saveObject(any())).thenReturn(CompletableFuture.completedFuture(true));
        DatabaseSetup dbSetup = mock(DatabaseSetup.class);
        when(dbSetup.getHandler(any())).thenReturn(handler);
        mockedDbSetup = Mockito.mockStatic(DatabaseSetup.class);
        mockedDbSetup.when(() -> DatabaseSetup.getDatabase()).thenReturn(dbSetup);
        // Also set the static field directly since it may have been initialized already
        WhiteBox.setInternalState(Database.class, "databaseSetup", dbSetup);

        manager = new ControlPanelManager(addon);
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        mockedUtils.closeOnDemand();
        mockedDbSetup.closeOnDemand();
        super.tearDown();
        deleteAll(new File("addons"));
    }

    @Test
    void testConstructor() {
        assertNotNull(manager);
    }

    @Test
    void testLoadWithObjects() throws Exception {
        ControlPanelObject cpo = createDefaultPanel();
        when(handler.loadObjects()).thenReturn(Collections.singletonList(cpo));

        manager.reload();
        assertTrue(manager.hasAnyControlPanel(world));
    }

    @Test
    void testLoadMigratesNullName() throws Exception {
        ControlPanelObject cpo = new ControlPanelObject();
        cpo.setUniqueId("BSkyBlock_default");
        cpo.setGameMode("BSkyBlock");
        cpo.setDefaultPanel(true);

        ControlPanelButton button = new ControlPanelButton();
        button.setSlot(0);
        button.setName(null);
        button.setCommand("test command");
        button.setDescriptionLines(new ArrayList<>());
        cpo.setPanelButtons(new ArrayList<>(List.of(button)));

        when(handler.loadObjects()).thenReturn(Collections.singletonList(cpo));

        manager.reload();
        assertTrue(manager.hasAnyControlPanel(world));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testLoadMigratesDeprecatedDescription() throws Exception {
        ControlPanelObject cpo = new ControlPanelObject();
        cpo.setUniqueId("BSkyBlock_default");
        cpo.setGameMode("BSkyBlock");
        cpo.setDefaultPanel(true);

        ControlPanelButton button = new ControlPanelButton();
        button.setSlot(0);
        button.setName("Test");
        button.setCommand("test");
        button.setDescription("old single description");
        button.setDescriptionLines(null);
        cpo.setPanelButtons(new ArrayList<>(List.of(button)));

        when(handler.loadObjects()).thenReturn(Collections.singletonList(cpo));

        manager.reload();
        assertTrue(manager.hasAnyControlPanel(world));
    }

    @Test
    void testHasAnyControlPanelEmpty() {
        assertFalse(manager.hasAnyControlPanel(world));
    }

    @Test
    void testHasAnyControlPanelEmptyGameMode() {
        mockedUtils.when(() -> Utils.getGameMode(any(org.bukkit.World.class))).thenReturn("");
        assertFalse(manager.hasAnyControlPanel(world));
    }

    @Test
    void testHasAnyControlPanelGameModeAddon() {
        GameModeAddon gma = mock(GameModeAddon.class);
        assertFalse(manager.hasAnyControlPanel(gma));
    }

    @Test
    void testGetUserControlPanelNoPermission() {
        User user = User.getInstance(mockPlayer);
        when(mockPlayer.getEffectivePermissions()).thenReturn(Collections.emptySet());

        ControlPanelObject result = manager.getUserControlPanel(user, world, "bskyblock.");
        assertNull(result);
    }

    @Test
    void testGetUserControlPanelDefaultFallback() throws Exception {
        ControlPanelObject cpo = createDefaultPanel();
        when(handler.loadObjects()).thenReturn(Collections.singletonList(cpo));
        manager.reload();

        User user = User.getInstance(mockPlayer);
        when(mockPlayer.getEffectivePermissions()).thenReturn(Collections.emptySet());

        ControlPanelObject result = manager.getUserControlPanel(user, world, "bskyblock.");
        assertNotNull(result);
        assertTrue(result.isDefaultPanel());
    }

    @Test
    void testGetUserControlPanelWithPermission() throws Exception {
        ControlPanelObject cpo = new ControlPanelObject();
        cpo.setUniqueId("BSkyBlock_vip");
        cpo.setGameMode("BSkyBlock");
        cpo.setPermissionSuffix("vip");
        cpo.setDefaultPanel(false);
        cpo.setPanelButtons(new ArrayList<>());

        when(handler.loadObjects()).thenReturn(Collections.singletonList(cpo));
        manager.reload();

        User user = User.getInstance(mockPlayer);
        PermissionAttachmentInfo pai = mock(PermissionAttachmentInfo.class);
        when(pai.getPermission()).thenReturn("bskyblock.controlpanel.panel.vip");
        when(mockPlayer.getEffectivePermissions()).thenReturn(Set.of(pai));

        ControlPanelObject result = manager.getUserControlPanel(user, world, "bskyblock.");
        assertNotNull(result);
        assertEquals("BSkyBlock_vip", result.getUniqueId());
    }

    @Test
    void testSave() {
        manager.save();
    }

    @Test
    void testWipeDataEmptyGameMode() {
        mockedUtils.when(() -> Utils.getGameMode(any(org.bukkit.World.class))).thenReturn("");
        User user = User.getInstance(mockPlayer);
        manager.wipeData(world, user);
    }

    @Test
    void testWipeDataNoExistingPanels() {
        User user = User.getInstance(mockPlayer);
        manager.wipeData(world, user);
    }

    @Test
    void testImportControlPanelsNoFile() {
        User user = User.getInstance(mockPlayer);
        manager.importControlPanels(user, world, "nonexistent.yml");
        checkSpigotMessage("controlpanel.errors.no-file");
    }

    @Test
    void testImportControlPanelsEmptyGameMode() {
        mockedUtils.when(() -> Utils.getGameMode(any(org.bukkit.World.class))).thenReturn("");
        User user = User.getInstance(mockPlayer);
        manager.importControlPanels(user, world, "test.yml");
        checkSpigotMessage("controlpanel.errors.not-a-gamemode-world");
    }

    @Test
    void testImportControlPanelsNullUser() {
        mockedUtils.when(() -> Utils.getGameMode(any(org.bukkit.World.class))).thenReturn("");
        manager.importControlPanels(null, world, "test.yml");
    }

    @Test
    void testReload() {
        manager.reload();
    }

    private ControlPanelObject createDefaultPanel() {
        ControlPanelObject cpo = new ControlPanelObject();
        cpo.setUniqueId("BSkyBlock_default");
        cpo.setGameMode("BSkyBlock");
        cpo.setPanelName("Commands");
        cpo.setDefaultPanel(true);

        ControlPanelButton button = new ControlPanelButton();
        button.setSlot(0);
        button.setName("Test");
        button.setCommand("test");
        button.setDescriptionLines(new ArrayList<>());
        cpo.setPanelButtons(new ArrayList<>(List.of(button)));

        return cpo;
    }
}
