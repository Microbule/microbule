package org.microbule.test.hello;

import org.junit.Test;
import org.microbule.test.JaxrsTestCase;

public abstract class HelloTestCase extends JaxrsTestCase<HelloService> {
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
