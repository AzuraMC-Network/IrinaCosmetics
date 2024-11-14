package cn.sakura.thepitcosmetics.cosmetics.ShootEffects;

import cn.sakura.thepitcosmetics.access.ShootItemAccess;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffects;
import cn.sakura.thepitcosmetics.cosmetics.EffectType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TestShootEffect extends AbstractEffects implements ShootItemAccess {

    @Override
    public String getEffectName() {
        return "TestEffect";
    }

    @Override
    public String getEffectInternalName() {
        return "testEffect";
    }

    @Override
    public EffectType getType() {
        return EffectType.SHOOT;
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("这是一个测试特效");

        return lore;
    }

    @Override
    public Material handleShootEffect() {
        return Material.DIAMOND_BOOTS;
    }
}
