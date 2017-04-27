package org.microbule.config.core;

import org.junit.Assert;
import org.junit.Test;

public class CompositeConfigTest extends Assert {
    @Test
    public void testValue() {
        MapConfig config1 = new MapConfig();
        config1.addValue("foo", "bar");
        MapConfig config2 = new MapConfig();
        config2.addValue("foo", "baz");
        config2.addValue("hello", "world");

        final CompositeConfig config = new CompositeConfig(config1, config2);
        assertEquals("bar", config.value("foo").get());
        assertEquals("world", config.value("hello").get());
    }
}