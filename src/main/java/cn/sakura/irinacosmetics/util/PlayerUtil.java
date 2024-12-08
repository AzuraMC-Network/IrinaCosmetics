package cn.sakura.irinacosmetics.util;

import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityWeather;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtil {
    public static void playThunderEffect(Location thunderLocation) {
        EntityLightning lightning = new EntityLightning(((CraftWorld)thunderLocation.getWorld()).getHandle(), thunderLocation.getX(), thunderLocation.getY(), thunderLocation.getZ(), true, true);
        PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather(lightning);

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
