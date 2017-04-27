package org.microbule.config.sysprop;

import org.junit.Assert;
import org.junit.Test;

public class SystemPropertiesConfigProviderTest extends Assert {
    @Test
    public void testGetServerConfig() {
        System.setProperty("microbule.MyService.server.foo", "bar");
        final SystemPropertiesConfigProvider provider = new SystemPropertiesConfigProvider();
        assertEquals("bar", provider.getServerConfig(MyService.class).value("foo").get());
    }

    @Test
    public void testGetProxyConfig() {
        System.setProperty("microbule.MyService.proxy.foo", "bar");
        final SystemPropertiesConfigProvider provider = new SystemPropertiesConfigProvider();
        assertEquals("bar", provider.getProxyConfig(MyService.class).value("foo").get());
    }
    public interface MyService {

    }
}