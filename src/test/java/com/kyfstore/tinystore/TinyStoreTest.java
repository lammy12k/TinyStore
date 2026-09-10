package com.kyfstore.tinystore;

import com.kyfstore.tinystore.codec.JsonCodec;
import com.kyfstore.tinystore.compression.LzmaCompressor;
import com.kyfstore.tinystore.driver.InMemoryDriver;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TinyStoreTest {

    static class UserData {
        public String name;
        public int age;

        public UserData() {}
        public UserData(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    void testPutAndGet() {
        TinyStore store = new TinyStore.Builder()
                .setDriver(new InMemoryDriver())
                .setCodec(new JsonCodec())
                .setCompressor(new LzmaCompressor())
                .build();

        UserData original = new UserData("Alice", 30);
        store.put("user:1", original, null);

        UserData retrieved = store.get("user:1", UserData.class);
        assertNotNull(retrieved);
        assertEquals("Alice", retrieved.name);
        assertEquals(30, retrieved.age);
    }

    @Test
    void testTtlExpiration() throws InterruptedException {
        TinyStore store = new TinyStore.Builder()
                .setDriver(new InMemoryDriver())
                .setCodec(new JsonCodec())
                .setCompressor(new LzmaCompressor())
                .build();

        store.put("temp:key", "Value", Duration.ofMillis(100));
        assertNotNull(store.get("temp:key", String.class));

        Thread.sleep(150);
        assertNull(store.get("temp:key", String.class));
    }
}