package cn.sakura.irinacosmetics.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translate(List<String> list) {
        List<String> translated = new ArrayList<>();
        for (String string : list) {
            translated.add(translate(string));
        }

        return translated;
    }

    public static void broadcast(String HasTranslateString) {
        Bukkit.broadcastMessage(translate(HasTranslateString));
    }

    public static void broadcastWithPermission(String HasTranslateString, String Permission) {
        Bukkit.broadcast(translate(HasTranslateString), Permission);
    }
}
