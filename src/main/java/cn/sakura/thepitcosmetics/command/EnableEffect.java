package cn.sakura.thepitcosmetics.command;

import cn.charlotte.pit.util.command.Command;
import cn.charlotte.pit.util.command.param.Parameter;
import cn.sakura.thepitcosmetics.util.CC;
import cn.sakura.thepitcosmetics.util.Effect;
import org.bukkit.entity.Player;

public class EnableEffect {
    @Command(
            names = {"enableeffect"},
            permissionNode = "pit.admin"
    )
    public void enableEffect(Player player, @Parameter(name = "effect") String effect) {
        Effect user = Effect.getUser(player.getUniqueId());
        switch (effect) {
            case "kill":
                user.setKillEffect("testKillEffect");
                break;
            case "death":
                user.setDeathEffect("testDeathEffect");
                break;
            case "shoot":
                user.setShootEffect("testShootEffect");
        }
        player.sendMessage(CC.translate("SUCCESS " + user.getShootEffect() + " | " + user.getKillEffect() + " | " + user.getDeathEffect()));
    }

    @Command(
            names = {"disableeffect"},
            permissionNode = "pit.admin"
    )
    public void disableEffect(Player player, @Parameter(name = "effect") String effect) {
        Effect user = Effect.getUser(player.getUniqueId());
        switch (effect) {
            case "kill":
                user.resetKillEffect();
                break;
            case "death":
                user.resetDeathEffect();
                break;
            case "shoot":
                user.resetShootEffect();
        }
    }
}
