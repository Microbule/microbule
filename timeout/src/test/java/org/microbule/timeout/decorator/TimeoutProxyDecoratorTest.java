package org.microbule.timeout.decorator;

import javax.ws.rs.ProcessingException;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.core.MapConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.JaxrsServerTestCase;

public class TimeoutProxyDecoratorTest extends JaxrsServerTestCase<DelayResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new TimeoutProxyDecorator());
    }

    @Override
    protected MapConfig createConfig() {
        final MapConfig config = super.createConfig();
        config.addValue("timeout.connectionTimeout", "100");
        config.addValue("timeout.receiveTimeout", "500");
        return config;
    }

    @Override
    protected DelayResource createImplementation() {
        return new DelayResourceImpl();
    }

    @Test(expected = ProcessingException.class)
    public void testConnectionTimeoutExpired() {
        final DelayResource proxy = createProxy();
        proxy.delay(3000);
    }

    @Test
    public void testConnectionTimeout() {
        final DelayResource proxy = createProxy();
        Assert.assertEquals("100", proxy.delay(100));
    }
}