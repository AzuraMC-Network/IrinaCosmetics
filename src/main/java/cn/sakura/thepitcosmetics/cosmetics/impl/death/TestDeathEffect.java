package cn.sakura.thepitcosmetics.cosmetics.impl.death;

import cn.charlotte.pit.util.item.ItemBuilder;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.util.CC;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TestDeathEffect extends AbstractEffect {

    @Override
    public String getDisplayName() {
        return "TestEffect";
    }

    @Override
    public String getEffectInternalName() {
        return "testDeathEffect";
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.BOW).shiny().build();
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("这是一个测试特效");

        return lore;
    }

    @Override
    public void handleShoot(Player shooter, Arrow arrow) {

    }

    @Override
    public void handleDeath(Player myself) {
        myself.playSound(myself.getLocation(), Sound.WOLF_DEATH, 3F, 1F);
        CC.broadcast(myself.getDisplayName() + " 已经趋势了");
    }

    @Override
    public void handleKill(Player target) {

    }
}
