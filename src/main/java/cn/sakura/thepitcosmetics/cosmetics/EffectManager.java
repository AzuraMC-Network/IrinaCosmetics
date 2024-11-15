package cn.sakura.thepitcosmetics.cosmetics;

import cn.sakura.thepitcosmetics.util.CC;
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
        Bukkit.getLogger().info(CC.translate("&8[&3Miral&bElioraen&8] &e正在加载特效"));

        for (Class<? extends AbstractEffect> aClass : classes) {
            if (AbstractEffect.class.isAssignableFrom(aClass)) {
                try {
                    AbstractEffect effect = aClass.newInstance();
                    this.effects.add(effect);
                    this.effectMap.put(effect.getEffectInternalName(), effect);

                } catch (Exception var5) {
                    var5.printStackTrace();
                    Bukkit.getLogger().warning(var5 + CC.translate("&8[&3Miral&bElioraen&8] &a出现错误"));
                }
            }
        }

        Bukkit.getLogger().info( CC.translate("&8[&3Miral&bElioraen&8] &a已加载 &e" + this.effects.size() + " &a个特效!"));

    }
}
