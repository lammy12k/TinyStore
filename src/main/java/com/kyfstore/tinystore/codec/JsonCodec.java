package com.kyfstore.tinystore.codec;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A {@link Codec} implementation that provides JSON serialization and deserialization
 * capabilities using the Jackson {@link ObjectMapper}.
 */
public class JsonCodec implements Codec {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a default {@code JsonCodec} instance.
     */
    public JsonCodec() {
    }

    /**
     * Serializes an object into a JSON byte array.
     *
     * @param value the object to serialize
     * @return a byte array containing the encoded JSON string
     * @throws RuntimeException if an error occurs during JSON serialization
     */
    @Override
    public byte[] serialize(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON encoding failed", e);
        }
    }

    /**
     * Deserializes a JSON byte array into an instance of the target class.
     *
     * @param <T>   the target object type
     * @param data  the JSON byte array to decode
     * @param clazz the target class type to map the JSON onto
     * @return the deserialized object instance
     * @throws RuntimeException if an error occurs during JSON deserialization
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON decoding failed", e);
        }
    }
}