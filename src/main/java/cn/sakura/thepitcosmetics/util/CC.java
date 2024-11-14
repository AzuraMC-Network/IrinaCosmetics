package cn.sakura.thepitcosmetics.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CC {
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void broadcast(String HasTranslateString) {
        Bukkit.broadcastMessage(translate(HasTranslateString));
    }

    public static void broadcastWithPermission(String HasTranslateString, String Permission) {
        Bukkit.broadcast(translate(HasTranslateString), Permission);
    }
}
