package cn.sakura.thepitcosmetics.menu.player.impl;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import cn.sakura.thepitcosmetics.game.Register;
import cn.sakura.thepitcosmetics.menu.AbstractMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Register
public class ShootEffect extends AbstractMenu implements Listener {
    @Override
    public String getMenuName() {
        return "弹射物轨迹";
    }

    @Override
    public int getMenuSize() {
        return 54;
    }

    @Override
    protected void setupItems(Player player) {
        ItemStack blackGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        blackGlassPane.setDurability((short) 15);
        addItemToInventory(0, new ItemBuilder(Material.BARRIER).internalName("NullEffect").build(), "&a无", List.of("", "&7默认的特效"));

        for (int i = 1; i <= Math.min(EffectManager.ShootEffects.size(), 35); i++) {
            AbstractEffect effect = EffectManager.ShootEffects.get(i - 1);

            addItemToInventory(i, new ItemBuilder(effect.getIcon()).internalName(effect.getEffectInternalName()).build(), effect.getDisplayName(),
                    List.of(
                            "&8弹射物轨迹",
                            "",
                            "&7选择 &f" + effect.getDisplayName() + " &7作为你的弹射物轨迹",
                            "",
                            "&7花费: &65,000",
                            "",
                            "&7[&e点击购买&7]"
                    )
            );
        }

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        for (int slots = 36; slots <= 44; slots++) {
            if (slots == 40) {
                addItemToInventory(40, new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).setSkullOwner(player.getName()).build(), "&7余额: &e" + (int) profile.getCoins(), List.of("", "&3Miral&bElioraen"));
                continue;
            }
            addItemToInventory(slots, blackGlassPane, "&r", List.of(""));
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
            EffectManager.getInstance().removePlayerShootEffect(player);
            return;
        }

        String effectInternalName = ItemUtil.getInternalName(clickedItem);

        EffectManager.getInstance().setPlayerShootEffect(player, effectInternalName);
    }
}
