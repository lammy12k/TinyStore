package com.kyfstore.tinystore.codec;

import com.typesafe.config.ConfigFactory;

/**
 * A {@link Codec} implementation that handles HOCON / .conf configuration format serialization
 * and deserialization using the Typesafe Config library.
 */
public class ConfCodec implements Codec {

    /**
     * Constructs a new {@code ConfCodec}.
     */
    public ConfCodec() {
    }

    /**
     * Serializes the given object into a byte array representation using its string format.
     *
     * @param value the object to serialize
     * @return the byte array representation of the object's string form
     */
    @Override
    public byte[] serialize(Object value) {
        return value.toString().getBytes();
    }

    /**
     * Deserializes a byte array containing HOCON / .conf formatted text into a {@link com.typesafe.config.Config} instance.
     *
     * @param <T>   the target type
     * @param data  the raw byte array containing HOCON configuration text
     * @param clazz the target class type
     * @return the parsed configuration object
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        String content = new String(data);
        return (T) ConfigFactory.parseString(content);
    }
}