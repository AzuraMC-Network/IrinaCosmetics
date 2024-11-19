package cn.sakura.thepitcosmetics.game;

import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EffectListener implements Listener {
    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) e.getEntity().getShooter();
        AbstractEffect effect = EffectManager.getInstance().getPlayerShootEffect(player);

        if (effect != null) {
            if (e.getEntity() instanceof Arrow) {
                effect.handleShoot(player, (Arrow) e.getEntity());
            }
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player target = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        AbstractEffect effect = EffectManager.getInstance().getPlayerKillEffect(killer);

        if (effect != null) {
            effect.handleKill(target);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player myself = e.getEntity();

        AbstractEffect effect = EffectManager.getInstance().getPlayerDeathEffect(myself);
        if (effect != null) {
            effect.handleDeath(myself);
        }
    }
}
