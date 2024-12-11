package cn.sakura.irinacosmetics.game;

import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

@Register
public class EffectListener implements Listener {
    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) e.getEntity().getShooter();
        AbstractEffect effect = EffectManager.getInstance().getPlayerEffect(player, EffectType.SHOOT);

        if (effect != null && e.getEntity() instanceof Arrow) effect.handleShoot(player, (Arrow) e.getEntity());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player target = e.getEntity();
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        AbstractEffect effect = EffectManager.getInstance().getPlayerEffect(killer, EffectType.KILL);

        if (effect != null) effect.handleKill(target);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player myself = e.getEntity();

        AbstractEffect effect = EffectManager.getInstance().getPlayerEffect(myself, EffectType.DEATH);
        if (effect != null) effect.handleDeath(myself);
    }
}
