package com.kyfstore.tinystore.driver;

import java.io.File;
import java.nio.file.Files;

/**
 * A {@link StorageDriver} implementation that persists raw data to the local file system as plain text files.
 * <p>
 * Each record is written to a separate {@code .txt} file within a specified base directory.
 * </p>
 */
public class PlaintextDriver implements StorageDriver {

    private final File baseDir;

    /**
     * Constructs a {@code PlaintextDriver} targeting the specified base directory path.
     * <p>
     * If the directory does not exist, it will be automatically created.
     * </p>
     *
     * @param baseDirPath the path to the directory where plain text files will be stored
     */
    public PlaintextDriver(String baseDirPath) {
        this.baseDir = new File(baseDirPath);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /**
     * Writes raw bytes to a file named after the provided key with a {@code .txt} extension.
     *
     * @param key  the unique identifier for the record
     * @param data the byte payload to persist
     * @throws RuntimeException if an error occurs while writing the file
     */
    @Override
    public void write(String key, byte[] data) {
        try {
            Files.write(new File(baseDir, key + ".txt").toPath(), data);
        } catch (Exception e) {
            throw new RuntimeException("Failed file write", e);
        }
    }

    /**
     * Reads the raw byte array from the plain text file associated with the given key.
     *
     * @param key the unique identifier of the record to read
     * @return the byte array contained in the file, or {@code null} if the file does not exist
     * @throws RuntimeException if an error occurs while reading the file
     */
    @Override
    public byte[] read(String key) {
        File file = new File(baseDir, key + ".txt");
        if (!file.exists()) return null;
        try {
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException("Failed file read", e);
        }
    }

    /**
     * Deletes the plain text file associated with the given key.
     *
     * @param key the unique identifier of the record to remove
     * @return {@code true} if the file existed and was successfully deleted; {@code false} otherwise
     */
    @Override
    public boolean delete(String key) {
        File file = new File(baseDir, key + ".txt");
        return file.exists() && file.delete();
    }
}