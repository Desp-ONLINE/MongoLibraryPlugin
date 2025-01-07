package com.binggre.mongolibraryplugin.base;

import com.binggre.binggreapi.functions.Callback;
import com.binggre.mongolibraryplugin.MongoLibraryPlugin;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MongoRepository<ID, T extends MongoData<ID>> {

    public static final String ID_FILED = "id";

    protected final Plugin plugin;
    protected final MongoCollection<Document> collection;
    protected final ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);

    public MongoRepository(Plugin plugin, String database, String collection) {
        this.plugin = plugin;
        this.collection = MongoLibraryPlugin.getInst().getMongoClient()
                .getDatabase(database)
                .getCollection(collection);
    }

    public abstract Document toDocument(T entity);

    public abstract T toEntity(Document document);

    public T findById(ID id) {
        Bson filter;

        if (id instanceof UUID uuid) {
            filter = Filters.eq(ID_FILED, uuid.toString());
        } else {
            filter = Filters.eq(ID_FILED, id);
        }
        Document find = collection.find(filter).first();
        if (find == null) {
            return null;
        }
        return toEntity(find);
    }

    public void findByIdAsync(ID id, Callback<T> callback) {
        runAsync(() -> callback.accept(findById(id)));
    }

    public void save(T entity) {
        Document document = toDocument(entity);
        ID id = entity.getId();

        Bson filter;
        if (id instanceof UUID uuid) {
            filter = Filters.eq(ID_FILED, uuid.toString());
        } else {
            filter = Filters.eq(ID_FILED, id);
        }
        collection.replaceOne(filter, document, replaceOptions);
    }

    public void saveAsync(T entity) {
        runAsync(() -> save(entity));
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        for (Document document : collection.find()) {
            list.add(toEntity(document));
        }
        return list;
    }

    public void findAllAsync(Callback<List<T>> callback) {
        runAsync(() -> callback.accept(findAll()));
    }

    public void deleteById(ID id) {
        Bson filter = Filters.eq(ID_FILED, id);
        collection.deleteOne(filter);
    }

    public void deleteByIdAsync(ID id) {
        runAsync(() -> deleteById(id));
    }

    public T findByFilter(String field, Object filterValue) {
        Bson filter;
        if (filterValue instanceof String stringFilter) {
            filter = Filters.regex(field, "^" + stringFilter + "$", "i");
        } else {
            filter = Filters.eq(field, filterValue);
        }
        Document find = collection.find(filter).first();
        if (find == null) {
            return null;
        }
        return toEntity(find);
    }

    public void findByFilterAsync(String field, Object filterValue, Callback<T> callback) {
        runAsync(() -> callback.accept(findByFilter(field, filterValue)));
    }

    public void update(T entity, String filed, Object value) {
        Bson filter = Filters.eq(ID_FILED, entity.getId());
        Bson update;

        if (value instanceof MongoUpdatable updatable) {
            Document document = updatable.toDocument();
            update = Updates.set(filed, document);
        } else {
            update = Updates.set(filed, value);
        }

        collection.updateOne(filter, update);
    }

    public void drop() {
        collection.drop();
    }

    private void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }
}