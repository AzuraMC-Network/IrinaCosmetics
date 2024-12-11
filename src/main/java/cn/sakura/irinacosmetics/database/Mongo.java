package cn.sakura.irinacosmetics.database;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
import cn.sakura.irinacosmetics.cosmetics.EffectManager;
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

import java.util.List;
import java.util.UUID;

public class Mongo implements IDatabase {
    private final String irina = IrinaCosmetics.irina;
    private MongoClient mongoClient;

    @Getter
    @Setter
    private MongoDatabase database;

    private final Plugin plugin = IrinaCosmetics.getInstance();

    // MongoDB 配置信息
    private final boolean enableVerify;
    private final String ip;
    private final int port;
    private final String databaseName;
    private final String userName;
    private final String password;
    private final int maxConnectionPool;
    private final int minConnectionPool;

    public Mongo() {
        // 从配置文件加载
        enableVerify = plugin.getConfig().getBoolean("MongoDataBase.enableVerify");
        ip = plugin.getConfig().getString("MongoDataBase.ip");
        port = plugin.getConfig().getInt("MongoDataBase.port");
        databaseName = plugin.getConfig().getString("MongoDataBase.database");
        userName = plugin.getConfig().getString("MongoDataBase.username");
        password = plugin.getConfig().getString("MongoDataBase.password");
        maxConnectionPool = plugin.getConfig().getInt("MongoDataBase.maxConnectionPool", 10);
        minConnectionPool = plugin.getConfig().getInt("MongoDataBase.minConnectionPool", 1);
    }

    @Override
    public void setUp() {
        try {
            // 连接池设置
            ConnectionPoolSettings poolSettings = ConnectionPoolSettings.builder()
                    .maxSize(Math.max(maxConnectionPool, 150))
                    .minSize(Math.max(minConnectionPool, 10))
                    .build();

            // 构建连接字符串
            String connectionString = enableVerify ?
                    String.format("mongodb://%s:%s@%s:%d/%s", userName, password, ip, port, databaseName) :
                    String.format("mongodb://%s:%d/%s", ip, port, databaseName);

            // MongoDB 设置
            MongoClientSettings settings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                    .applyConnectionString(new com.mongodb.ConnectionString(connectionString))
                    .applyToConnectionPoolSettings(builder -> builder.applySettings(poolSettings))
                    .build();

            // 创建 MongoClient
            mongoClient = MongoClients.create(settings);

            // 检测数据库连接
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            db.runCommand(new Document("ping", 1));
            db.createCollection("player");

            setDatabase(db);
            plugin.getLogger().info("MongoDB 数据库连接成功: " + databaseName);
        } catch (Exception e) {
            plugin.getLogger().severe("MongoDB 初始化失败: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
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
        PlayerData data = getData(player);
        Document doc = new Document("playerName", data.getPlayerName())
                .append("lowName", data.getPlayerLowName())
                .append("uuid", data.getUuid())
                .append("killEffect", data.getKillEffect().getEffectInternalName())
                .append("deathEffect", data.getDeathEffect().getEffectInternalName())
                .append("shootEffect", data.getShootEffect().getEffectInternalName())
                .append("unlockedEffects", data.getUnlockedEffects());

        player.sendMessage(CC.translate(irina + "&7未检查到档案, 开始创建..."));
        createPlayerProfile("player", doc, data.getUuid());
        player.sendMessage(CC.translate(irina + "&a创建成功!"));
    }

    @Override
    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        MongoCollection<Document> collection = database.getCollection("player");

        Document query = new Document("uuid", uuid);
        Document result = collection.find(query).first();

        if (result != null) {
            PlayerData data = new PlayerData(player);
            EffectManager effectManager = EffectManager.getInstance();
            data.setUuid(UUID.fromString(result.getString("uuid")));  // 确保读取UUID时的格式正确
            data.setPlayerName(result.getString("playerName"));
            data.setPlayerLowName(result.getString("lowName"));
            data.setDeathEffect(effectManager.getEffect(result.getString("deathEffect")));
            data.setKillEffect(effectManager.getEffect(result.getString("killEffect")));
            data.setShootEffect(effectManager.getEffect(result.getString("shootEffect")));
            data.setUnlockedEffects((List<AbstractEffect>) result.get("unlockedEffects"));

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

        // 在保存时要检查 PlayerData 是否为空
        if (data == null) {
            plugin.getLogger().warning("玩家数据不存在，无法保存数据: " + player.getName());
            return;
        }

        Document doc = new Document("deathEffect", data.getDeathEffect())
                .append("killEffect", data.getKillEffect().getEffectInternalName())
                .append("shootEffect", data.getShootEffect().getEffectInternalName())
                .append("unlockedEffects", data.getUnlockedEffects());

        updateCollectionByUuid("player", doc, uuid);  // 确保保存时UUID正确
    }

    @Override
    public void removePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerData.getData().remove(uuid);  // 在退出时清除玩家数据
    }

    @Override
    public PlayerData getData(Player player) {
        UUID uuid = player.getUniqueId();
        return PlayerData.getData().computeIfAbsent(uuid, k -> new PlayerData(player));  // 如果没有数据则创建
    }

    public void updateCollectionByUuid(String collectionName, Document document, UUID uuid) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document query = new Document("uuid", uuid.toString());
        Document updateValue = new Document("$set", document);

        if (isExistValueInCollection(collectionName, "uuid", uuid)) {
            collection.updateOne(query, updateValue);  // 如果存在就更新
        } else {
            plugin.getLogger().severe("玩家数据文档不存在但却执行了更新操作: " + uuid);
        }
    }

    public void createPlayerProfile(String collectionName, Document document, UUID uuid) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        if (!isExistValueInCollection(collectionName, "playerName", uuid)) {
            collection.insertOne(document);
        }
    }

    public boolean isExistValueInCollection(String collectionName, String checkType, UUID checkValue) {

        if (database == null) {
            plugin.getLogger().warning("数据库未初始化");
            return false;  // 如果数据库对象为 null，返回 false
        }

        // 获取 MongoDB 集合
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // 使用 countDocuments 来提高效率
        Document query = new Document(checkType, checkValue.toString());
        Document result = collection.find(query).first();

        return result != null;
    }

    @Override
    public boolean isExistPlayerProfile(String collectionName, UUID uuid) {
        return isExistValueInCollection(collectionName, "uuid", uuid);
    }
}
