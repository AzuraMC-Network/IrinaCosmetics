package cn.sakura.thepitcosmetics.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translate(List<String> list) {
        List<String> toReturn = new ArrayList<>();
        for (String line : list) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return toReturn;
    }

    public static void broadcast(String HasTranslateString) {
        Bukkit.broadcastMessage(translate(HasTranslateString));
    }

    public static void broadcastWithPermission(String HasTranslateString, String Permission) {
        Bukkit.broadcast(translate(HasTranslateString), Permission);
    }
}
