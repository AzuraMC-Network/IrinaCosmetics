package cn.sakura.irinacosmetics.data;

import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {
    private String playerLowName;
    private String playerName;
    private Player player;
    private UUID uuid;

    public PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerLowName = playerName.toLowerCase();
    }

    public void loadPlayerData(UUID playerUUID) {}

    public void savePlayerData(UUID playerUUID) {}

    public List<AbstractEffect> getUnlockedCosmetics() {
        UUID uuid = this.uuid;
        return EffectManager.playerUnlockedEffects.get(uuid);
    }
}
