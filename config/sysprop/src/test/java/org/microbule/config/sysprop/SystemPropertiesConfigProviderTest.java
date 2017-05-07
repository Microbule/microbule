package org.microbule.config.sysprop;

import org.junit.Assert;
import org.junit.Test;

public class SystemPropertiesConfigProviderTest extends Assert {
    @Test
    public void testGetConfig() {
        System.setProperty("one.two.foo", "bar");
        final SystemPropertiesConfigProvider provider = new SystemPropertiesConfigProvider();
        assertEquals("bar", provider.getConfig("one", "two").value("foo").get());
    }
}