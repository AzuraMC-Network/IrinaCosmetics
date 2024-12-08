package cn.sakura.irinacosmetics.command;

import cn.charlotte.pit.util.command.Command;
import cn.sakura.irinacosmetics.menu.AbstractMenu;
import cn.sakura.irinacosmetics.menu.player.EffectTypeSelect;
import org.bukkit.entity.Player;

public class OpenMenu {
    @Command(
            names = {"openeffectmenu"}
    )
    public void openEffectMenu(Player player) {
        AbstractMenu menu = new EffectTypeSelect();
        menu.open(player);
    }
}
