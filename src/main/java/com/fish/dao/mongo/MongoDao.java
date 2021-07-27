package com.fish.dao.mongo;

import com.fish.common.ServerConfig;
import com.fish.util.YamlUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: fjjdragon
 * @date: 2021-06-23 0:15
 */
@Slf4j
public class MongoDao {

    public static MongoDao getInstance() {
        return MongoDao.MongoDaoHolder.instance;
    }

    static class MongoDaoHolder {
        private static MongoDao instance = new MongoDao();
    }

    private MongoClient mongoClient = null;

    private MongoDao() {
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MongoDao.getInstance().close()));
        if (!MongoDao.getInstance().init()) {
            System.exit(0);
        }

    }


    /**
     * 初始哈mongodb链接
     *
     * @return
     */
    public boolean init() {
        if (null != mongoClient)
            return true;
        try {
            String uri = YamlUtil.getValueByKey(ServerConfig.getInstance().getConfigProperties(), "mongodb.uri", "");
            MongoClientOptions.Builder options = new MongoClientOptions.Builder();
            options.cursorFinalizerEnabled(true);
            options.connectionsPerHost(300);
            options.connectTimeout(30000);
            options.maxWaitTime(5000);
            options.socketTimeout(0);
            options.writeConcern(WriteConcern.ACKNOWLEDGED);
            options.build();
            mongoClient = new MongoClient(new MongoClientURI(uri, options));
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }

    }

    /**
     * 获取mongodb实例
     *
     * @param mMongoDatabase
     * @return
     */
    public MongoDatabase getMongoDatabase(String mMongoDatabase) {
        return mongoClient.getDatabase(mMongoDatabase);
    }

    /**
     * 获取mongodb 数据表
     *
     * @param mMongoDatabase
     * @param mMongoCollection
     * @return
     */
    public MongoCollection<Document> getMongoCollection(String mMongoDatabase, String mMongoCollection) {
        return mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection);
    }

    /**
     * 查找一个记录
     *
     * @param mMongoDatabase
     * @param mMongoCollection
     * @param document
     * @return
     */
    public Document findOne(String mMongoDatabase, String mMongoCollection, Document document) {
        MongoCursor<Document> cur = mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection)
                .find(document).limit(1).iterator();
        if (cur.hasNext())
            return cur.next();
        return null;
    }

    /**
     * 查找一个记录
     *
     * @param mMongoDatabase
     * @param mMongoCollection
     * @param document
     * @return
     */
    public Document findOne(String mMongoDatabase, String mMongoCollection, Document document, Document slice) {
        MongoCursor<Document> cur = mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection)
                .find(document).limit(1).projection(slice).iterator();
        if (cur.hasNext())
            return cur.next();
        return null;
    }

    /**
     * 计数
     *
     * @param mMongoDatabase
     * @param mMongoCollection
     * @param document
     * @return
     */
    public int findCount(String mMongoDatabase, String mMongoCollection, Document document) {
        return (int) mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection).countDocuments(document);
    }

    /**
     * 查询数据列表
     *
     * @param mMongoDatabase
     * @param mMongoCollection
     * @param cdn
     * @param slice
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public List<Document> getList(String mMongoDatabase, String mMongoCollection, Document cdn, Document slice,
                                  Document sort, int page, int pageSize) {
        List<Document> list = new ArrayList<Document>();
        MongoCursor<Document> cur = null;
        if (pageSize > 0) {
            cur = mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection).find(cdn).sort(sort)
                    .skip(pageSize * page).limit(pageSize).projection(slice).iterator();
        } else {
            cur = mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection).find(cdn).sort(sort)
                    .projection(slice).iterator();
        }
        while (cur.hasNext()) {
            list.add(cur.next());
        }
        return list;
    }

    /**
     * 查询数组数据
     *
     * @param mMongoDatabase
     * @param mMongoCollection
     * @param cdn
     * @param slice
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public MongoCursor<Document> getMongoCursor(String mMongoDatabase, String mMongoCollection, Document cdn,
                                                Document slice, Document sort, int page, int pageSize) {
        return mongoClient.getDatabase(mMongoDatabase).getCollection(mMongoCollection).find(cdn).sort(sort)
                .skip(pageSize * page).limit(pageSize).projection(slice).iterator();
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}