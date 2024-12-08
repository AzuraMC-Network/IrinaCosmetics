package cn.sakura.irinacosmetics.cosmetics.impl.kill;

import cn.charlotte.pit.util.item.ItemBuilder;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import cn.sakura.irinacosmetics.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestKillEffect extends AbstractEffect {

    @Override
    public String getDisplayName() {
        return "TestEffect";
    }

    @Override
    public String getEffectInternalName() {
        return "testKillEffect";
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.KILL;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.BOW).shiny().build();
    }

    @Override
    public void handleShoot(Player shooter, Arrow arrow) {

    }

    @Override
    public void handleDeath(Player myself) {

    }

    @Override
    public void handleKill(Player target) {
        PlayerUtil.playThunderEffect(target.getLocation());
    }
}
