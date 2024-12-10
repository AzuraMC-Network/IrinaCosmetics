package cn.sakura.irinacosmetics.database;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.cosmetics.AbstractEffect;
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

public class Mongo implements IDatabase{
    private final String irina = IrinaCosmetics.irina;
    private MongoClient mongoClient;
    @Getter
    @Setter
    private MongoDatabase database;

    private final Plugin pl = IrinaCosmetics.getInstance();

    private final Boolean enableVerify = pl.getConfig().getBoolean("MongoDataBase.enableVerify");
    private final String ip = pl.getConfig().getString("MongoDataBase.ip");
    private final int port = pl.getConfig().getInt("MongoDataBase.port");
    private final String databaseName = pl.getConfig().getString("MongoDataBase.database");
    private final String userName = pl.getConfig().getString("MongoDataBase.username");
    private final String password = pl.getConfig().getString("MongoDataBase.password");
    private final int maxConnectionPool = pl.getConfig().getInt("MongoDataBase.maxConnectionPool");
    private final int minConnectionPool = pl.getConfig().getInt("MongoDataBase.minConnectionPool");

    @Override
    public void setUp() {
        try {
            // 自定义连接池设置
            ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                    .maxSize(maxConnectionPool > 0 ? maxConnectionPool : 10) // 确保最大连接数有效
                    .minSize(minConnectionPool >= 0 ? minConnectionPool : 1) // 确保最小连接数非负
                    .build();

            // 构建连接字符串
            String connectionString = enableVerify ?
                    String.format("mongodb://%s:%s@%s:%d/%s", userName, password, ip, port, databaseName) :
                    String.format("mongodb://%s:%d/%s", ip, port, databaseName);

            // 自定义 MongoClient 设置
            MongoClientSettings settings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.JAVA_LEGACY) // 兼容旧版 Java 的 UUID 格式
                    .applyConnectionString(new com.mongodb.ConnectionString(connectionString))
                    .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                    .build();

            // 创建 MongoClient 实例
            mongoClient = MongoClients.create(settings);

            // 获取数据库名称
            String dbName = pl.getConfig().getString("MongoDataBase.database");
            if (dbName == null || dbName.isEmpty()) {
                throw new IllegalArgumentException("配置中未指定数据库名称");
            }

            // 检查并连接数据库
            MongoDatabase database = mongoClient.getDatabase(dbName);
            database.runCommand(new Document("ping", 1)); // 检测连接是否成功

            // 自动创建数据库
            database.getCollection("test_collection").insertOne(new Document("key", "value")); // 自动触发数据库创建
            database.getCollection("test_collection").deleteMany(new Document()); // 清除临时数据

            // 设置数据库
            setDatabase(database);

            System.out.println("MongoDB 数据库已成功连接或创建：" + dbName);
        } catch (IllegalArgumentException e) {
            System.err.println("配置错误: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(IrinaCosmetics.getInstance());
        } catch (Exception e) {
            System.err.println("初始化 MongoDB 连接异常: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(IrinaCosmetics.getInstance());
        }
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public void createPlayerData(Player player) {
        PlayerData data = getData(player);
        Document doc = new Document("playerName", data.getPlayerName())
                .append("lowName", data.getPlayerLowName())
                .append("uuid", data.getUuid())
                .append("killEffect", data.getKillEffect())
                .append("deathEffect", data.getDeathEffect())
                .append("shootEffect", data.getShootEffect())
                .append("unlockedEffects", data.getUnlockedCosmetics());

        player.sendMessage(CC.translate(irina + "&7未检查到档案, 开始创建..."));
        createPlayerProfileByPlayerName("player", doc, data.getPlayerName());
        player.sendMessage(CC.translate(irina + "&a创建成功!"));

    }

    @Override
    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        MongoCollection<Document> collection = getDatabase().getCollection("player");

        Document query = new Document("uuid", uuid.toString());
        Document result = collection.find(query).first();

        if (result != null) {
            PlayerData data = new PlayerData(player);
            data.setPlayerName(result.getString("playerName"));
            data.setPlayerLowName(result.getString("lowName"));
            data.setUuid(UUID.fromString(result.getString("uuid")));
            data.setDeathEffect((AbstractEffect) result.get("deathEffect"));
            data.setKillEffect((AbstractEffect) result.get("killEffect"));
            data.setShootEffect((AbstractEffect) result.get("shootEffect"));
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
        PlayerData data = PlayerData.getPlayerData(player);
        Document doc = new Document("deathEffect", data.getDeathEffect())
                .append("killEffect", data.getKillEffect())
                .append("shootEffect", data.getShootEffect())
                .append("unlockedEffects", data.getUnlockedEffects());

        updateCollectionByUuid("player", doc, uuid);
    }

    @Override
    public void removePlayerData(Player player) {
        PlayerData.getData().remove(player.getUniqueId());
    }

    @Override
    public PlayerData getData(Player player) {
        UUID uuid = player.getUniqueId();
        if (!PlayerData.getData().containsKey(uuid)) {
            PlayerData newData = new PlayerData(player);
            PlayerData.getData().put(uuid, newData);
        }
        return PlayerData.getData().get(uuid);
    }

    /**
     * 插入内容到集合
     * @param collectionName 集合名称
     * @param document 要插入的文档
     * @param uuid 玩家的uuid
     */
    public void updateCollectionByUuid(String collectionName, Document document, UUID uuid) {
        if (getDatabase() == null) {
            throw new IllegalStateException("数据库尚未连接。");
        }
        MongoCollection<Document> collection = getDatabase().getCollection(collectionName);
        Document query = new Document("uuid", uuid.toString());
        Document updateValue = new Document("$set", document);

        if (isExistValueInCollection(collectionName, "uuid", uuid.toString())) {
            collection.updateOne(query, updateValue);
        } else {
            System.err.print("严重错误 | 玩家数据文档不存在但却执行了更新操作");
        }
    }

    /**
     * 插入内容到集合
     * @param collectionName 集合名称
     * @param document 要插入的文档
     */
    public void createPlayerProfileByPlayerName(String collectionName, Document document, String playerName) {
        if (getDatabase() == null) {
            throw new IllegalStateException("数据库尚未连接。");
        }
        MongoCollection<Document> collection = getDatabase().getCollection(collectionName);

        if (!isExistValueInCollection(collectionName, "playerName", playerName)) {
            collection.insertOne(document);
        }
    }

    /**
     * 查询集合内容是否存在
     * @param collectionName 想要查询的集合名
     * @param checkType 想要查询的类型
     * @param checkValue 想要查询的内容
     * @return 找到返回true
     */
    public boolean isExistValueInCollection(String collectionName, String checkType, String checkValue) {
        MongoCollection<Document> collection = getDatabase().getCollection(collectionName);

        Document query = new Document(checkType, checkValue);
        Document result = collection.find(query).first(); // 获取第一条匹配的记录

        return result != null;
    }

    /**
     * 查询集合中是否存在此玩家
     * @param collectionName 想要查询的集合名
     * @param playerName 玩家名
     * @return 找到返回true
     */
    @Override
    public boolean isExistPlayerProfile(String collectionName, String playerName) {
        MongoCollection<Document> collection = getDatabase().getCollection(collectionName);

        Document query = new Document("playerName", playerName);
        Document result = collection.find(query).first(); // 获取第一条匹配的记录

        return result != null;
    }
}
