package cn.sakura.thepitcosmetics.util;

import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Effect {
    private final static Map<UUID, Effect> users = new HashMap<>();

    @Getter
    private UUID uuid;
    @Getter
    private AbstractEffect shootEffect;
    @Getter
    private AbstractEffect deathEffect;
    @Getter
    private AbstractEffect killEffect;

    public Effect(UUID uuid) {
        this.uuid = uuid;
    }

    public static Effect getUser(UUID uuid) {
        return users.computeIfAbsent(uuid, Effect::new);
    }

    public void setKillEffect(String effectIternalName) {
        this.killEffect = getEffect(effectIternalName);
    }
    public void setShootEffect(String effectIternalName) {
        this.shootEffect = getEffect(effectIternalName);
    }
    public void setDeathEffect(String effectIternalName) {
        this.deathEffect = getEffect(effectIternalName);
    }

    public AbstractEffect getEffect(String effect) {
        return new EffectManager().getEffectMap().get(effect);
    }

    public void resetShootEffect() {
        this.shootEffect = null;
    }

    public void resetDeathEffect() {
        this.deathEffect = null;
    }

    public void resetKillEffect() {
        this.killEffect = null;
    }
}
