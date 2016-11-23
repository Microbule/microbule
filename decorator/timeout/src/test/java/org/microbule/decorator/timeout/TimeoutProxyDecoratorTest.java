package org.microbule.decorator.timeout;

import javax.ws.rs.ProcessingException;

import org.junit.Test;
import org.microbule.core.JaxrsProxyFactoryImpl;
import org.microbule.test.JaxrsTestCase;

public class TimeoutProxyDecoratorTest extends JaxrsTestCase<DelayResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(JaxrsProxyFactoryImpl factory) {
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
        assertEquals("100", proxy.delay(100));
    }
}