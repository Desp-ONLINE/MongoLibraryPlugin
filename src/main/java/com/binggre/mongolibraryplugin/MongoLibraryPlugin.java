package com.binggre.mongolibraryplugin;

import com.mongodb.client.MongoClient;
import org.bukkit.plugin.java.JavaPlugin;

public final class MongoLibraryPlugin extends JavaPlugin {

    private static MongoLibraryPlugin inst;

    public static MongoLibraryPlugin getInst() {
        return inst;
    }

    private MongoClient mongoClient;

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public void onEnable() {
        inst = this;
        saveResource("database.yml", false);
        mongoClient = new MongoDB().getMongoClient();
    }

    @Override
    public void onDisable() {
    }
}
