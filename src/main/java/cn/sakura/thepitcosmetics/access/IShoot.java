package cn.sakura.thepitcosmetics.access;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public interface IShoot {
    void handleShoot(Player player, Arrow arrow);
}
