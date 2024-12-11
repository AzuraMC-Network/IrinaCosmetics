package cn.sakura.irinacosmetics.game;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.database.IDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

@Register
public class PlayerListener implements Listener {
    private final IDatabase database = IrinaCosmetics.getMongoDataBase();
    private static final Plugin plugin = IrinaCosmetics.plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!database.isExistPlayerProfile("player", player.getUniqueId())) {
            database.createPlayerData(player);
        } else {
            database.loadPlayerData(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(IrinaCosmetics.getInstance(), () -> {
            database.savePlayerData(player);
            database.removePlayerData(player);
        });
    }
}
