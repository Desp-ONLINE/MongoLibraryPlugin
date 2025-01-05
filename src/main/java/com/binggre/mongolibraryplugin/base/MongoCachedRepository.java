package com.binggre.mongolibraryplugin.base;

import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class MongoCachedRepository<ID, T extends MongoData<ID>> extends MongoRepository<ID, T> {

    protected final Map<ID, T> cache;

    public MongoCachedRepository(Plugin plugin, String database, String collection, Map<ID, T> cache) {
        super(plugin, database, collection);
        this.cache = cache;
    }

    public Map<ID, T> getCache() {
        return new HashMap<>(cache);
    }

    public T get(ID id) {
        return cache.get(id);
    }

    public void putIn(T entity) {
        cache.put(entity.getId(), entity);
    }

    public T remove(ID id) {
        return cache.remove(id);
    }

    public Collection<T> values() {
        return cache.values();
    }

    @Override
    public void drop() {
        cache.clear();
        super.drop();
    }
}