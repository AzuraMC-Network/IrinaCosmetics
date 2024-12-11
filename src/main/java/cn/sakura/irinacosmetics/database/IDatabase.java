package cn.sakura.irinacosmetics.database;

import cn.sakura.irinacosmetics.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IDatabase {
    void setUp();
    void close();
    void createPlayerData(Player player);
    void loadPlayerData(Player player);
    void savePlayerData(Player player);
    boolean isExistPlayerData(String collectionName, UUID uuid);
}
