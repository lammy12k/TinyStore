package com.kyfstore.tinystore;

import java.io.Serializable;

/**
 * Encapsulates a raw serialized data payload alongside optional Time-To-Live (TTL) metadata.
 * <p>
 * Serves as the internal container persisted by storage drivers to maintain consistent expiry tracking
 * regardless of the underlying backend capabilities.
 * </p>
 */
public class RecordEnvelope implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] payload;

    /**
     * Absolute timestamp in milliseconds at which this record expires,
     * or {@code null} if the record does not expire.
     */
    private final Long expireAt;

    /**
     * Constructs a {@code RecordEnvelope} containing the payload and calculated expiration timestamp.
     *
     * @param payload   the raw serialized payload bytes
     * @param ttlMillis the duration in milliseconds before the record expires, or {@code null} for no expiration
     */
    public RecordEnvelope(byte[] payload, Long ttlMillis) {
        this.payload = payload;
        this.expireAt = (ttlMillis != null) ? System.currentTimeMillis() + ttlMillis : null;
    }

    /**
     * Checks whether the record has passed its expiration time based on system clock time.
     *
     * @return {@code true} if an expiration timestamp is set and current time exceeds it; {@code false} otherwise
     */
    public boolean isExpired() {
        return expireAt != null && System.currentTimeMillis() > expireAt;
    }

    /**
     * Retrieves the raw byte payload held within the envelope.
     *
     * @return the byte array payload
     */
    public byte[] getPayload() {
        return payload;
    }
}