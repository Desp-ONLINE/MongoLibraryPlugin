package com.binggre.mongolibraryplugin;

import com.mongodb.client.MongoClient;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MongoLibraryPlugin extends JavaPlugin {

    @Getter
    private static MongoLibraryPlugin inst;

    @Getter
    private MongoClient mongoClient;

    @Override
    public void onEnable() {
        inst = this;
        saveResource("database.yml", false);
        mongoClient = new MongoDB().getMongoClient();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
