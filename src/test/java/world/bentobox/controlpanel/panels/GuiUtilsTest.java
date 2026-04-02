package world.bentobox.controlpanel.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import world.bentobox.controlpanel.CommonTestSetup;

class GuiUtilsTest extends CommonTestSetup {

    @Test
    void testStringSplitSimple() {
        List<String> result = GuiUtils.stringSplit("Hello world", 999, false);
        assertFalse(result.isEmpty());
        assertEquals("Hello world", result.get(0));
    }

    @Test
    void testStringSplitWithPipe() {
        List<String> result = GuiUtils.stringSplit("Line1|Line2|Line3", 999, false);
        assertEquals(3, result.size());
        assertEquals("Line1", result.get(0));
        assertEquals("Line2", result.get(1));
        assertEquals("Line3", result.get(2));
    }

    @Test
    void testStringSplitList() {
        List<String> input = Arrays.asList("Hello", "World");
        List<String> result = GuiUtils.stringSplit(input, 999);
        assertEquals(2, result.size());
    }

    @Test
    void testStringSplitEmptyList() {
        List<String> input = Collections.emptyList();
        List<String> result = GuiUtils.stringSplit(input, 999);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMaterialItemNormal() {
        ItemStack result = GuiUtils.getMaterialItem(Material.STONE);
        assertNotNull(result);
        assertEquals(Material.STONE, result.getType());
        assertEquals(1, result.getAmount());
    }

    @Test
    void testGetMaterialItemWithAmount() {
        ItemStack result = GuiUtils.getMaterialItem(Material.STONE, 5);
        assertEquals(5, result.getAmount());
    }

    @Test
    void testGetMaterialItemWallMaterial() {
        ItemStack result = GuiUtils.getMaterialItem(Material.OAK_WALL_SIGN);
        assertEquals(Material.OAK_SIGN, result.getType());
    }

    @Test
    void testGetMaterialItemPottedPlant() {
        ItemStack result = GuiUtils.getMaterialItem(Material.POTTED_OAK_SAPLING);
        assertEquals(Material.OAK_SAPLING, result.getType());
    }

    @Test
    void testGetMaterialItemMelonStem() {
        ItemStack result = GuiUtils.getMaterialItem(Material.MELON_STEM);
        assertEquals(Material.MELON_SEEDS, result.getType());
    }

    @Test
    void testGetMaterialItemAttachedMelonStem() {
        ItemStack result = GuiUtils.getMaterialItem(Material.ATTACHED_MELON_STEM);
        assertEquals(Material.MELON_SEEDS, result.getType());
    }

    @Test
    void testGetMaterialItemPumpkinStem() {
        ItemStack result = GuiUtils.getMaterialItem(Material.PUMPKIN_STEM);
        assertEquals(Material.PUMPKIN_SEEDS, result.getType());
    }

    @Test
    void testGetMaterialItemCarrots() {
        ItemStack result = GuiUtils.getMaterialItem(Material.CARROTS);
        assertEquals(Material.CARROT, result.getType());
    }

    @Test
    void testGetMaterialItemBeetroots() {
        ItemStack result = GuiUtils.getMaterialItem(Material.BEETROOTS);
        assertEquals(Material.BEETROOT, result.getType());
    }

    @Test
    void testGetMaterialItemPotatoes() {
        ItemStack result = GuiUtils.getMaterialItem(Material.POTATOES);
        assertEquals(Material.POTATO, result.getType());
    }

    @Test
    void testGetMaterialItemCocoa() {
        ItemStack result = GuiUtils.getMaterialItem(Material.COCOA);
        assertEquals(Material.COCOA_BEANS, result.getType());
    }

    @Test
    void testGetMaterialItemKelpPlant() {
        ItemStack result = GuiUtils.getMaterialItem(Material.KELP_PLANT);
        assertEquals(Material.KELP, result.getType());
    }

    @Test
    void testGetMaterialItemRedstoneWire() {
        ItemStack result = GuiUtils.getMaterialItem(Material.REDSTONE_WIRE);
        assertEquals(Material.REDSTONE, result.getType());
    }

    @Test
    void testGetMaterialItemTripwire() {
        ItemStack result = GuiUtils.getMaterialItem(Material.TRIPWIRE);
        assertEquals(Material.STRING, result.getType());
    }

    @Test
    void testGetMaterialItemFrostedIce() {
        ItemStack result = GuiUtils.getMaterialItem(Material.FROSTED_ICE);
        assertEquals(Material.ICE, result.getType());
    }

    @Test
    void testGetMaterialItemEndPortal() {
        ItemStack result = GuiUtils.getMaterialItem(Material.END_PORTAL);
        assertEquals(Material.PAPER, result.getType());
    }

    @Test
    void testGetMaterialItemWater() {
        ItemStack result = GuiUtils.getMaterialItem(Material.WATER);
        assertEquals(Material.WATER_BUCKET, result.getType());
    }

    @Test
    void testGetMaterialItemLava() {
        ItemStack result = GuiUtils.getMaterialItem(Material.LAVA);
        assertEquals(Material.LAVA_BUCKET, result.getType());
    }

    @Test
    void testGetMaterialItemFire() {
        ItemStack result = GuiUtils.getMaterialItem(Material.FIRE);
        assertEquals(Material.FIRE_CHARGE, result.getType());
    }

    @Test
    void testGetMaterialItemAir() {
        ItemStack result = GuiUtils.getMaterialItem(Material.AIR);
        assertEquals(Material.GLASS_BOTTLE, result.getType());
    }

    @Test
    void testGetMaterialItemPistonHead() {
        ItemStack result = GuiUtils.getMaterialItem(Material.PISTON_HEAD);
        assertEquals(Material.PISTON, result.getType());
    }

    @Test
    void testWrapMethod() {
        String result = GuiUtils.wrap("short", 100);
        assertEquals("short", result);
    }
}
