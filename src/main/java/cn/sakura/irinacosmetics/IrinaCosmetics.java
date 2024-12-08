package cn.sakura.irinacosmetics;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.command.CommandHandler;
import cn.sakura.irinacosmetics.util.ClassUtil;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.game.Register;
import cn.sakura.irinacosmetics.util.CC;
import lombok.Getter;
import me.yic.xconomy.api.XConomyAPI;
import me.yic.xconomy.data.syncdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public final class IrinaCosmetics extends JavaPlugin implements Listener {
    private static final Plugin xConomyPlugin = Bukkit.getPluginManager().getPlugin("XConomy");
    private static final Plugin thePitPremiumPlugin = Bukkit.getPluginManager().getPlugin("ThePitPremium");
    private XConomyAPI xConomyAPI;
    private final String BalanceType = this.getConfig().getString("BalanceType");
    public static final String irina = "&8[&bI&fRINA&8] &f| ";
    @Getter
    public static IrinaCosmetics instance;
    public static Plugin plugin;

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;

        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&a欢迎使用, 主人"));

        plugin.saveDefaultConfig();

        if (BalanceType != null) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&a当前方案: " + BalanceType.toUpperCase()));
            switch (BalanceType.toLowerCase()) {
                case "xconomy":
                    if (xConomyPlugin != null) {
                        xConomyAPI = new XConomyAPI();
                    } else {
                        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&cXConomy 未加载!"));
                        Bukkit.getPluginManager().disablePlugin(this);
                        return;
                    }
                case "thepitpremium":
                    if (thePitPremiumPlugin == null) {
                        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&cThePitPremium 未加载!"));
                        Bukkit.getPluginManager().disablePlugin(this);
                        return;
                    }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&c笨蛋主人! 你忘了写 BalanceType 啦!"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        loadEffectManager();
        loadCommands();
        loadListener();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&c期待与你下次再见, 主人"));
    }

    private void loadEffectManager() {
        EffectManager effectManager = EffectManager.getInstance();

        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&f正在扫描特效类..."));

        try {
            // 扫描指定包中的所有类
            Collection<Class<?>> classes = ClassUtil.getClassesInPackage(this, "cn.sakura.irinacosmetics.cosmetics.impl");

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
            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&c加载时发生错误!"));
            e.printStackTrace();
        }
    }

    private void loadCommands() {
        CommandHandler.loadCommandsFromPackage(this, "cn.sakura.irinacosmetics.command");
    }

    private void loadListener() {
        Collection<Class<?>> classes = ClassUtil.getClassesInPackage(this, "cn.sakura.irinacosmetics");

        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(Register.class) && Listener.class.isAssignableFrom(aClass)) {
                try {
                    Bukkit.getPluginManager().registerEvents((Listener) aClass.newInstance(), IrinaCosmetics.getInstance());
                } catch (Exception ignored) {
                }
            }
        }
    }

    public int getBalance(UUID playerUUID) {
        switch (BalanceType.toLowerCase()) {
            case "thepitpremium":
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(playerUUID);

                return (int) profile.getCoins();
            case "xconomy":
                PlayerData playerData = xConomyAPI.getPlayerData(playerUUID);

                return playerData != null ? playerData.getBalance().intValue() : 0;
            default:
                return 0;
        }
    }
}
