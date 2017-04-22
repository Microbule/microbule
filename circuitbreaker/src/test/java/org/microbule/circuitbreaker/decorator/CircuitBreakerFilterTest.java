package org.microbule.circuitbreaker.decorator;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ServiceUnavailableException;

import org.junit.Test;
import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.test.hello.HelloService;
import org.microbule.test.hello.HelloServiceImpl;
import org.microbule.test.hello.HelloTestCase;

public class CircuitBreakerFilterTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(DefaultJaxrsProxyFactory factory) {
        factory.addDecorator("circuitbreaker", new CircuitBreakerDecorator());
    }

    @Override
    protected HelloService createImplementation() {
        return new EvilHelloService();
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testOpeningCircuitBreaker() {
        final HelloService proxy = createProxy();
        for (int i = 0; i < 20; ++i) {
            try {
                proxy.sayHello("Dr. Evil");
            } catch (InternalServerErrorException e) {
                // Ignore!
            }
        }
        proxy.sayHello("Microbule");
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class EvilHelloService extends HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public String sayHello(String name) {
            if ("Dr. Evil".equals(name)) {
                throw new InternalServerErrorException("I don't like you!");
            }
            return super.sayHello(name);
        }
    }
}