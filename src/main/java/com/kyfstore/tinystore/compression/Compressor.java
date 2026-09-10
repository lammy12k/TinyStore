package com.kyfstore.tinystore.compression;

/**
 * Strategy interface for compressing and decompressing raw byte arrays.
 */
public interface Compressor {

    /**
     * Compresses the provided byte array.
     *
     * @param data the uncompressed byte array
     * @return the compressed byte array
     */
    byte[] compress(byte[] data);

    /**
     * Decompresses the provided compressed byte array back to its original state.
     *
     * @param data the compressed byte array
     * @return the decompressed byte array
     */
    byte[] decompress(byte[] data);
}