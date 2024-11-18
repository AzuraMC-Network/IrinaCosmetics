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

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

public final class ThePitCosmetics extends JavaPlugin implements Listener {
    @Getter
    public static ThePitCosmetics instance;

    @Override
    public void onEnable() {
        instance = this;
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
        EffectManager effectManager = EffectManager.getInstance();

        Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&3Miral&bElioraen&8] &f正在扫描特效类..."));

        try {
            // 扫描指定包中的所有类
            Collection<Class<?>> classes = ClassUtil.getClassesInPackage(this, "cn.sakura.thepitcosmetics.cosmetics.impl");

            // 过滤出继承 AbstractEffect 的类
            Collection<Class<? extends AbstractEffect>> filteredClasses = classes.stream()
                    .filter(AbstractEffect.class::isAssignableFrom)
                    .filter(clazz -> !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) // 忽略抽象类和接口
                    .map(clazz -> (Class<? extends AbstractEffect>) clazz)
                    .collect(Collectors.toList());

            // 初始化特效管理器
            effectManager.init(filteredClasses);

        } catch (Exception e) {
            // 捕获加载过程中可能的异常
            Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&3Miral&bElioraen&8] &c加载时发生错误!"));
            e.printStackTrace();
        }
    }


    private void loadCommands() {
        CommandHandler.loadCommandsFromPackage(this, "cn.sakura.thepitcosmetics.command");
    }
}
