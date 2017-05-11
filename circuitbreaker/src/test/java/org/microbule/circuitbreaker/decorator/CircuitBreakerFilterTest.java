/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.circuitbreaker.decorator;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ServiceUnavailableException;

import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;
import org.microbule.test.server.hello.HelloTestCase;

public class CircuitBreakerFilterTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new CircuitBreakerDecorator());
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