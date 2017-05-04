package org.microbule.config.core;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class DefaultConfigServiceTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigProvider mockProvider;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(mockProvider.name()).thenReturn("mock");
    }

    @Test
    public void testGetProxyConfigWithNoProviderNames() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        finder.start();
        DefaultConfigService svc = new DefaultConfigService(finder, Collections.emptyList(), Collections.emptyList(), 100, TimeUnit.MILLISECONDS);
        final Config config = svc.getProxyConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testGetProxyConfigWithNoProviders() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        finder.start();
        DefaultConfigService svc = new DefaultConfigService(finder, Collections.emptyList(), Lists.newArrayList("provider1", "provider2s"),100, TimeUnit.MILLISECONDS);
        final Config config = svc.getProxyConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testGetServerConfigWithNoProviderNames() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        finder.start();
        DefaultConfigService svc = new DefaultConfigService(finder, Collections.emptyList(), Collections.emptyList(),100, TimeUnit.MILLISECONDS);
        final Config config = svc.getServerConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testGetServerConfigWithNoProviders() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        DefaultConfigService svc = new DefaultConfigService(finder, Collections.emptyList(), Lists.newArrayList("provider1", "provider2"),100, TimeUnit.MILLISECONDS);
        final Config config = svc.getServerConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testWithProviders() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        finder.addBean(mockProvider);
        finder.addBean(EmptyConfigProvider.INSTANCE);
        DefaultConfigService svc = new DefaultConfigService(finder, Collections.emptyList(), Lists.newArrayList("empty", "mock"), 1, TimeUnit.SECONDS);
        finder.start();
        MapConfig expected = new MapConfig();
        expected.group("foo").addValue("bar", "baz");
        when(mockProvider.getServerConfig(HelloService.class)).thenReturn(expected);

        final Config result = svc.getServerConfig(HelloService.class);
        assertEquals("baz", result.group("foo").value("bar").get());
    }

    @Test
    public void testWithProvidersCsvConstructor() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        finder.addBean(mockProvider);
        finder.addBean(EmptyConfigProvider.INSTANCE);
        DefaultConfigService svc = new DefaultConfigService(finder, null, "empty,mock", 1, TimeUnit.SECONDS);
        finder.start();
        MapConfig expected = new MapConfig();
        expected.group("foo").addValue("bar", "baz");
        when(mockProvider.getServerConfig(HelloService.class)).thenReturn(expected);

        final Config result = svc.getServerConfig(HelloService.class);
        assertEquals("baz", result.group("foo").value("bar").get());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public interface HelloService {
    }
}