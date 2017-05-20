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

package org.microbule.util.proxy;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;

public class DynamicProxyUtilsTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateProxy() {
        final HelloService proxy = DynamicProxyUtils.createProxy(HelloService.class, HelloServiceImpl::new, "foo");
        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
    }

    @Test
    public void testEquals() {
        final HelloService proxy1 = DynamicProxyUtils.createProxy(HelloService.class, HelloServiceImpl::new, "foo");
        final HelloService proxy2 = DynamicProxyUtils.createProxy(HelloService.class, HelloServiceImpl::new, "foo");
        assertEquals(proxy1, proxy1);
        assertNotEquals(proxy1, proxy2);
    }

    @Test
    public void testHashCode() {
        final HelloService proxy = DynamicProxyUtils.createProxy(HelloService.class, HelloServiceImpl::new, "foo");
        assertEquals(System.identityHashCode(proxy), proxy.hashCode());
    }

    @Test
    public void testIsHashCode() throws Exception {
        assertFalse(DynamicProxyUtils.isHashCodeMethod(BadMethods.class.getMethod("equals")));
        assertFalse(DynamicProxyUtils.isHashCodeMethod(BadMethods.class.getMethod("hashCode", String.class)));
    }

    @Test
    public void testIsToString() throws Exception {
        assertFalse(DynamicProxyUtils.isToStringMethod(BadMethods.class.getMethod("equals")));
        assertFalse(DynamicProxyUtils.isToStringMethod(BadMethods.class.getMethod("toString", String.class)));
    }

    @Test
    public void testIsEquals() throws Exception {
        assertFalse(DynamicProxyUtils.isEqualsMethod(BadMethods.class.getMethod("toString", String.class)));
        assertFalse(DynamicProxyUtils.isEqualsMethod(BadMethods.class.getMethod("equals")));
        assertFalse(DynamicProxyUtils.isEqualsMethod(BadMethods.class.getMethod("equals",String.class)));
    }

    @Test
    public void testToString() {
        final HelloService proxy = DynamicProxyUtils.createProxy(HelloService.class, HelloServiceImpl::new, "foo");
        assertEquals("foo", proxy.toString());
    }

    @Test
    public void testIsUtilsClass() throws Exception {
        assertIsUtilsClass(DynamicProxyUtils.class);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testWithException() {
        final HelloService proxy = DynamicProxyUtils.createProxy(HelloService.class, () -> new HelloService() {
            @Override
            public String sayHello(String name) {
                throw new IllegalArgumentException(name);
            }

            @Override
            public String version() {
                return "1.0";
            }
        }, "foo");
        proxy.sayHello("Microbule");
    }

    interface BadMethods {
        boolean equals(String str);

        boolean equals();

        int hashCode(String str);

        String toString(String str);
    }

}