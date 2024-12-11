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
    void removePlayerData(Player player);
    PlayerData getData(Player player);
    boolean isExistPlayerProfile(String collectionName, UUID uuid);
}
