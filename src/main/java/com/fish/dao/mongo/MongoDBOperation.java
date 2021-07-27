package com.fish.dao.mongo;

import com.fish.model.UserData;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;

/**
 * @author: fjjdragon
 * @date: 2021-07-27 0:02
 */
public class MongoDBOperation {
    private static FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.BEFORE);

    public static Document findOneAndUpdateUserInfo(Document cdn, UserData userData) {
        MongoCollection<Document> col = MongoDao.getInstance().getMongoCollection("test", "user_info");
        Document $set = new Document();
        Document $setOnInsert = new Document();


        return col.findOneAndUpdate(cdn, new Document("$set", $set).append("$setOnInsert", $setOnInsert), options);
    }

}