package cn.sakura.irinacosmetics.data;

import cn.charlotte.pit.data.PlayerProfile;
import cn.sakura.irinacosmetics.IrinaCosmetics;
import me.yic.xconomy.api.XConomyAPI;
import me.yic.xconomy.data.syncdata.PlayerData;
import org.black_ixx.playerpoints.PlayerPointsAPI;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceManager {
    private static final String BALANCE_TYPE = IrinaCosmetics.getInstance().BalanceType.toLowerCase();
    private static final PlayerPointsAPI PLAYER_POINTS_API = IrinaCosmetics.getInstance().playerPoints.getAPI();
    private static final XConomyAPI X_CONOMY_API = IrinaCosmetics.getInstance().xConomyAPI;

    public static int getBalance(UUID playerUUID) {
        switch (BALANCE_TYPE) {
            case "playerpoints":
                return getPlayerPointsApi().look(playerUUID);
            case "thepitpremium":
                PlayerProfile profile = getPlayerProfile(playerUUID);
                return profile != null ? (int) profile.getCoins() : 0;
            case "xconomy":
                PlayerData playerData = getPlayerData(playerUUID);
                return playerData != null ? playerData.getBalance().intValue() : 0;
            default:
                return 0;
        }
    }

    public static void setBalance(UUID playerUUID, int value) {
        switch (BALANCE_TYPE) {
            case "playerpoints":
                getPlayerPointsApi().set(playerUUID, value);
                break;
            case "thepitpremium":
                PlayerProfile profile = getPlayerProfile(playerUUID);
                if (profile != null) profile.setCoins(value);
                break;
            case "xconomy":
                PlayerData playerData = getPlayerData(playerUUID);
                if (playerData != null) playerData.setBalance(BigDecimal.valueOf(value));
                break;
            default:
                break;
        }
    }

    public static void addBalance(UUID playerUUID, int value) {
        switch (BALANCE_TYPE) {
            case "playerpoints":
                getPlayerPointsApi().set(playerUUID, getBalance(playerUUID) + value);
            case "thepitpremium":
                PlayerProfile profile = getPlayerProfile(playerUUID);
                if (profile != null) profile.setCoins(profile.getCoins() + value);
                break;
            case "xconomy":
                PlayerData playerData = getPlayerData(playerUUID);
                if (playerData != null) playerData.setBalance(BigDecimal.valueOf(playerData.getBalance().intValue() + value));
                break;
            default:
                break;
        }
    }

    public static void takeBalance(UUID playerUUID, int value) {
        switch (BALANCE_TYPE) {
            case "playerpoints":
                getPlayerPointsApi().set(playerUUID, getBalance(playerUUID) - value);
            case "thepitpremium":
                PlayerProfile profile = getPlayerProfile(playerUUID);
                if (profile != null) profile.setCoins(profile.getCoins() - value);
                break;
            case "xconomy":
                PlayerData playerData = getPlayerData(playerUUID);
                if (playerData != null) playerData.setBalance(BigDecimal.valueOf(playerData.getBalance().intValue() - value));
                break;
            default:
                break;
        }
    }

    private static PlayerProfile getPlayerProfile(UUID playerUUID) {
        return PlayerProfile.getPlayerProfileByUuid(playerUUID);
    }

    private static PlayerData getPlayerData(UUID playerUUID) {
        return X_CONOMY_API.getPlayerData(playerUUID);
    }

    private static PlayerPointsAPI getPlayerPointsApi() {
        return PLAYER_POINTS_API;
    }
}
