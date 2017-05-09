package org.microbule.tracer.decorator;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.hello.HelloService;
import org.microbule.test.server.hello.HelloTestCase;
import org.slf4j.MDC;

public class TracerProxyDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new TracerProxyDecorator());
    }

    @Test
    public void testWithCustomTraceId() {
        final HelloService proxy = createProxy();
        MDC.put(TracerConstants.TRACE_ID_KEY, "foobarbaz");
        Assert.assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        MDC.remove(TracerConstants.TRACE_ID_KEY);
    }
}