package com.kyfstore.tinystore.codec;

/**
 * Strategy interface for serializing objects to byte arrays and deserializing byte arrays back into objects.
 */
public interface Codec {

    /**
     * Serializes an object into its byte array representation.
     *
     * @param value the object to serialize
     * @return a byte array representing the serialized object
     */
    byte[] serialize(Object value);

    /**
     * Deserializes a byte array into an instance of the specified class.
     *
     * @param <T>   the target type
     * @param data  the raw byte array containing serialized data
     * @param clazz the target class type to deserialize into
     * @return the deserialized object instance
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}