package cn.sakura.thepitcosmetics.cosmetics;

import java.util.List;

public abstract class AbstractEffects {

    public abstract String getEffectName();

    public abstract String getEffectInternalName();

    public abstract EffectType getType();

    public abstract List<String> getLore();
}
