package org.microbule.config.core;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class PropertiesConfigTest extends Assert {

    @Test
    public void testValue() {
        final PropertiesConfig config = new PropertiesConfig();
        config.addValue("foo", "bar");
        assertEquals("bar", config.value("foo").get());
    }

    @Test
    public void testValueWithCustomSeparator() {
        Properties props = new Properties();
        props.setProperty("foo/bar", "baz");
        final PropertiesConfig config = new PropertiesConfig(props,"/");
        assertEquals("baz", config.group("foo").value("bar").get());
    }

    @Test
    public void testValueWithPropertiesConstructor() {
        Properties props = new Properties();
        props.setProperty("foo", "bar");
        final PropertiesConfig config = new PropertiesConfig(props);
        assertEquals("bar", config.value("foo").get());
    }

}