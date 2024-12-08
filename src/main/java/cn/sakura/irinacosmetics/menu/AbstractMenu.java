package cn.sakura.irinacosmetics.menu;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.game.Register;
import cn.sakura.irinacosmetics.menu.player.EffectTypeSelect;
import cn.sakura.irinacosmetics.util.CC;
import cn.sakura.irinacosmetics.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Register
public abstract class AbstractMenu implements Listener {
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

    protected void addFrame(Player player) {
        ItemStack blackGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        blackGlassPane.setDurability((short) 15);
        ItemStack redGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        redGlassPane.setDurability((short)14);
        ItemStack greenGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        greenGlassPane.setDurability((short)13);
        int coin = IrinaCosmetics.getInstance().getBalance(player.getUniqueId());
        for (int slots = 45; slots <= 53; slots++) {
            if (slots == 45 ) {
                addItemToInventory(slots, new ItemUtil(redGlassPane).setInternalName("close").build(), "&c关闭", List.of(""));
                continue;
            }
            if (slots == 49) {
                addItemToInventory(slots, new ItemUtil(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).setSkullOwner(player.getName()).build(), "&7余额: &e" + coin, List.of("", "&bI&frina Cosmetics"));
                continue;
            }
            if (slots == 53) {
                addItemToInventory(slots, new ItemUtil(greenGlassPane).setInternalName("back").build(), "&a返回", List.of(""));
                continue;
            }
            addItemToInventory(slots,blackGlassPane, "&r", List.of(""));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (ItemUtil.getInternalName(currentItem).equalsIgnoreCase("close")) {
            player.closeInventory();
            return;
        }
        if (ItemUtil.getInternalName(currentItem).equalsIgnoreCase("back")) {
            EffectTypeSelect effectTypeSelect = new EffectTypeSelect();
            effectTypeSelect.open(player);
            return;
        }
    }
}
