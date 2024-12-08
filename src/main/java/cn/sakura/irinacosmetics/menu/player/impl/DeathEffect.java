package cn.sakura.irinacosmetics.menu.player.impl;

import cn.sakura.irinacosmetics.util.ItemUtil;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.game.Register;
import cn.sakura.irinacosmetics.menu.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Register
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
        addItemToInventory(0, new ItemUtil(Material.BARRIER).setInternalName("NullEffect").build(), "&a无", List.of("", "&7默认的特效"));

        for (int i = 1; i <= Math.min(EffectManager.DeathEffects.size(), 35); i++) {
            AbstractEffect effect = EffectManager.DeathEffects.get(i - 1);

            addItemToInventory(i, new ItemUtil(effect.getIcon()).setInternalName(effect.getEffectInternalName()).build(), "&r" + effect.getDisplayName(),
                    List.of(
                            "&8亡语",
                            "",
                            "&7选择 &f" + effect.getDisplayName() + " &7作为你的亡语",
                            "",
                            "&7花费: &6" + df.format(effect.getPrice()),
                            "",
                            "&7[&e点击购买&7]"
                    )
            );
        }

        addFrame(player);
    }

    @EventHandler
    public void handleClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(getMenuName())) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (ItemUtil.getInternalName(clickedItem) == null || clickedItem.getType() == Material.STAINED_GLASS_PANE) return;

        if (ItemUtil.getInternalName(clickedItem).equalsIgnoreCase("nulleffect")) {
            EffectManager.getInstance().removePlayerDeathEffect(player);
            return;
        }

        String effectInternalName = ItemUtil.getInternalName(clickedItem);

        EffectManager.getInstance().setPlayerDeathEffect(player, effectInternalName);
    }

}
