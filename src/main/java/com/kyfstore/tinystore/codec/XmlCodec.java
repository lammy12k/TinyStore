package com.kyfstore.tinystore.codec;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * A {@link Codec} implementation that provides XML serialization and deserialization
 * capabilities using the Jackson {@link XmlMapper}.
 */
public class XmlCodec implements Codec {

    private final XmlMapper mapper = new XmlMapper();

    /**
     * Constructs a default {@code XmlCodec} instance.
     */
    public XmlCodec() {
    }

    /**
     * Serializes an object into an XML byte array.
     *
     * @param value the object to serialize
     * @return a byte array containing the encoded XML string
     * @throws RuntimeException if an error occurs during XML serialization
     */
    @Override
    public byte[] serialize(Object value) {
        try {
            return mapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException("XML encoding failed", e);
        }
    }

    /**
     * Deserializes an XML byte array into an instance of the target class.
     *
     * @param <T>   the target object type
     * @param data  the XML byte array to decode
     * @param clazz the target class type to map the XML onto
     * @return the deserialized object instance
     * @throws RuntimeException if an error occurs during XML deserialization
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("XML decoding failed", e);
        }
    }
}