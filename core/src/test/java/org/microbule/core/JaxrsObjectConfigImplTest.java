package org.microbule.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.core.service.HelloService;

public class JaxrsObjectConfigImplTest extends Assert {

    private JaxrsObjectConfigImpl config;

    @Before
    public void initConfig() {
        final Map<String,Object> props = new HashMap<>();
        props.put("boolProp", "true");
        props.put("intProp", "12345");
        props.put("stringProp","hello");
        props.put("doubleProp", "123.45");
        config = new JaxrsObjectConfigImpl(HelloService.class, "http://localhost/hello", props);
    }

    @Test
    public void testGetMissingProperty() {
        assertNull(config.getProperty("bogus"));
    }

    @Test
    public void testGetBooleanProperty() {
        assertTrue(config.getBooleanProperty("boolProp"));
        assertFalse(config.isTrue("bogus"));
    }

    @Test
    public void testGetDoubleProperty() {
        assertEquals(123.45, config.getDoubleProperty("doubleProp"), 0.00000001);
    }

    @Test
    public void testGetIntProperty() {
        assertEquals(12345L, config.getIntProperty("intProp").longValue());
    }

    @Test
    public void testGetPropertyWithCustomTransform() {
        assertEquals(12345L, config.getProperty("intProp", Long::parseLong, 54321L).longValue());
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        assertEquals("thedefault", config.getProperty("bogus", "thedefault"));
    }
}