package cn.sakura.irinacosmetics.command;


import cn.charlotte.pit.util.command.Command;
import cn.charlotte.pit.util.command.param.Parameter;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import org.bukkit.entity.Player;

public class EnableEffect {
    @Command(
            names = {"enableeffecft"},
            permissionNode = "pit.admin"
    )
    public void enableEffect(Player player, @Parameter(name = "BOOLEAN") String Boolean, @Parameter(name = "EffectInternalName") String EffectInternalName) {
        EffectType EFFECT_TYPE = null;
        switch (Boolean) {
            case "remove":
                EffectManager.getInstance().removePlayerAllEffect(player);
                break;
            case "kill":
                EFFECT_TYPE = EffectType.KILL;
                break;
            case "death":
                EFFECT_TYPE = EffectType.DEATH;
                break;
            case "shoot":
                EFFECT_TYPE = EffectType.SHOOT;
                break;
        }
        EffectManager.getInstance().setPlayerEffect(player, EffectInternalName, EFFECT_TYPE);
    }
}
