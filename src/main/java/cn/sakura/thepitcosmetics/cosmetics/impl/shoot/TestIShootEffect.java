package cn.sakura.thepitcosmetics.cosmetics.impl.shoot;

import cn.sakura.thepitcosmetics.ThePitCosmetics;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TestIShootEffect extends AbstractEffect {

    @Override
    public String getDisplayName() {
        return "TestEffect";
    }

    @Override
    public String getEffectInternalName() {
        return "testShootEffect";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add("这是一个测试特效");

        return lore;
    }

    @Override
    public void handleShoot(Player player, Arrow arrow) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround()) {
                    this.cancel();
                    return;
                }

                Location arrowLoc2 = arrow.getLocation();

                for (int i = 0; i < 2; i++) {
                    arrow.getWorld().playEffect(arrowLoc2, Effect.INSTANT_SPELL, 0);
                }
            }
        }.runTaskTimerAsynchronously(ThePitCosmetics.getInstance(), 0, 1);
    }

    @Override
    public void handleDeath(Player player) {}

    @Override
    public void handleKill(Player player) {}
}
