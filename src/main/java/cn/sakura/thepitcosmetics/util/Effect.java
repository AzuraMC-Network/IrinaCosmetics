package cn.sakura.thepitcosmetics.util;

import lombok.Data;

@Data
public class Effect {

    private org.bukkit.Effect effect;

    public Effect(org.bukkit.Effect effect) {
        this.effect = effect;
    }
}
