package com.kyfstore.tinystore;

import com.kyfstore.tinystore.codec.Codec;
import com.kyfstore.tinystore.compression.Compressor;
import com.kyfstore.tinystore.driver.StorageDriver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;

/**
 * Main entry point for the TinyStore storage engine.
 * <p>
 * Provides a key-value interface that orchestrates serialization, optional compression,
 * envelope wrapping with Time-To-Live (TTL) handling, and persistence via standard drivers.
 * </p>
 */
public class TinyStore {

    private final StorageDriver driver;
    private final Codec codec;
    private final Compressor compressor;

    private TinyStore(Builder builder) {
        this.driver = builder.driver;
        this.codec = builder.codec;
        this.compressor = builder.compressor;
    }

    /**
     * Serializes, compresses, wraps in an envelope with TTL metadata, and writes an object to storage.
     *
     * @param <T>   the type of object being stored
     * @param key   the unique identifier for the record
     * @param value the object instance to persist
     * @param ttl   the duration before this record expires, or {@code null} for no expiration
     * @throws RuntimeException if an I/O error occurs during serialization or storage write operations
     */
    public <T> void put(String key, T value, Duration ttl) {
        try {
            byte[] rawBytes = codec.serialize(value);
            byte[] compressed = compressor.compress(rawBytes);
            RecordEnvelope envelope = new RecordEnvelope(compressed, ttl != null ? ttl.toMillis() : null);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(envelope);
            driver.write(key, baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error writing to TinyStore", e);
        }
    }

    /**
     * Reads, validates expiration, decompresses, and deserializes an object from storage.
     * <p>
     * If the record has expired, it is automatically removed from the backend driver and {@code null} is returned.
     * </p>
     *
     * @param <T>   the expected target return type
     * @param key   the unique identifier of the record to read
     * @param clazz the target class type for deserialization
     * @return the deserialized object, or {@code null} if the key is missing or expired
     * @throws RuntimeException if an I/O or deserialization error occurs during retrieval
     */
    public <T> T get(String key, Class<T> clazz) {
        byte[] rawData = driver.read(key);
        if (rawData == null) return null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            RecordEnvelope envelope = (RecordEnvelope) ois.readObject();

            if (envelope.isExpired()) {
                driver.delete(key);
                return null;
            }

            byte[] decompressed = compressor.decompress(envelope.getPayload());
            return codec.deserialize(decompressed, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error reading from TinyStore", e);
        }
    }

    /**
     * Fluent builder for instantiating and configuring {@link TinyStore} instances.
     */
    public static class Builder {

        private StorageDriver driver;
        private Codec codec;
        private Compressor compressor;

        /**
         * Constructs a default {@code Builder} instance.
         */
        public Builder() {
        }

        /**
         * Sets the backend {@link StorageDriver} implementation.
         *
         * @param driver the storage driver to use
         * @return this builder instance for chaining
         */
        public Builder setDriver(StorageDriver driver) {
            this.driver = driver;
            return this;
        }

        /**
         * Sets the serialization {@link Codec} implementation.
         *
         * @param codec the codec to use
         * @return this builder instance for chaining
         */
        public Builder setCodec(Codec codec) {
            this.codec = codec;
            return this;
        }

        /**
         * Sets the {@link Compressor} pipeline implementation.
         *
         * @param compressor the compressor to use
         * @return this builder instance for chaining
         */
        public Builder setCompressor(Compressor compressor) {
            this.compressor = compressor;
            return this;
        }

        /**
         * Constructs a new {@link TinyStore} configured with the specified options.
         *
         * @return a new {@code TinyStore} instance
         */
        public TinyStore build() {
            return new TinyStore(this);
        }
    }
}