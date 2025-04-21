package world.bentobox.controlpanel.utils;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderParse {
    public static ItemStack parse(String string, ItemStack itemStack)
    {
        if(!Bukkit.getServer().getPluginManager().isPluginEnabled("ItemsAdder")) return itemStack;
        CustomStack stack = CustomStack.getInstance(string);
        if(stack != null) return stack.getItemStack();
        return itemStack;
    }
    public static ItemStack parse(String string)
    {
        return parse(string, new ItemStack(Material.PAPER));
    }
}
