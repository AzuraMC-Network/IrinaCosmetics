package cn.sakura.thepitcosmetics;

import cn.charlotte.pit.util.command.CommandHandler;
import cn.charlotte.pit.util.command.util.ClassUtil;
import cn.sakura.thepitcosmetics.cosmetics.AbstractEffect;
import cn.sakura.thepitcosmetics.cosmetics.EffectManager;
import cn.sakura.thepitcosmetics.game.EffectListener;
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
        loadEffectManager();
        loadCommands();
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&3Miral&bElioraen&8] &bPlugin Enabled"));
        Bukkit.getPluginManager().registerEvents(new EffectListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&3Miral&bElioraen&8] &cPlugin Disabled"));
    }

    private void loadEffectManager() {
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

    private void loadCommands() {
        CommandHandler.loadCommandsFromPackage(this, "cn.sakura.thepitcosmetics.command");
    }
}
