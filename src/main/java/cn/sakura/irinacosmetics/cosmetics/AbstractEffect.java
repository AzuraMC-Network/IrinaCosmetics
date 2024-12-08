package cn.sakura.irinacosmetics.cosmetics;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractEffect {

    public abstract String getDisplayName();

    public abstract String getEffectInternalName();

    public abstract EffectType getEffectType();

    public abstract ItemStack getIcon();

    public abstract void handleShoot(Player shooter, Arrow arrow); //飞行物轨迹

    public abstract void handleDeath(Player myself); //亡语

    public abstract void handleKill(Player target); //击杀
}
