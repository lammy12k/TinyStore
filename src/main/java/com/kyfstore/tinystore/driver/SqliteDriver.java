package com.kyfstore.tinystore.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A {@link StorageDriver} implementation that persists key-value data to an embedded SQLite database.
 * <p>
 * Stores raw byte payloads inside a binary column ({@code BLOB}) using JDBC transactions.
 * </p>
 */
public class SqliteDriver implements StorageDriver {

    private final String connectionUrl;

    /**
     * Constructs a {@code SqliteDriver} targeting the specified database file path.
     * <p>
     * Automatically initializes the underlying SQLite database and creates the {@code store} table if missing.
     * </p>
     *
     * @param dbPath the file path to the SQLite database file
     * @throws RuntimeException if database table initialization fails
     */
    public SqliteDriver(String dbPath) {
        this.connectionUrl = "jdbc:sqlite:" + dbPath;
        initTable();
    }

    private void initTable() {
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS store (key TEXT PRIMARY KEY, val BLOB)");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to init SQLite", e);
        }
    }

    /**
     * Inserts or replaces the byte payload associated with the specified key in the SQLite database.
     *
     * @param key  the unique identifier for the record
     * @param data the byte payload to persist
     * @throws RuntimeException if a database error occurs during the write operation
     */
    @Override
    public void write(String key, byte[] data) {
        String sql = "INSERT OR REPLACE INTO store(key, val) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setBytes(2, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed SQLite write", e);
        }
    }

    /**
     * Retrieves the byte payload associated with the specified key from the SQLite database.
     *
     * @param key the unique identifier of the record to read
     * @return the byte array payload, or {@code null} if the key is not found
     * @throws RuntimeException if a database error occurs during the read operation
     */
    @Override
    public byte[] read(String key) {
        String sql = "SELECT val FROM store WHERE key = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getBytes("val");
        } catch (SQLException e) {
            throw new RuntimeException("Failed SQLite read", e);
        }
        return null;
    }

    /**
     * Deletes the record associated with the specified key from the SQLite database.
     *
     * @param key the unique identifier of the record to remove
     * @return {@code true} if a record was deleted, {@code false} if no matching key was found
     * @throws RuntimeException if a database error occurs during the deletion operation
     */
    @Override
    public boolean delete(String key) {
        String sql = "DELETE FROM store WHERE key = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed SQLite delete", e);
        }
    }
}