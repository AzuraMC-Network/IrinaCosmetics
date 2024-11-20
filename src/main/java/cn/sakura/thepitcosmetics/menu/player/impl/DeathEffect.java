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

public class DeathEffect extends AbstractMenu implements Listener {
    @Override
    public String getMenuName() {
        return "亡语";
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

        for (int i = 1; i <= Math.min(EffectManager.DeathEffects.size(), 35); i++) {
            AbstractEffect effect = EffectManager.DeathEffects.get(i - 1);

            addItemToInventory(i, new ItemBuilder(effect.getIcon()).internalName(effect.getEffectInternalName()), effect.getDisplayName(),
                    List.of(
                            "&8亡语",
                            "",
                            "&7选择 &f" + effect.getDisplayName() + " &7作为你的亡语",
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
            EffectManager.getInstance().removePlayerDeathEffect(player);
            return;
        }

        String effectInternalName = ItemUtil.getInternalName(clickedItem);

        EffectManager.getInstance().setPlayerDeathEffect(player, effectInternalName);
    }

//    private List<String> checkSelectEffect(Player player, AbstractEffect checkEffect) {
//        AbstractEffect effect = EffectManager.getInstance().getPlayerDeathEffect(player);
//        if (effect == checkEffect) {
//            return List.of(
//                    effect.getLore() + "",
//                    "",
//                    "&7[&a点击选择&7]"
//            );
//        }
//        return List.of(
//                effect.getLore() + "",
//                "",
//                "&7花费: &65,000",
//                "",
//                "&7[&e点击购买&7]"
//        );
//    }
}
