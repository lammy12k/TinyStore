package com.kyfstore.tinystore.driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A {@link StorageDriver} implementation that persists key-value data to a PostgreSQL database.
 * <p>
 * Stores raw byte payloads inside a binary column ({@code BYTEA}) using standard JDBC operations.
 * </p>
 */
public class PostgresDriver implements StorageDriver {

    private final String url;
    private final String user;
    private final String password;

    /**
     * Constructs a {@code PostgresDriver} with the specified connection configuration parameters.
     * <p>
     * Automatically attempts to create the target {@code tinystore} database table if it does not already exist.
     * </p>
     *
     * @param host     the hostname or IP address of the PostgreSQL server
     * @param port     the port number of the PostgreSQL server
     * @param database the database name to connect to
     * @param user     the authentication username
     * @param password the authentication password
     * @throws RuntimeException if database table initialization fails
     */
    public PostgresDriver(String host, int port, String database, String user, String password) {
        this.url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        this.user = user;
        this.password = password;
        initTable();
    }

    private void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tinystore (key VARCHAR(255) PRIMARY KEY, val BYTEA);";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize PostgreSQL table", e);
        }
    }

    /**
     * Inserts or updates the raw byte payload associated with the given key in the PostgreSQL table.
     *
     * @param key  the unique identifier for the record
     * @param data the byte payload to persist
     * @throws RuntimeException if a database error occurs during the write operation
     */
    @Override
    public void write(String key, byte[] data) {
        String sql = "INSERT INTO tinystore (key, val) VALUES (?, ?) " +
                "ON CONFLICT (key) DO UPDATE SET val = EXCLUDED.val";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setBytes(2, data);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("PostgreSQL write failed", e);
        }
    }

    /**
     * Retrieves the byte payload associated with the given key from the PostgreSQL table.
     *
     * @param key the unique identifier of the record to read
     * @return the byte array payload, or {@code null} if the key is not found
     * @throws RuntimeException if a database error occurs during the read operation
     */
    @Override
    public byte[] read(String key) {
        String sql = "SELECT val FROM tinystore WHERE key = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("val");
            }
        } catch (SQLException e) {
            throw new RuntimeException("PostgreSQL read failed", e);
        }
        return null;
    }

    /**
     * Deletes the record associated with the given key from the PostgreSQL table.
     *
     * @param key the unique identifier of the record to remove
     * @return {@code true} if a record was deleted, {@code false} if no matching key was found
     * @throws RuntimeException if a database error occurs during the deletion operation
     */
    @Override
    public boolean delete(String key) {
        String sql = "DELETE FROM tinystore WHERE key = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, key);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("PostgreSQL delete failed", e);
        }
    }
}