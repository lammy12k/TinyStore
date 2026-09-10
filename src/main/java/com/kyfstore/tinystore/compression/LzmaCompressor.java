package com.kyfstore.tinystore.compression;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZInputStream;
import org.tukaani.xz.XZOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A {@link Compressor} implementation utilizing the LZMA/LZMA2 compression format
 * via the Tukaani XZ library.
 */
public class LzmaCompressor implements Compressor {

    /**
     * Constructs a default {@code LzmaCompressor} instance.
     */
    public LzmaCompressor() {
    }

    /**
     * Compresses the input byte array using LZMA2 compression.
     *
     * @param data the uncompressed byte array
     * @return the compressed byte array
     * @throws RuntimeException if an I/O error occurs during compression
     */
    @Override
    public byte[] compress(byte[] data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             XZOutputStream xzOut = new XZOutputStream(baos, new LZMA2Options())) {
            xzOut.write(data);
            xzOut.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("LZMA compression failed", e);
        }
    }

    /**
     * Decompresses an LZMA2-compressed byte array back into its original raw bytes.
     *
     * @param data the compressed byte array
     * @return the decompressed raw byte array
     * @throws RuntimeException if an I/O error occurs during decompression
     */
    @Override
    public byte[] decompress(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             XZInputStream xzIn = new XZInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = xzIn.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("LZMA decompression failed", e);
        }
    }
}