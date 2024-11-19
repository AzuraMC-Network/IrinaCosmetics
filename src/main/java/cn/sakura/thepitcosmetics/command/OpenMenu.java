package cn.sakura.thepitcosmetics.command;

import cn.charlotte.pit.util.command.Command;
import cn.sakura.thepitcosmetics.menu.AbstractMenu;
import cn.sakura.thepitcosmetics.menu.player.EffectTypeSelect;
import org.bukkit.entity.Player;

public class OpenMenu {
    @Command(
            names = {"openeffectmenu"},
            permissionNode = "pit.admin"
    )
    public void openEffectMenu(Player player) {
        AbstractMenu menu = new EffectTypeSelect();
        menu.open(player);
    }
}
