package cn.sakura.thepitcosmetics.command;


import cn.charlotte.pit.util.command.Command;
import cn.charlotte.pit.util.command.param.Parameter;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import org.bukkit.entity.Player;

public class EnableEffect {
    @Command(
            names = {"enableeffecft"},
            permissionNode = "pit.admin"
    )
    public void enableEffect(Player player, @Parameter(name = "BOOLEAN") String Boolean, @Parameter(name = "EffectInternalName") String EffectInternalName) {
        switch (Boolean) {
            case "remove":
                EffectManager.getInstance().removePlayerAllEffect(player);
                break;
            case "kill":
                EffectManager.getInstance().setPlayerKillEffect(player, EffectInternalName);
                break;
            case "death":
                EffectManager.getInstance().setPlayerDeathEffect(player, EffectInternalName);
                break;
            case "shoot":
                EffectManager.getInstance().setPlayerShootEffect(player, EffectInternalName);
                break;
        }
    }
}
