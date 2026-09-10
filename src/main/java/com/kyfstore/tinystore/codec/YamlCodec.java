package com.kyfstore.tinystore.codec;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * A {@link Codec} implementation that provides YAML serialization and deserialization
 * capabilities using the Jackson {@link YAMLMapper}.
 */
public class YamlCodec implements Codec {

    private final YAMLMapper mapper = new YAMLMapper();

    /**
     * Constructs a default {@code YamlCodec} instance.
     */
    public YamlCodec() {
    }

    /**
     * Serializes an object into a YAML byte array.
     *
     * @param value the object to serialize
     * @return a byte array containing the encoded YAML string
     * @throws RuntimeException if an error occurs during YAML serialization
     */
    @Override
    public byte[] serialize(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException("YAML encoding failed", e);
        }
    }

    /**
     * Deserializes a YAML byte array into an instance of the target class.
     *
     * @param <T>   the target object type
     * @param data  the YAML byte array to decode
     * @param clazz the target class type to map the YAML onto
     * @return the deserialized object instance
     * @throws RuntimeException if an error occurs during YAML deserialization
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("YAML decoding failed", e);
        }
    }
}