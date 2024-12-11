package cn.sakura.irinacosmetics.data;

import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class PlayerData {
    @Getter
    private static final Map<UUID, PlayerData> data = new HashMap<>();

    private String playerLowName;
    private String playerName;
    private Player player;
    private UUID uuid;
    private AbstractEffect killEffect;
    private AbstractEffect deathEffect;
    private AbstractEffect shootEffect;
    private List<AbstractEffect> unlockedEffects;

    /**
     * 构造函数，初始化玩家数据。
     *
     * @param player 玩家实例
     */
    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerLowName = playerName.toLowerCase();
        this.killEffect = EffectManager.getInstance().getPlayerKillEffect(player);
        this.deathEffect = EffectManager.getInstance().getPlayerDeathEffect(player);
        this.shootEffect = EffectManager.getInstance().getPlayerShootEffect(player);
        this.unlockedEffects = EffectManager.getInstance().getPlayerUnlockedCosmetics(player);
    }
}
