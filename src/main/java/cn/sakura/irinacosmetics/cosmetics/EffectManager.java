package cn.sakura.irinacosmetics.cosmetics;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class EffectManager {
    @Getter
    private static final EffectManager instance = new EffectManager();

    private final HashMap<UUID, AbstractEffect> playerKillEffects = new HashMap<>();
    private final HashMap<UUID, AbstractEffect> playerDeathEffects = new HashMap<>();
    private final HashMap<UUID, AbstractEffect> playerShootEffects = new HashMap<>();
    public static final List<AbstractEffect> KillEffects = new ArrayList<>();
    public static final List<AbstractEffect> DeathEffects = new ArrayList<>();
    public static final List<AbstractEffect> ShootEffects = new ArrayList<>();
    private final List<AbstractEffect> effects = new ArrayList<>();
    private final HashMap<String, AbstractEffect> effectMap = new HashMap<>();

    private EffectManager() {}

    /**
     * 初始化特效
     * @param classes 要加载的特效类集合
     */
    public void init(Collection<Class<? extends AbstractEffect>> classes) {
        Bukkit.getLogger().info(CC.translate(IrinaCosmetics.irina + "&e正在加载特效"));

        int loadedCount = 0;

        for (Class<? extends AbstractEffect> effectClass : classes) {
            try {
                AbstractEffect effect = effectClass.getDeclaredConstructor().newInstance();
                registerEffect(effect);
                loadedCount++;
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING,
                        CC.translate(IrinaCosmetics.irina + "&c加载特效 " + effectClass.getName() + " 时出现错误:"), e);
            }
        }

        Bukkit.getLogger().info(CC.translate(IrinaCosmetics.irina + "&a已加载 &e" + loadedCount + " &a个特效!"));
    }

    /**
     * 注册特效实例
     * @param effect 特效实例
     */
    public void registerEffect(AbstractEffect effect) {
        if (effectMap.containsKey(effect.getEffectInternalName())) {
            Bukkit.getLogger().warning(CC.translate(IrinaCosmetics.irina + "&&c特效 "
                    + effect.getEffectInternalName() + " | " + effect.getDisplayName() + " 已存在，跳过注册。"));
            return;
        }

        effects.add(effect);

        switch (effect.getEffectType()) {
            case KILL:
                KillEffects.add(effect);
                break;
            case DEATH:
                DeathEffects.add(effect);
                break;
            case SHOOT:
                ShootEffects.add(effect);
                break;
        }

        effectMap.put(effect.getEffectInternalName(), effect);
        Bukkit.getLogger().info(CC.translate(IrinaCosmetics.irina + "&a特效 "
                + effect.getEffectInternalName() + " | " + effect.getDisplayName() + " 注册成功!"));
    }

    /**
     * 按名称获取特效实例
     * @param effectName 特效内部名称
     * @return 特效实例或null
     */
    public AbstractEffect getEffect(String effectName) {
        return effectMap.get(effectName);
    }

    /**
     * 获取所有特效
     * @return 特效列表
     */
    public List<AbstractEffect> getAllEffects() {
        return Collections.unmodifiableList(effects);
    }

    /**
     * 移除特效
     * @param effectInternalName 特效内部名称
     */
    public void removeEffect(String effectInternalName) {
        AbstractEffect effect = effectMap.remove(effectInternalName);
        if (effect != null) {
            effects.remove(effect);
            Bukkit.getLogger().info(CC.translate(IrinaCosmetics.irina + "&a特效 "
                    + effectInternalName + " 已移除!"));
            return;
        }
        Bukkit.getLogger().warning(CC.translate(IrinaCosmetics.irina + "&c尝试移除特效 "
                + effectInternalName + " 但未找到。"));
    }

    /**
     * 重新加载特效
     * @param effectClass 要重新加载的特效类
     */
    public void reloadEffect(Class<? extends AbstractEffect> effectClass) {
        try {
            AbstractEffect newEffect = effectClass.getDeclaredConstructor().newInstance();
            String effectName = newEffect.getEffectInternalName();

            // 先移除旧的特效
            removeEffect(effectName);

            // 注册新的特效
            registerEffect(newEffect);

            Bukkit.getLogger().info(CC.translate(IrinaCosmetics.irina + "&a特效 "
                    + effectName + " 已成功重新加载!"));
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, CC.translate(IrinaCosmetics.irina + "&c重新加载特效 " + effectClass.getName() + " 时出现错误:"), e);
        }
    }

    public AbstractEffect getPlayerKillEffect(Player player) {
        return playerKillEffects.get(player.getUniqueId());
    }

    public AbstractEffect getPlayerDeathEffect(Player player) {
        return playerDeathEffects.get(player.getUniqueId());
    }

    public AbstractEffect getPlayerShootEffect(Player player) {
        return playerShootEffects.get(player.getUniqueId());
    }

    public void setPlayerShootEffect(Player player, String effectName) {
        AbstractEffect effect = getEffect(effectName);
        if (effect != null) {
            playerShootEffects.put(player.getUniqueId(), effect);
            player.sendMessage(CC.translate("&a特效 &f" + effect.getDisplayName() + " &a已启用!"));
        } else {
            player.sendMessage(CC.translate("&c特效 &f" + effectName + " &c不存在!"));
        }
    }

    public void setPlayerKillEffect(Player player, String effectName) {
        AbstractEffect effect = getEffect(effectName);
        if (effect != null) {
            playerKillEffects.put(player.getUniqueId(), effect);
            player.sendMessage(CC.translate("&a特效 &f" + effect.getDisplayName() + " &a已启用!"));
        } else {
            player.sendMessage(CC.translate("&c特效 &f" + effectName + " &c不存在!"));
        }
    }

    public void setPlayerDeathEffect(Player player, String effectName) {
        AbstractEffect effect = getEffect(effectName);
        if (effect != null) {
            playerDeathEffects.put(player.getUniqueId(), effect);
            player.sendMessage(CC.translate("&a特效 &f" + effect.getDisplayName() + " &a已启用!"));
        } else {
            player.sendMessage(CC.translate("&c特效 &f" + effectName + " &c不存在!"));
        }
    }

    public void removePlayerKillEffect(Player player) {
        playerKillEffects.remove(player.getUniqueId());
        player.sendMessage(CC.translate("&c特效已关闭"));
    }

    public void removePlayerDeathEffect(Player player) {
        playerDeathEffects.remove(player.getUniqueId());
        player.sendMessage(CC.translate("&c特效已关闭"));
    }

    public void removePlayerShootEffect(Player player) {
        playerShootEffects.remove(player.getUniqueId());
        player.sendMessage(CC.translate("&c特效已关闭"));
    }

    public void removePlayerAllEffect(Player player) {
        playerShootEffects.remove(player.getUniqueId());
        playerDeathEffects.remove(player.getUniqueId());
        playerKillEffects.remove(player.getUniqueId());
        player.sendMessage(CC.translate("&c特效已全部关闭"));
    }
}
