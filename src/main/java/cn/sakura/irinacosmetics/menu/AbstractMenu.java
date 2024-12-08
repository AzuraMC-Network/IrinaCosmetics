package cn.sakura.irinacosmetics.menu;

import cn.sakura.irinacosmetics.data.BalanceManager;
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

import java.text.DecimalFormat;
import java.util.List;

import static cn.sakura.irinacosmetics.util.ItemUtil.createGlassPane;

@Register
public abstract class AbstractMenu implements Listener {
    protected DecimalFormat df = new DecimalFormat("###,###,###");
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
        ItemStack blackGlassPane = createGlassPane((short) 15);
        ItemStack redGlassPane = createGlassPane((short) 14);
        ItemStack greenGlassPane = createGlassPane((short) 13);

        int coin = BalanceManager.getBalance(player.getUniqueId());

        for (int slots = 45; slots <= 53; slots++) {
            switch (slots) {
                case 45:
                    addItemToInventory(slots, new ItemUtil(redGlassPane).setInternalName("close").build(), "&c关闭", List.of(""));
                    break;
                case 49:
                    addItemToInventory(slots,
                            new ItemUtil(new ItemStack(Material.SKULL_ITEM, 1, (short) 3))
                                    .setSkullOwner(player.getName())
                                    .build(),
                            "&7余额: &e" + coin, List.of("", "&bI&fRINA"));
                    break;
                case 53:
                    addItemToInventory(slots, new ItemUtil(greenGlassPane).setInternalName("back").build(), "&a返回", List.of(""));
                    break;
                default:
                    addItemToInventory(slots, blackGlassPane, "&r", List.of(""));
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (ItemUtil.getInternalName(currentItem) == null) return;

        switch (ItemUtil.getInternalName(currentItem).toLowerCase()) {
            case "close":
                player.closeInventory();
                return;
            case "back":
                EffectTypeSelect effectTypeSelect = new EffectTypeSelect();
                effectTypeSelect.open(player);
                return;
            default:
                return;
        }
    }
}
