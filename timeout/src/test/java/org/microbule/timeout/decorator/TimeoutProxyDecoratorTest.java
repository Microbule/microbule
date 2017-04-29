package org.microbule.timeout.decorator;

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.ws.rs.ProcessingException;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.test.server.JaxrsServerTestCase;

public class TimeoutProxyDecoratorTest extends JaxrsServerTestCase<DelayResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(DefaultJaxrsProxyFactory factory) {
        factory.addDecorator("timeout", new TimeoutProxyDecorator(100, 500));
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

    @Test
    public void testWithServiceLoader() {
        ServiceLoader<JaxrsProxyDecorator> loader  = ServiceLoader.load(JaxrsProxyDecorator.class);
        final Iterator<JaxrsProxyDecorator> i = loader.iterator();
        assertTrue(i.hasNext());
        assertTrue(i.next() instanceof TimeoutProxyDecorator);
    }
}