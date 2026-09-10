package com.kyfstore.tinystore.driver;

import java.io.File;
import java.nio.file.Files;

/**
 * A {@link StorageDriver} implementation that persists raw binary data to the local file system.
 * <p>
 * Each stored record is saved as a separate {@code .bin} file within a specified base directory.
 * </p>
 */
public class BinaryDriver implements StorageDriver {

    private final File baseDir;

    /**
     * Constructs a {@code BinaryDriver} targeting the specified base directory path.
     * <p>
     * If the directory does not exist, it will be automatically created.
     * </p>
     *
     * @param baseDirPath the path to the directory where binary files will be stored
     */
    public BinaryDriver(String baseDirPath) {
        this.baseDir = new File(baseDirPath);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    /**
     * Writes raw binary data to a file named after the provided key with a {@code .bin} extension.
     *
     * @param key  the unique identifier for the record
     * @param data the raw byte payload to persist
     * @throws RuntimeException if an error occurs while writing the file
     */
    @Override
    public void write(String key, byte[] data) {
        try {
            File file = new File(baseDir, key + ".bin");
            Files.write(file.toPath(), data);
        } catch (Exception e) {
            throw new RuntimeException("Binary file write failed", e);
        }
    }

    /**
     * Reads the raw byte array from the binary file associated with the given key.
     *
     * @param key the unique identifier of the record to read
     * @return the byte array contained in the file, or {@code null} if the file does not exist
     * @throws RuntimeException if an error occurs while reading the file
     */
    @Override
    public byte[] read(String key) {
        File file = new File(baseDir, key + ".bin");
        if (!file.exists()) return null;
        try {
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException("Binary file read failed", e);
        }
    }

    /**
     * Deletes the binary file associated with the given key.
     *
     * @param key the unique identifier of the record to remove
     * @return {@code true} if the file existed and was successfully deleted; {@code false} otherwise
     */
    @Override
    public boolean delete(String key) {
        File file = new File(baseDir, key + ".bin");
        return file.exists() && file.delete();
    }
}