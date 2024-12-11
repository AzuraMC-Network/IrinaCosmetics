package cn.sakura.irinacosmetics.game;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.data.PlayerData;
import cn.sakura.irinacosmetics.database.IDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
    private final IDatabase database;
    private static final Plugin plugin = IrinaCosmetics.plugin;

    public PlayerListener(IDatabase database) {
        this.database = database;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (database != null) {
            if (!database.isExistPlayerData("player", player.getUniqueId())) {
                database.createPlayerData(player);
            } else {
                database.loadPlayerData(player);
            }
        } else {
            plugin.getLogger().warning("THIS FUCK DATA BASE IS NULL");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(IrinaCosmetics.getInstance(), () -> {
            database.savePlayerData(player);
            PlayerData.getData().remove(player.getUniqueId());
        });
    }
}
