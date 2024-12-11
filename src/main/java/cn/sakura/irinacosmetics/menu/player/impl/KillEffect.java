package cn.sakura.irinacosmetics.menu.player.impl;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import cn.sakura.irinacosmetics.data.BalanceManager;
import cn.sakura.irinacosmetics.data.PlayerData;
import cn.sakura.irinacosmetics.util.CC;
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

import java.util.ArrayList;
import java.util.List;

@Register
public class KillEffect extends AbstractMenu implements Listener {
    private static final String irina = IrinaCosmetics.irina;

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
        PlayerData data = PlayerData.getPlayerData(player);
        addItemToInventory(0, new ItemUtil(Material.BARRIER).setInternalName("NullEffect").build(), "&a无", List.of("", "&7默认的特效"));

        for (int i = 1; i <= Math.min(EffectManager.KillEffects.size(), 35); i++) {
            AbstractEffect effect = EffectManager.KillEffects.get(i - 1);

            String buyOrUse = data.getUnlockedEffects().contains(effect.getEffectInternalName()) ? "&7[&a点击使用&7]" : "&7[&e点击购买&7]";

            addItemToInventory(i, new ItemUtil(effect.getIcon()).setInternalName(effect.getEffectInternalName()).build(), "&r" + effect.getDisplayName(),
                    List.of(
                            "&8击杀特效",
                            "",
                            "&7选择 &f" + effect.getDisplayName() + " &7作为你的击杀特效",
                            "",
                            "&7花费: &6" + df.format(effect.getPrice()),
                            "",
                            buyOrUse
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
            EffectManager.getInstance().removePlayerEffect(player, EffectType.KILL);
            return;
        }

        String effectInternalName = ItemUtil.getInternalName(clickedItem);
        AbstractEffect effect = EffectManager.getInstance().getEffect(effectInternalName);

        PlayerData data = PlayerData.getPlayerData(player);

        boolean haveEffect = false;
        for (String effectName : data.getUnlockedEffects()) {
            if (effectInternalName.equalsIgnoreCase(effectName)) {
                haveEffect = true;
                break;
            }
        }

        if (!haveEffect) {
            if (BalanceManager.getBalance(player.getUniqueId()) >= effect.getPrice()) {
                BalanceManager.takeBalance(player.getUniqueId(), effect.getPrice());

                player.sendMessage(CC.translate(irina + "&a购买成功!"));

                EffectManager.getInstance().getPlayerUnlockedEffects(player).add(effectInternalName);
            } else {
                player.sendMessage(CC.translate(irina + "&c笨蛋主人, 你没有钱购买这个!"));
            }
        }

        EffectManager.getInstance().setPlayerEffect(player, effectInternalName, EffectType.KILL);
        data.setKillEffect(EffectManager.getInstance().getPlayerEffect(player, EffectType.KILL));

        PlayerData.getData().put(player.getUniqueId(), data);
    }
}
