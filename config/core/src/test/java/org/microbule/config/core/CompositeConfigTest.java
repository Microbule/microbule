package org.microbule.config.core;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.api.Config;

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

    @Test
    public void testGroup() {
        MapConfig config1 = new MapConfig();
        config1.addValue("a.foo", "bar");
        MapConfig config2 = new MapConfig();
        config2.addValue("a.foo", "baz");
        config2.addValue("a.hello", "world");

        final Config config = new CompositeConfig(config1, config2).group("a");
        assertEquals("bar", config.value("foo").get());
        assertEquals("world", config.value("hello").get());
    }
}