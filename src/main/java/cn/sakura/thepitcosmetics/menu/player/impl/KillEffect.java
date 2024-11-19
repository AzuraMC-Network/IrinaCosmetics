package cn.sakura.thepitcosmetics.menu.player.impl;

import cn.sakura.thepitcosmetics.menu.AbstractMenu;
import org.bukkit.entity.Player;

public class KillEffect extends AbstractMenu {
    @Override
    public String getMenuName() {
        return "击杀";
    }

    @Override
    public int getMenuSize() {
        return 54;
    }

    @Override
    protected void setupItems(Player player) {

    }
}
