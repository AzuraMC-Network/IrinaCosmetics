package cn.sakura.thepitcosmetics.cosmetics;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Getter
public class EffectManager {
    private final List<AbstractEffect> effects = new ArrayList<>();
    private final HashMap<String, AbstractEffect> effectMap = new HashMap<>();

    public void init(Collection<Class<? extends AbstractEffect>> classes) {
        Bukkit.getLogger().info("§8[§fGAME§bBYTE §aThePitAddon§8] §e正在加载附魔...");

        for (Class<? extends AbstractEffect> aClass : classes) {
            if (AbstractEffect.class.isAssignableFrom(aClass)) {
                try {
                    AbstractEffect effect = aClass.newInstance();
                    this.effects.add(effect);
                    this.effectMap.put(effect.getEffectInternalName(), effect);

                } catch (Exception var5) {
                    var5.printStackTrace();
                    Bukkit.getLogger().warning(var5 + "§8[§fGAME§bBYTE §aThePitAddon§8] §c出现异常,错误已抛出");
                }
            }
        }

        Bukkit.getLogger().info( "§8[§fGAME§bBYTE §aThePitAddon§8] §a成功加载附魔" + this.effects.size() + "个!");
    }
}
