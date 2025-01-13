package com.binggre.mongolibraryplugin.base;

import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mongolibraryplugin.MongoLibraryPlugin;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;

public abstract class MongoConfiguration {

    public abstract void init();

    private transient boolean save;

    @Getter
    protected final MongoCollection<Document> collection;

    public MongoConfiguration(String database, String collection) {
        this.collection = MongoLibraryPlugin.getInst().getMongoClient()
                .getDatabase(database)
                .getCollection(collection);

        Document configDocument = getConfigDocument();
        if (configDocument == null) {
            save = true;
        }
    }

    public Document getConfigDocument() {
        return collection.find().first();
    }

    public void save() {
        if (save) {
            collection.drop();
            collection.insertOne(Document.parse(FileManager.toJson(this)));
            save = false;
        }
    }
}