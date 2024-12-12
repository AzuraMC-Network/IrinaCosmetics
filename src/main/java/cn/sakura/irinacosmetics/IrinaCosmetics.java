package cn.sakura.irinacosmetics;

import cn.sakura.irinacosmetics.command.MainCommand;
import cn.sakura.irinacosmetics.database.IDatabase;
import cn.sakura.irinacosmetics.database.Mongo;
import cn.sakura.irinacosmetics.game.PlayerListener;
import cn.sakura.irinacosmetics.util.ClassUtil;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.game.Register;
import cn.sakura.irinacosmetics.util.CC;
import lombok.Getter;
import lombok.Setter;
import me.yic.xconomy.api.XConomyAPI;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.stream.Collectors;

public final class IrinaCosmetics extends JavaPlugin implements Listener {
    public XConomyAPI xConomyAPI;
    public PlayerPoints playerPoints;
    public final String BalanceType = this.getConfig().getString("BalanceType");
    public static final String irina = "&8[&bI&fRINA&8] &f| ";
    @Getter
    public IDatabase mongoDataBase;

    @Getter @Setter
    public static IrinaCosmetics instance;
    public static Plugin plugin;

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;

        plugin.saveDefaultConfig();

        getCommand("irina").setExecutor(new MainCommand());

        Bukkit.getScheduler().runTaskLater(this, () -> {
            Plugin xConomyPlugin = Bukkit.getPluginManager().getPlugin("XConomy");
            Plugin thePitPremiumPlugin = Bukkit.getPluginManager().getPlugin("ThePitPremium");
            Plugin playerPointsPlugin = Bukkit.getPluginManager().getPlugin("PlayerPoints");

            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&a欢迎使用, 主人"));

            if (BalanceType != null) {
                Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&a当前经济方案: &f" + BalanceType.toUpperCase()));
                switch (BalanceType.toLowerCase()) {
                    case "playerpoints":
                        if (playerPointsPlugin != null) {
                            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&aPlayerPoints 已加载!"));
                            playerPoints = new PlayerPoints();
                        } else {
                            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&cPlayerPoints 未加载!"));
                            Bukkit.getPluginManager().disablePlugin(this);
                            return;
                        }
                    case "xconomy":
                        if (xConomyPlugin != null) {
                            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&aXConomy 已加载!"));
                            xConomyAPI = new XConomyAPI();
                        } else {
                            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&cXConomy 未加载!"));
                            Bukkit.getPluginManager().disablePlugin(this);
                            return;
                        }
                        break;
                    case "thepitpremium":
                        if (thePitPremiumPlugin != null) {
                            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&aThePitPremium 已加载!"));
                        } else {
                            Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&cThePitPremium 未加载!"));
                            Bukkit.getPluginManager().disablePlugin(this);
                            return;
                        }
                        break;
                    default:
                        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&f笨蛋主人, 你认真的吗? 你确定是这个未知方案吗?"));
                        Bukkit.getPluginManager().disablePlugin(this);
                        return;
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&c笨蛋主人! 你忘了写 BalanceType 啦!"));
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }

            loadEffectManager();
            loadListener();
            mongoDataBase = new Mongo();
            mongoDataBase.setUp();
            Bukkit.getPluginManager().registerEvents(new PlayerListener(mongoDataBase), this);
        }, 21L);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(CC.translate(irina + "&c期待与你下次再见, 主人"));
        if (mongoDataBase != null) mongoDataBase.close();
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
}
