package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.ron.ContainerTest;
import com.fasterxml.jackson.dataformat.ron.databind.RONMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapReaderTest extends ContainerTest {

    public static final MapType MAP_STRING_INTEGER = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);

    private static <K,V> Map<K, V> mapOf(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private static <K,V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @Override
    public void testEmpty() throws IOException {
        String ron = "{}";
        Map<String, Object> map = new RONMapper().readValue(ron, Map.class);
        assertTrue(map.isEmpty());
    }

    @Override
    public void testOne() throws IOException {
        String ron = "{\"foo\":1}";
        Map<String, Integer> map = new RONMapper().readValue(ron, MAP_STRING_INTEGER);
        assertEquals(mapOf("foo", 1), map);
    }

    @Override
    public void testMultiple() throws IOException {
        String ron = "{\"foo\":1,\"bar\":2}";
        Map<String, Object> map = new RONMapper().readValue(ron, MAP_STRING_INTEGER);
        assertEquals(mapOf("foo", 1, "bar", 2), map);
    }
}
