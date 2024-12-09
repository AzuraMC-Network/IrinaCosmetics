package cn.sakura.irinacosmetics.database;

import org.bukkit.entity.Player;

public interface IDatabase {
    void setUp();
    void close();
    void createPlayerData(Player player);
    void loadPlayerData(Player player);
    void savePlayerData(Player player);
    void getPlayerData(Player player);
}
