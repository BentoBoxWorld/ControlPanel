package world.bentobox.controlpanel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import world.bentobox.bentobox.api.addons.Addon.State;
import world.bentobox.bentobox.api.addons.AddonDescription;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.database.AbstractDatabaseHandler;
import world.bentobox.bentobox.database.Database;
import world.bentobox.bentobox.database.DatabaseSetup;
import world.bentobox.bentobox.managers.AddonsManager;
import world.bentobox.controlpanel.config.Settings;
import world.bentobox.controlpanel.managers.ControlPanelManager;

class ControlPanelAddonTest extends CommonTestSetup {

    private ControlPanelAddon addon;

    @Mock
    private AddonsManager addonsManager;

    private MockedStatic<DatabaseSetup> mockedDbSetup;

    @SuppressWarnings("unchecked")
    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        addon = new ControlPanelAddon();

        // Set up data folder
        File dataFolder = new File("addons/ControlPanel");
        dataFolder.mkdirs();
        addon.setDataFolder(dataFolder);

        // Create JAR file with config.yml
        File jFile = new File("addon.jar");
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jFile))) {
            // Add config.yml
            Path configSrc = Paths.get("src/main/resources/config.yml");
            if (Files.exists(configSrc)) {
                Path configDest = Paths.get("config.yml");
                Files.copy(configSrc, configDest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                addToJar(configDest, jos);
                Files.deleteIfExists(configDest);
            }
            // Add controlPanelTemplate.yml
            Path templateSrc = Paths.get("src/main/resources/controlPanelTemplate.yml");
            if (Files.exists(templateSrc)) {
                Path templateDest = Paths.get("controlPanelTemplate.yml");
                Files.copy(templateSrc, templateDest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                addToJar(templateDest, jos);
                Files.deleteIfExists(templateDest);
            }
        }
        addon.setFile(jFile);

        AddonDescription desc = new AddonDescription.Builder("main", "ControlPanel", "1.16.0")
                .description("test").authors("tastybento").build();
        addon.setDescription(desc);

        when(plugin.getAddonsManager()).thenReturn(addonsManager);
        when(addonsManager.getGameModeAddons()).thenReturn(Collections.emptyList());
        when(plugin.isEnabled()).thenReturn(true);
        when(plugin.getLogger()).thenReturn(java.util.logging.Logger.getAnonymousLogger());
        when(plugin.getFlagsManager()).thenReturn(fm);
        when(fm.getFlags()).thenReturn(Collections.emptyList());

        // Mock DatabaseSetup for ControlPanelManager creation
        AbstractDatabaseHandler<Object> handler = mock(AbstractDatabaseHandler.class);
        when(handler.loadObjects()).thenReturn(Collections.emptyList());
        when(handler.saveObject(any())).thenReturn(CompletableFuture.completedFuture(true));
        DatabaseSetup dbSetup = mock(DatabaseSetup.class);
        when(dbSetup.getHandler(any())).thenReturn(handler);
        mockedDbSetup = Mockito.mockStatic(DatabaseSetup.class);
        mockedDbSetup.when(() -> DatabaseSetup.getDatabase()).thenReturn(dbSetup);
        WhiteBox.setInternalState(Database.class, "databaseSetup", dbSetup);
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        mockedDbSetup.closeOnDemand();
        super.tearDown();
        deleteAll(new File("addons"));
        Files.deleteIfExists(Paths.get("addon.jar"));
    }

    private void addToJar(Path path, JarOutputStream jos) throws IOException {
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            JarEntry entry = new JarEntry(path.toString());
            jos.putNextEntry(entry);
            while ((bytesRead = fis.read(buffer)) != -1) {
                jos.write(buffer, 0, bytesRead);
            }
        }
    }

    @Test
    void testOnLoadSuccess() {
        Settings settings = new Settings();
        try (MockedConstruction<Config> mockedConfig = Mockito.mockConstruction(Config.class,
                (mock, context) -> when(mock.loadConfigObject()).thenReturn(settings))) {
            addon.onLoad();
            assertNotNull(addon.getSettings());
        }
    }

    @Test
    void testOnLoadFailNullSettings() {
        try (MockedConstruction<Config> mockedConfig = Mockito.mockConstruction(Config.class,
                (mock, context) -> when(mock.loadConfigObject()).thenReturn(null))) {
            addon.onLoad();
            assertNull(addon.getSettings());
        }
    }

    @Test
    void testOnEnableWithGameModes() {
        Settings settings = new Settings();
        try (MockedConstruction<Config> mockedConfig = Mockito.mockConstruction(Config.class,
                (mock, context) -> when(mock.loadConfigObject()).thenReturn(settings))) {
            addon.onLoad();
        }

        GameModeAddon gma = mock(GameModeAddon.class);
        AddonDescription gmaDesc = new AddonDescription.Builder("main", "BSkyBlock", "1.0").build();
        when(gma.getDescription()).thenReturn(gmaDesc);

        CompositeCommand playerCmd = mock(CompositeCommand.class);
        when(playerCmd.getSubCommandAliases()).thenReturn(new HashMap<>());
        when(playerCmd.getAddon()).thenReturn(addon);
        when(gma.getPlayerCommand()).thenReturn(Optional.of(playerCmd));

        CompositeCommand adminCmd = mock(CompositeCommand.class);
        when(adminCmd.getSubCommandAliases()).thenReturn(new HashMap<>());
        when(adminCmd.getAddon()).thenReturn(addon);
        when(gma.getAdminCommand()).thenReturn(Optional.of(adminCmd));

        when(addonsManager.getGameModeAddons()).thenReturn(Collections.singletonList(gma));

        addon.setState(State.ENABLED);
        addon.onEnable();
        assertNotNull(addon.getAddonManager());
    }

    @Test
    void testOnReloadSuccess() {
        Settings settings = new Settings();
        try (MockedConstruction<Config> mockedConfig = Mockito.mockConstruction(Config.class,
                (mock, context) -> when(mock.loadConfigObject()).thenReturn(settings))) {
            addon.onLoad();
        }
        addon.setState(State.ENABLED);
        addon.onEnable();

        Settings newSettings = new Settings();
        try (MockedConstruction<Config> mockedConfig = Mockito.mockConstruction(Config.class,
                (mock, context) -> when(mock.loadConfigObject()).thenReturn(newSettings))) {
            addon.onReload();
            assertNotNull(addon.getSettings());
        }
    }

    @Test
    void testOnDisable() {
        Settings settings = new Settings();
        try (MockedConstruction<Config> mockedConfig = Mockito.mockConstruction(Config.class,
                (mock, context) -> {
                    when(mock.loadConfigObject()).thenReturn(settings);
                    when(mock.saveConfigObject(any())).thenReturn(true);
                })) {
            addon.onLoad();
            addon.setState(State.ENABLED);
            addon.onEnable();
            addon.onDisable();
        }
    }

    @Test
    void testGetSettingsDefault() {
        assertNull(addon.getSettings());
    }

    @Test
    void testGetAddonManagerDefault() {
        assertNull(addon.getAddonManager());
    }
}
