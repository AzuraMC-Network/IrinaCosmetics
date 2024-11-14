package cn.sakura.thepitcosmetics.cosmetics.impl.kill;

import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

    }

    @Override
    public void handleKill(Player target) {

    }
}
