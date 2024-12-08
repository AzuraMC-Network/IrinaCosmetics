package cn.sakura.irinacosmetics.cosmetics.impl.shoot;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
    public EffectType getEffectType() {
        return EffectType.SHOOT;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ARROW);
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

                for (int i = 0; i < 2; i++) {
                    arrow.getWorld().playEffect(arrow.getLocation(), Effect.INSTANT_SPELL, 0);
                }
            }
        }.runTaskTimerAsynchronously(IrinaCosmetics.getInstance(), 0, 1);
    }

    @Override
    public void handleDeath(Player player) {}

    @Override
    public void handleKill(Player player) {}
}
