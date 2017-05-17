package org.microbule.core;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.core.MapConfig;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class DefaultJaxrsConfigBuilderTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigService configService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testBuild() {
        final DefaultJaxrsConfigBuilder<HelloService> builder = new DefaultJaxrsConfigBuilder<>(configService, HelloService.class, "hello");
        MapConfig c = new MapConfig();
        c.addValue("key", "value1");
        when(configService.createConfig("a", "b", "c")).thenReturn(c);
        MapConfig d = new MapConfig();
        d.addValue("key", "value2");
        when(configService.createConfig("a", "b", "d")).thenReturn(d);
        final Config config = builder.withPath("a", "b", "c")
                .withPath("a", "b", "d")
                .build();
        assertEquals("value1", config.value("key").get());
    }

    @Test
    public void testConstructor() {
        final DefaultJaxrsConfigBuilder<HelloService> builder = new DefaultJaxrsConfigBuilder<>(configService, HelloService.class, "hello");
        assertEquals(HelloService.class, builder.serviceInterface());
        assertEquals("hello", builder.serviceName());
    }
}