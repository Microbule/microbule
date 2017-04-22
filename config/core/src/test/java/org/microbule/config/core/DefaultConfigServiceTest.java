package org.microbule.config.core;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class DefaultConfigServiceTest extends Assert {

    @Mock
    private ConfigProvider mockProvider;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProxyConfigWithNoProviderNames() {
        DefaultConfigService svc = new DefaultConfigService(Collections.emptyList(), 100, TimeUnit.MILLISECONDS);
        final Config config = svc.getProxyConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testGetServerConfigWithNoProviderNames() {
        DefaultConfigService svc = new DefaultConfigService(Collections.emptyList(),100, TimeUnit.MILLISECONDS);
        final Config config = svc.getServerConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testGetProxyConfigWithNoProviders() {
        DefaultConfigService svc = new DefaultConfigService(Lists.newArrayList("provider1", "provider2s"),100, TimeUnit.MILLISECONDS);
        final Config config = svc.getProxyConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testGetServerConfigWithNoProviders() {
        DefaultConfigService svc = new DefaultConfigService(Lists.newArrayList("provider1", "provider2"),100, TimeUnit.MILLISECONDS);
        final Config config = svc.getServerConfig(HelloService.class);
        final Optional<String> val = config.group("foo").value("bar");
        assertFalse(val.isPresent());
    }

    @Test
    public void testWithProviders() {
        DefaultConfigService svc = new DefaultConfigService(Lists.newArrayList("empty", "mock"), 1, TimeUnit.SECONDS);
        svc.registerConfigProvider("mock", mockProvider);
        svc.registerConfigProvider("empty", EmptyConfigProvider.INSTANCE);

        MapConfig expected = new MapConfig();
        expected.group("foo").addValue("bar", "baz");
        when(mockProvider.getServerConfig(HelloService.class)).thenReturn(expected);

        final Config result = svc.getServerConfig(HelloService.class);
        assertEquals("baz", result.group("foo").value("bar").get());
    }

    public interface HelloService {

    }
}