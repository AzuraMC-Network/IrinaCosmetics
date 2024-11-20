package cn.sakura.thepitcosmetics.cosmetics.impl.shoot;

import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.sakura.thepitcosmetics.ThePitCosmetics;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectType;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Firework extends AbstractEffect {
    @Override
    public String getDisplayName() {
        return "火箭";
    }

    @Override
    public String getEffectInternalName() {
        return "firework";
    }

    @Override
    public EffectType getEffectType() {
        return EffectType.SHOOT;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.FIREWORK);
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

                arrow.getWorld().playEffect(arrow.getLocation(), Effect.LAVA_POP, 0);
                arrow.getWorld().playEffect(arrow.getLocation(), Effect.SMOKE, 0);
            }
        }.runTaskTimerAsynchronously(ThePitCosmetics.getInstance(), 0, 1);
    }

    @Override
    public void handleDeath(Player myself) {

    }

    @Override
    public void handleKill(Player target) {

    }
}
