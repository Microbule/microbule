package org.microbule.config.core;

import org.junit.Test;
import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class DefaultConfigBuilderFactoryTest extends MockObjectTestCase  {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigProvider provider1;

    @Mock
    private ConfigProvider provider2;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testWithNoConfigProviders() {
        final MapConfig config1 = new MapConfig();
        config1.addValue("a.one", "config1.a1");
        config1.addValue("b.one", "config1.b1");
        config1.addValue("b.two", "config1.b2");
        config1.addValue("a.custom", "config1.aCustom");
        config1.addValue("b.custom", "config1.bCustom");

        when(provider1.getConfig("a")).thenReturn(config1.group("a"));
        when(provider1.getConfig("b")).thenReturn(config1.group("b"));
        when(provider1.name()).thenReturn("provider1");
        when(provider1.priority()).thenReturn(ConfigProvider.DEFAULT_PRIORITY);

        final MapConfig config2 = new MapConfig();
        config2.addValue("a.one", "config2.a1");
        config2.addValue("a.three", "config2.a3");
        config2.addValue("b.one", "config2.b1");
        config2.addValue("b.three", "config2.b3");
        config2.addValue("a.custom", "config2.aCustom");
        config2.addValue("b.custom", "config2.bCustom");

        final MapConfig custom = new MapConfig();
        custom.addValue("custom", "OVERRIDE");

        when(provider2.getConfig("a")).thenReturn(config2.group("a"));
        when(provider2.getConfig("b")).thenReturn(config2.group("b"));
        when(provider2.name()).thenReturn("provider2");
        when(provider2.priority()).thenReturn(ConfigProvider.EXTERNAL_PRIORITY);

        final SimpleBeanFinder finder = new SimpleBeanFinder();
        finder.addBean(provider1);
        finder.addBean(provider2);


        final DefaultConfigBuilderFactory configService = new DefaultConfigBuilderFactory(finder);
        finder.initialize();

        final Config config = configService.createBuilder()
                .withPath("a")
                .withPath("b")
                .withCustom(custom)
                .build();

        assertEquals("config1.a1", config.value("one").get());
        assertEquals("config1.b2", config.value("two").get());
        assertEquals("config2.a3", config.value("three").get());
        assertEquals("OVERRIDE", config.value("custom").get());
    }
}