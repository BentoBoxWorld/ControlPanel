package world.bentobox.controlpanel.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    void testTitleConstant() {
        assertEquals("controlpanel.gui.titles.", Constants.TITLE);
    }

    @Test
    void testButtonConstant() {
        assertEquals("controlpanel.gui.buttons.", Constants.BUTTON);
    }

    @Test
    void testDescriptionConstant() {
        assertEquals("controlpanel.gui.descriptions.", Constants.DESCRIPTION);
    }

    @Test
    void testMessageConstant() {
        assertEquals("controlpanel.messages.", Constants.MESSAGE);
    }

    @Test
    void testErrorsConstant() {
        assertEquals("controlpanel.errors.", Constants.ERRORS);
    }

    @Test
    void testQuestionsConstant() {
        assertEquals("controlpanel.questions.", Constants.QUESTIONS);
    }

    @Test
    void testCommandsConstant() {
        assertEquals("controlpanel.commands.", Constants.COMMANDS);
    }

    @Test
    void testTypesConstant() {
        assertEquals("controlpanel.types.", Constants.TYPES);
    }

    @Test
    void testVariables() {
        assertEquals("[gamemode]", Constants.VARIABLE_GAMEMODE);
        assertEquals("[admin]", Constants.VARIABLE_ADMIN);
        assertEquals("[file]", Constants.VARIABLE_FILENAME);
        assertEquals("[message]", Constants.VARIABLE_MESSAGE);
    }

    @Test
    void testAllConstantsStartWithControlPanel() {
        assertTrue(Constants.TITLE.startsWith("controlpanel."));
        assertTrue(Constants.BUTTON.startsWith("controlpanel."));
        assertTrue(Constants.DESCRIPTION.startsWith("controlpanel."));
        assertTrue(Constants.MESSAGE.startsWith("controlpanel."));
        assertTrue(Constants.ERRORS.startsWith("controlpanel."));
        assertTrue(Constants.QUESTIONS.startsWith("controlpanel."));
        assertTrue(Constants.COMMANDS.startsWith("controlpanel."));
        assertTrue(Constants.TYPES.startsWith("controlpanel."));
    }
}
