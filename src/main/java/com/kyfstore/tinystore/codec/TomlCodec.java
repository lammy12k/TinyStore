package com.kyfstore.tinystore.codec;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;

/**
 * A {@link Codec} implementation that provides TOML serialization and deserialization
 * capabilities using the Jackson {@link TomlMapper}.
 */
public class TomlCodec implements Codec {

    private final TomlMapper mapper = new TomlMapper();

    /**
     * Constructs a default {@code TomlCodec} instance.
     */
    public TomlCodec() {
    }

    /**
     * Serializes an object into a TOML byte array.
     *
     * @param value the object to serialize
     * @return a byte array containing the encoded TOML string
     * @throws RuntimeException if an error occurs during TOML serialization
     */
    @Override
    public byte[] serialize(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException("TOML encoding failed", e);
        }
    }

    /**
     * Deserializes a TOML byte array into an instance of the target class.
     *
     * @param <T>   the target object type
     * @param data  the TOML byte array to decode
     * @param clazz the target class type to map the TOML onto
     * @return the deserialized object instance
     * @throws RuntimeException if an error occurs during TOML deserialization
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("TOML decoding failed", e);
        }
    }
}