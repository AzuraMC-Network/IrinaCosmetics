package cn.sakura.thepitcosmetics.command;

import cn.charlotte.pit.util.command.Command;
import cn.sakura.thepitcosmetics.util.CC;
import cn.sakura.thepitcosmetics.util.Effect;
import org.bukkit.entity.Player;

public class EnableEffect {
    @Command(
            names = {"enablekilleffect"},
            permissionNode = "pit.admin"
    )
    public void killEffect(Player player) {
        player.sendMessage(CC.translate("TURN ON KILL EFFECT"));
        Effect user = Effect.getUser(player.getUniqueId());
        user.setKillEffect("testKillEffect");
    }
    @Command(
            names = {"enabledeatheffect"},
            permissionNode = "pit.admin"
    )
    public void deathEffect(Player player) {
        player.sendMessage(CC.translate("TURN ON DEATH EFFECT"));
        Effect user = Effect.getUser(player.getUniqueId());
        user.setDeathEffect("testDeathEffect");
    }
    @Command(
            names = {"enableshooteffect"},
            permissionNode = "pit.admin"
    )
    public void shootEffect(Player player) {
        player.sendMessage(CC.translate("TURN ON SHOOT EFFECT"));
        Effect user = Effect.getUser(player.getUniqueId());
        user.setShootEffect("testShootEffect");
    }

    @Command(
            names = {"disablekilleffect"},
            permissionNode = "pit.admin"
    )
    public void disableKillEffect(Player player) {
        player.sendMessage(CC.translate("TURN OFF KILL EFFECT"));
        Effect user = Effect.getUser(player.getUniqueId());
        user.resetKillEffect();
    }
    @Command(
            names = {"disabledeatheffect"},
            permissionNode = "pit.admin"
    )
    public void disableDeathEffect(Player player) {
        player.sendMessage(CC.translate("TURN OFF DEATH EFFECT"));
        Effect user = Effect.getUser(player.getUniqueId());
        user.resetDeathEffect();
    }
    @Command(
            names = {"disableshooteffect"},
            permissionNode = "pit.admin"
    )
    public void disableShootEffect(Player player) {
        player.sendMessage(CC.translate("TURN OF SHOOT EFFECT"));
        Effect user = Effect.getUser(player.getUniqueId());
        user.resetShootEffect();
    }

}
