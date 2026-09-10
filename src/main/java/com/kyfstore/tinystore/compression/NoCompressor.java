package com.kyfstore.tinystore.compression;

/**
 * A no-op {@link Compressor} implementation that passes raw byte arrays through
 * unchanged without performing any compression or decompression.
 */
public class NoCompressor implements Compressor {

    /**
     * Constructs a default {@code NoCompressor} instance.
     */
    public NoCompressor() {
    }

    /**
     * Returns the provided byte array without modifying it.
     *
     * @param data the raw byte array
     * @return the same byte array passed in
     */
    @Override
    public byte[] compress(byte[] data) {
        return data;
    }

    /**
     * Returns the provided byte array without modifying it.
     *
     * @param data the byte array
     * @return the same byte array passed in
     */
    @Override
    public byte[] decompress(byte[] data) {
        return data;
    }
}