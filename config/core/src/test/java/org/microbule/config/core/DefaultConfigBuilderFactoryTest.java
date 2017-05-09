package org.microbule.config.core;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class DefaultConfigBuilderFactoryTest extends MockObjectTestCase  {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigProvider provider1;

    @Mock
    private ConfigProvider provider2;

    @Mock
    private MicrobuleContainer container;

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


        when(container.pluginSortedSet(same(ConfigProvider.class), any())).thenAnswer(invocation -> {
            SortedSet<ConfigProvider> set = new ConcurrentSkipListSet<>((Comparator<ConfigProvider>)invocation.getArgument(1));
            set.add(provider1);
            set.add(provider2);
            return set;
        });

        final DefaultConfigBuilderFactory configService = new DefaultConfigBuilderFactory(container);



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