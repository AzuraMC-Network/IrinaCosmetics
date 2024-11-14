package cn.sakura.thepitcosmetics.game;

import cn.sakura.thepitcosmetics.util.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EffectListener implements Listener {
    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();
            Player shooter = (Player) arrow.getShooter();

            Effect user = Effect.getUser(shooter.getUniqueId());
            if (user != null && user.getShootEffect() != null) user.getShootEffect().handleShoot(shooter, arrow);
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player target = e.getEntity();
        Player killer = e.getEntity().getKiller();

        Effect user = Effect.getUser(killer.getUniqueId());
        if (user != null && user.getKillEffect() != null) user.getKillEffect().handleKill(target);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player myself = e.getEntity();

        Effect user = Effect.getUser(myself.getUniqueId());
        if (user != null && user.getDeathEffect() != null) user.getDeathEffect().handleDeath(myself);
    }
}
