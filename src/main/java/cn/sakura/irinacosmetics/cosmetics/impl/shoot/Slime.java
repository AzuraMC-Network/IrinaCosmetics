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

public class Slime extends AbstractEffect {
    @Override
    public String getDisplayName() {
        return "史莱姆";
    }

    @Override
    public String getEffectInternalName() {
        return "slime";
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.SHOOT;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SLIME_BALL);
    }

    @Override
    public void handleShoot(Player shooter, Arrow arrow) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround()) {
                    this.cancel();
                    return;
                }

                arrow.getWorld().playEffect(arrow.getLocation(), Effect.SLIME, 0);
            }
        }.runTaskTimerAsynchronously(IrinaCosmetics.getInstance(), 0, 1);
    }

    @Override
    public void handleDeath(Player myself) {

    }

    @Override
    public void handleKill(Player target) {

    }
}
