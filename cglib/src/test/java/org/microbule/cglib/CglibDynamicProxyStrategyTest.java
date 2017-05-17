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

package org.microbule.cglib;

import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;

public class CglibDynamicProxyStrategyTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testEquals() {
        final CglibDynamicProxyStrategy strategy = new CglibDynamicProxyStrategy(new SimpleContainer());
        final HelloService proxy1 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        final HelloService proxy2 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        assertEquals(proxy1, proxy1);
        assertNotEquals(proxy1, proxy2);
    }


    @Test
    public void testToString() {
        final CglibDynamicProxyStrategy strategy = new CglibDynamicProxyStrategy(new SimpleContainer());
        final HelloService proxy1 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        assertEquals("my description", proxy1.toString());
    }

    @Test
    public void testClassCaching() {
        final CglibDynamicProxyStrategy strategy = new CglibDynamicProxyStrategy(new SimpleContainer());
        final HelloService proxy1 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        final HelloService proxy2 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        assertEquals(proxy1.getClass(), proxy2.getClass());
    }

    @Test
    public void testHashCode() {
        final CglibDynamicProxyStrategy strategy = new CglibDynamicProxyStrategy(new SimpleContainer());
        final HelloService proxy1 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        assertEquals(System.identityHashCode(proxy1), proxy1.hashCode());
    }

    @Test
    public void testRegularMethodCall() {
        final CglibDynamicProxyStrategy strategy = new CglibDynamicProxyStrategy(new SimpleContainer());
        final HelloService proxy1 = strategy.createDynamicProxy(HelloService.class, HelloServiceImpl::new, "my description");
        assertEquals("Hello, Microbule!", proxy1.sayHello("Microbule"));
    }
}