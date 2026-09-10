package com.kyfstore.tinystore.driver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory implementation of {@link StorageDriver} backed by a {@link ConcurrentHashMap}.
 * <p>
 * Useful for testing, temporary caching, or volatile storage requirements where data persistence
 * across application restarts is not required. Thread-safe for concurrent access.
 * </p>
 */
public class InMemoryDriver implements StorageDriver {

    private final Map<String, byte[]> store = new ConcurrentHashMap<>();

    /**
     * Constructs a default {@code InMemoryDriver} with an empty store.
     */
    public InMemoryDriver() {
    }

    /**
     * Stores a copy of the raw byte payload in memory under the given key.
     *
     * @param key  the unique identifier for the record
     * @param data the raw byte payload to store
     */
    @Override
    public void write(String key, byte[] data) {
        store.put(key, data);
    }

    /**
     * Retrieves the raw byte payload stored in memory for the specified key.
     *
     * @param key the unique identifier of the record to read
     * @return the stored byte array, or {@code null} if no value is associated with the key
     */
    @Override
    public byte[] read(String key) {
        return store.get(key);
    }

    /**
     * Removes the record associated with the specified key from memory.
     *
     * @param key the unique identifier of the record to remove
     * @return {@code true} if a record was removed, {@code false} if the key was not found
     */
    @Override
    public boolean delete(String key) {
        return store.remove(key) != null;
    }
}