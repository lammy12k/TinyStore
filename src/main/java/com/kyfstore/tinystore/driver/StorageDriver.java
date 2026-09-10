package com.kyfstore.tinystore.driver;

/**
 * Strategy interface defining persistence operations for raw byte payloads.
 * <p>
 * Implementations bridge the library to specific storage backends such as
 * databases, local file systems, or in-memory stores.
 * </p>
 */
public interface StorageDriver {

    /**
     * Persists a raw byte array payload under the specified key.
     * <p>
     * If the key already exists, the implementation should overwrite the existing payload.
     * </p>
     *
     * @param key  the unique identifier for the record
     * @param data the raw byte payload to store
     */
    void write(String key, byte[] data);

    /**
     * Retrieves the raw byte array payload associated with the specified key.
     *
     * @param key the unique identifier of the record to read
     * @return the raw byte payload, or {@code null} if no record is found
     */
    byte[] read(String key);

    /**
     * Removes the record associated with the specified key from the underlying storage.
     *
     * @param key the unique identifier of the record to delete
     * @return {@code true} if a record was found and deleted, {@code false} otherwise
     */
    boolean delete(String key);
}