package org.microbule.test.server.hello;

import org.junit.Test;
import org.microbule.test.server.JaxrsServerTestCase;

public abstract class HelloTestCase extends JaxrsServerTestCase<HelloService> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected HelloService createImplementation() {
        return new HelloServiceImpl();
    }

    @Test
    public void testSayHello() {
        assertEquals("Hello, Microbule!", createProxy().sayHello("Microbule"));
    }
}
