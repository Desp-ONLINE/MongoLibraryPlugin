package com.binggre.mongolibraryplugin;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MongoDB {

    private final MongoClient mongoClient;

    public MongoDB() {
        MongoLibraryPlugin plugin = MongoLibraryPlugin.getInst();
        File databaseFile = new File(plugin.getDataFolder().getPath(), "database.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(databaseFile);

        String url = yaml.getString("mongodb.url");
        int port = yaml.getInt("mongodb.port");
        String user = yaml.getString("mongodb.user");
        String pass = yaml.getString("mongodb.password");
        String address = yaml.getString("mongodb.address");

        String atlasString = String.format("%s%s:%s@%s:%s/", url, user, pass, address, port);

        ConnectionString connectionString = new ConnectionString(atlasString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        mongoClient = MongoClients.create(settings);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}