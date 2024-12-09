package cn.sakura.irinacosmetics.database;

import cn.sakura.irinacosmetics.IrinaCosmetics;
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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Mongo implements IDatabase{
    private MongoClient mongoClient;
    @Getter
    @Setter
    private MongoDatabase database;

    private final Plugin pl = IrinaCosmetics.getInstance();

    private final String ip = pl.getConfig().getString("MongoDataBase.ip");
    private final int port = pl.getConfig().getInt("MongoDataBase.port");
    private final int maxConnectionPool = pl.getConfig().getInt("MongoDataBase.maxConnectionPool");
    private final int minConnectionPool = pl.getConfig().getInt("MongoDataBase.minConnectionPool");

    @Override
    public void setUp() {
        try {
            // 自定义连接池设置
            ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                    .maxSize(maxConnectionPool) // 最大连接数
                    .minSize(minConnectionPool) // 最小连接数
                    .build();

            // 自定义 MongoClient 设置
            MongoClientSettings settings = MongoClientSettings.builder()
                    .uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
                    .applyConnectionString(new com.mongodb.ConnectionString("mongodb://"+ ip + ":" + port))
                    .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                    .build();

            // 创建 MongoClient 实例
            mongoClient = MongoClients.create(settings);
            // 获取数据库
            setDatabase(mongoClient.getDatabase(pl.getConfig().getString("MongoDataBase.database")));
        } catch (Exception e) {
            System.err.println("初始化MongoDB连接异常: " + e.getMessage());
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

        PlayerData data = new PlayerData(player);
        Document doc = new Document("playerName", data.getPlayerName())
                .append("lowName", data.getPlayerLowName())
                .append("uuid", data.getUuid());

        String irina = IrinaCosmetics.irina;
        player.sendMessage(CC.translate(irina + "&7未检查到档案数据, 开始创建..."));
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

        }
    }

    @Override
    public void savePlayerData(Player player) {

    }

    @Override
    public void getPlayerData(Player player) {

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
}
