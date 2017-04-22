package org.microbule.timeout.decorator;

import javax.ws.rs.ProcessingException;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.test.JaxrsTestCase;

public class TimeoutProxyDecoratorTest extends JaxrsTestCase<DelayResource> {
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
}