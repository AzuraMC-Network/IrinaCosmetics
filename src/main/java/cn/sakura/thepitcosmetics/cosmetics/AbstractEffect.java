package cn.sakura.thepitcosmetics.cosmetics;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AbstractEffect {

    public abstract String getDisplayName();

    public abstract String getEffectInternalName();

    public abstract ItemStack getIcon();

    public abstract List<String> getLore();

    public abstract void handleShoot(Player shooter, Arrow arrow); //飞行物轨迹

    public abstract void handleDeath(Player myself); //亡语

    public abstract void handleKill(Player target); //击杀
}
