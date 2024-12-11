package cn.sakura.irinacosmetics.data;

import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class PlayerData {
    @Getter
    public static final Map<UUID, PlayerData> data = new HashMap<>();

    private EffectManager effectManager = EffectManager.getInstance();

    private String playerLowName;
    private String playerName;
    private Player player;
    private UUID uuid;
    private AbstractEffect killEffect;
    private AbstractEffect deathEffect;
    private AbstractEffect shootEffect;
    private List<String> unlockedEffects;

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
        this.killEffect = effectManager.getPlayerEffect(player, EffectType.KILL) != null ? effectManager.getPlayerEffect(player, EffectType.KILL) : null;
        this.deathEffect = effectManager.getPlayerEffect(player, EffectType.DEATH) != null ? effectManager.getPlayerEffect(player, EffectType.DEATH) : null;
        this.shootEffect = effectManager.getPlayerEffect(player, EffectType.SHOOT) != null ? effectManager.getPlayerEffect(player, EffectType.SHOOT) : null;
        this.unlockedEffects = effectManager.getPlayerUnlockedEffects(player);
    }

    public static PlayerData getPlayerData(Player player) {
        return data.getOrDefault(player.getUniqueId(), new PlayerData(player));
    }
}
