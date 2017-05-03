package org.microbule.circuitbreaker.decorator;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ServiceUnavailableException;

import org.junit.Test;
import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.test.server.hello.HelloService;
import org.microbule.test.server.hello.HelloServiceImpl;
import org.microbule.test.server.hello.HelloTestCase;

public class CircuitBreakerFilterTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleBeanFinder finder) {
        finder.addBean(new CircuitBreakerDecorator());
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