package cn.sakura.irinacosmetics.database;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
import cn.sakura.irinacosmetics.cosmetics.EffectType;
import cn.sakura.irinacosmetics.data.PlayerData;
import cn.sakura.irinacosmetics.util.CC;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ConnectionPoolSettings;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Mongo implements IDatabase {
    private final String irina = IrinaCosmetics.irina;
    private MongoClient mongoClient;

    @Getter
    @Setter
    private MongoDatabase database;
    @Getter
    private final Mongo instance;

    private final Plugin plugin = IrinaCosmetics.getInstance();

    private final Map<String, Object> config;
    private final EffectManager effectManager = EffectManager.getInstance();

    public Mongo() {
        instance = this;
        config = plugin.getConfig().getConfigurationSection("MongoDataBase").getValues(false);
    }

    @Override
    public void setUp() {
        try {
            // 初始化 MongoDB 客户端
            mongoClient = MongoClients.create(buildClientSettings());

            // 检测数据库连接
            MongoDatabase db = mongoClient.getDatabase((String) config.get("database"));
            db.runCommand(new Document("ping", 1));
            setDatabase(db);

            plugin.getLogger().info("MongoDB 数据库连接成功: " + db.getName());
        } catch (Exception e) {
            plugin.getLogger().severe("MongoDB 初始化失败: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private MongoClientSettings buildClientSettings() {
        String connectionString = (boolean) config.get("enableVerify") ?
                String.format("mongodb://%s:%s@%s:%d/%s",
                        config.get("username"),
                        config.get("password"),
                        config.get("ip"),
                        config.get("port"),
                        config.get("database")) :
                String.format("mongodb://%s:%d/%s",
                        config.get("ip"),
                        config.get("port"),
                        config.get("database"));

        ConnectionPoolSettings poolSettings = ConnectionPoolSettings.builder()
                .maxSize((int) config.getOrDefault("maxConnectionPool", 150))
                .minSize((int) config.getOrDefault("minConnectionPool", 10))
                .build();

        return MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                .applyConnectionString(new com.mongodb.ConnectionString(connectionString))
                .applyToConnectionPoolSettings(builder -> builder.applySettings(poolSettings))
                .build();
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            plugin.getLogger().info("MongoDB 连接已关闭。");
        }
    }

    @Override
    public void createPlayerData(Player player) {
        PlayerData data = PlayerData.getPlayerData(player);
        Document doc = new Document("playerName", data.getPlayerName())
                .append("lowName", data.getPlayerLowName())
                .append("uuid", data.getUuid().toString())
                .append("killEffect", "NULL")
                .append("deathEffect", "NULL")
                .append("shootEffect", "NULL")
                .append("unlockedEffects", new ArrayList<>());

        player.sendMessage(CC.translate(irina + "&7未检查到档案, 开始创建..."));
        createOrUpdateDocument(doc, data.getUuid());

        Document result = findDocumentByUuid(player.getUniqueId());

        data.setUuid(UUID.fromString(result.getString("uuid")));
        data.setPlayerName(result.getString("playerName"));
        data.setPlayerLowName(result.getString("lowName"));
        data.setDeathEffect(effectManager.getEffect(result.getString("deathEffect")));
        data.setKillEffect(effectManager.getEffect(result.getString("killEffect")));
        data.setShootEffect(effectManager.getEffect(result.getString("shootEffect")));
        data.setUnlockedEffects(effectManager.getPlayerUnlockedEffects(player));

        PlayerData.getData().put(data.getUuid(), data);
        player.sendMessage(CC.translate(irina + "&a创建成功!"));
    }

    @Override
    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        Document result = findDocumentByUuid(uuid);

        if (result != null) {
            // 获取效果名称
            String deathEffectName = result.getString("deathEffect");
            String killEffectName = result.getString("killEffect");
            String shootEffectName = result.getString("shootEffect");

            // 设置玩家效果
            if (!deathEffectName.equalsIgnoreCase("null")) effectManager.setPlayerEffect(player, deathEffectName, EffectType.DEATH);
            if (!killEffectName.equalsIgnoreCase("null")) effectManager.setPlayerEffect(player, killEffectName, EffectType.KILL);
            if (!shootEffectName.equalsIgnoreCase("null")) effectManager.setPlayerEffect(player, shootEffectName, EffectType.SHOOT);
            if (result.getList("unlockedEffects", String.class) != null) effectManager.getPlayerUnlockedEffects(player).addAll(result.getList("unlockedEffects", String.class));

            // 初始化玩家数据对象
            PlayerData data = new PlayerData(player);
            data.setUuid(UUID.fromString(result.getString("uuid")));
            data.setPlayerName(result.getString("playerName"));
            data.setPlayerLowName(result.getString("lowName"));
            data.setDeathEffect(effectManager.getEffect(deathEffectName));
            data.setKillEffect(effectManager.getEffect(killEffectName));
            data.setShootEffect(effectManager.getEffect(shootEffectName));
            data.setUnlockedEffects(effectManager.getPlayerUnlockedEffects(player));

            // 存入内存
            PlayerData.getData().put(uuid, data);
            player.sendMessage(CC.translate(irina + "&a档案加载完毕!"));
        } else {
            player.sendMessage(CC.translate(irina + "&c档案加载出现异常, 请联系管理员!"));
        }
    }

    @Override
    public void savePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData data = PlayerData.getData().get(uuid);
        if (data == null) {
            plugin.getLogger().warning("玩家数据不存在，无法保存: " + player.getName());
            return;
        }

        Document doc = new Document("deathEffect", data.getDeathEffect() != null ? data.getDeathEffect().getEffectInternalName() : "NULL")
                .append("killEffect", data.getKillEffect() != null ? data.getKillEffect().getEffectInternalName() : "NULL")
                .append("shootEffect", data.getShootEffect() != null ? data.getShootEffect().getEffectInternalName() : "NULL")
                .append("unlockedEffects", data.getUnlockedEffects() != null ? data.getUnlockedEffects() : EffectManager.getInstance().getPlayerUnlockedEffects(player));

        createOrUpdateDocument(doc, uuid);
    }

    public void createOrUpdateDocument(Document document, UUID uuid) {
        MongoCollection<Document> collection = database.getCollection("player");
        Document query = new Document("uuid", uuid.toString());
        Document update = new Document("$set", document);

        if (isDocumentExist("player", uuid.toString())) {
            collection.updateOne(query, update);
        } else {
            collection.insertOne(document);
        }
    }

    public Document findDocumentByUuid(UUID uuid) {
        MongoCollection<Document> collection = database.getCollection("player");
        return collection.find(new Document("uuid", uuid.toString())).first();
    }

    public boolean isDocumentExist(String collectionName, String value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(new Document("uuid", value)).first() != null;
    }

    @Override
    public boolean isExistPlayerData(String collectionName, UUID uuid) {
        return isDocumentExist(collectionName, uuid.toString());
    }
}
