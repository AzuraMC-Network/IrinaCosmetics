package cn.sakura.thepitcosmetics.menu.player.impl;

import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import cn.sakura.thepitcosmetics.menu.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KillEffect extends AbstractMenu implements Listener {
    @Override
    public String getMenuName() {
        return "击杀";
    }

    @Override
    public int getMenuSize() {
        return 54;
    }

    @Override
    protected void setupItems(Player player) {
        ItemStack blackGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        blackGlassPane.setDurability((short) 15);
        addItemToInventory(0, new ItemBuilder(Material.BARRIER).internalName("NullEffect"), "&a无", List.of("", "&7默认的特效"));

        for (int i = 1; i <= Math.min(EffectManager.KillEffects.size(), 35); i++) {
            AbstractEffect effect = EffectManager.KillEffects.get(i - 1);

            addItemToInventory(i, new ItemBuilder(effect.getIcon()).internalName(effect.getEffectInternalName()), effect.getDisplayName(),
                    List.of(
                            "&8击杀特效",
                            "",
                            "&7选择 &f" + effect.getDisplayName() + " &7作为你的击杀特效",
                            "",
                            "&7花费: &65,000",
                            "",
                            "&7[&e点击购买&7]"
                    )
            );
        }

        for (int slots = 36; slots <= 44; slots++) {
            addItemToInventory(slots, new ItemBuilder(blackGlassPane), "&r", List.of(""));
        }
    }

    @EventHandler
    public void handleClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(getMenuName())) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (ItemUtil.getInternalName(clickedItem) == null) return;

        if (ItemUtil.getInternalName(clickedItem).equalsIgnoreCase("nulleffect")) {
            EffectManager.getInstance().removePlayerKillEffect(player);
            return;
        }

        String effectInternalName = ItemUtil.getInternalName(clickedItem);

        EffectManager.getInstance().setPlayerKillEffect(player, effectInternalName);
    }
}
