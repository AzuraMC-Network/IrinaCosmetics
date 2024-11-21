package cn.sakura.thepitcosmetics.menu;

import cn.sakura.thepitcosmetics.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class AbstractMenu {
    protected Inventory inventory;

    public abstract String getMenuName();
    public abstract int getMenuSize();

    public void open(Player player) {
        inventory = Bukkit.createInventory(null, getMenuSize(), getMenuName());
        setupItems(player);
        player.openInventory(inventory);
    }

    protected abstract void setupItems(Player player);

    protected void addItemToInventory(int slot, ItemStack material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(CC.translate(displayName));
            meta.setLore(CC.translate(lore));
            item.setItemMeta(meta);
        }

        inventory.setItem(slot, item);
    }
}
