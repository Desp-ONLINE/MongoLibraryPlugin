package com.binggre.mongolibraryplugin.base;

import com.binggre.binggreapi.utils.file.FileManager;
import com.binggre.mongolibraryplugin.MongoLibraryPlugin;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

public abstract class MongoConfiguration {

    public abstract void init();

    private transient boolean save;

    @Getter
    protected transient final MongoDatabase database;
    @Getter
    protected transient final MongoCollection<Document> collection;

    public MongoConfiguration(String database, String collection) {
        this.database = MongoLibraryPlugin.getInst().getMongoClient().getDatabase(database);
        this.database.createCollection(collection);
        this.collection = this.database.getCollection(collection);

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