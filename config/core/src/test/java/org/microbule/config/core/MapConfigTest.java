package org.microbule.config.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MapConfigTest extends Assert {
    @Test
    public void testValue() {
        Map<String,String> values = new HashMap<>();
        values.put("foo", "bar");
        final MapConfig config = new MapConfig(values);
        assertEquals("bar", config.value("foo").get());
    }

    @Test
    public void testWithCustomSeparator() {
        Map<String,String> values = new HashMap<>();
        values.put("foo/bar", "baz");
        final MapConfig config = new MapConfig(values, "/");
        assertEquals("baz", config.group("foo").value("bar").get());
    }
}