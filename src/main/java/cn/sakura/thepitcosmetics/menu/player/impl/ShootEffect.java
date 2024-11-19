package cn.sakura.thepitcosmetics.menu.player.impl;

import cn.sakura.thepitcosmetics.menu.AbstractMenu;
import org.bukkit.entity.Player;

public class ShootEffect extends AbstractMenu {
    @Override
    public String getMenuName() {
        return "弹射物轨迹";
    }

    @Override
    public int getMenuSize() {
        return 54;
    }

    @Override
    protected void setupItems(Player player) {

    }
}
