package cn.sakura.irinacosmetics.data;

import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {
    /**
     * 存储每个玩家的档案数据，以 UUID 作为键。
     */
    @Getter
    private static final HashMap<UUID, PlayerData> data = new HashMap<>();
    private String playerLowName;
    private String playerName;
    private Player player;
    private UUID uuid;
    private AbstractEffect killEffect;
    private AbstractEffect deathEffect;
    private AbstractEffect shootEffect;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerLowName = playerName.toLowerCase();
        this.killEffect = EffectManager.getInstance().getPlayerKillEffect(player);
        this.deathEffect = EffectManager.getInstance().getPlayerDeathEffect(player);
        this.shootEffect = EffectManager.getInstance().getPlayerShootEffect(player);
    }

    public static PlayerData getPlayerData(Player player) {
        return data.get(player.getUniqueId());
    }

    public List<AbstractEffect> getUnlockedCosmetics() {
        UUID uuid = this.uuid;
        return EffectManager.playerUnlockedEffects.get(uuid);
    }
}
