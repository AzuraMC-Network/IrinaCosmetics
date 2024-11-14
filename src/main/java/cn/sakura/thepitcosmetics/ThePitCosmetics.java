package cn.sakura.thepitcosmetics;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.util.command.util.ClassUtil;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import cn.sakura.thepitcosmetics.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.stream.Collectors;

public final class ThePitCosmetics extends JavaPlugin implements Listener {
    @Getter
    public static ThePitCosmetics instance;

    @Override
    public void onEnable() {
        loadEnchantmentManager();
        Bukkit.getConsoleSender().sendMessage(CC.translate("&bPlugin Enabled"));
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&bPlugin Disabled"));
    }

    private void loadEnchantmentManager() {
        EffectManager effectManager = new EffectManager();

        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(this, "cn.sakura.thepitcosmetics.cosmetics.impl");

        Collection<Class<? extends AbstractEffect>> filteredClasses = classes.stream()
                .filter(AbstractEffect.class::isAssignableFrom)
                .map(clazz -> (Class<? extends AbstractEffect>) clazz)
                .collect(Collectors.toList());

        effectManager.init(filteredClasses);

        if (!effectManager.getEffects().isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§8[§fGAME§bBYTE §aThePitAddon§8] §a附魔成功加载");
        } else {
            Bukkit.getConsoleSender().sendMessage("§8[§fGAME§bBYTE §aThePitAddon§8] §c未成功加载任何附魔");
        }
    }
}
