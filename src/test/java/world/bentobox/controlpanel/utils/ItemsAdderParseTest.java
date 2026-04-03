package world.bentobox.controlpanel.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import world.bentobox.controlpanel.CommonTestSetup;

class ItemsAdderParseTest extends CommonTestSetup {

    @Test
    void testParseWhenItemsAdderNotEnabled() {
        when(pim.isPluginEnabled("ItemsAdder")).thenReturn(false);
        ItemStack defaultItem = new ItemStack(Material.STONE);
        ItemStack result = ItemsAdderParse.parse("custom_item", defaultItem);
        assertEquals(defaultItem, result);
    }

    @Test
    void testParseConvenienceMethodWhenNotEnabled() {
        when(pim.isPluginEnabled("ItemsAdder")).thenReturn(false);
        ItemStack result = ItemsAdderParse.parse("custom_item");
        assertEquals(Material.PAPER, result.getType());
    }
}
