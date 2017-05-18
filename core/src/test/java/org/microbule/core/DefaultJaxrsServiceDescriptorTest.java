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

package org.microbule.core;

import org.apache.cxf.feature.LoggingFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.test.core.hello.HelloService;

public class DefaultJaxrsServiceDescriptorTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private DefaultJaxrsServiceDescriptor descriptor;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initConfig() {
        descriptor = new DefaultJaxrsServiceDescriptor(HelloService.class, "hello");
    }

    @Test
    public void testAddFeature() {
        descriptor.addFeature(new LoggingFeature());
        assertEquals(1, descriptor.getFeatures().size());
    }

    @Test
    public void testAddProvider() {
        descriptor.addProvider(new Object());
        assertEquals(1, descriptor.getProviders().size());
    }

    @Test
    public void testServiceInterface() {
        assertEquals(HelloService.class, descriptor.serviceInterface());
    }

    @Test
    public void testServiceName() {
        assertEquals("hello", descriptor.serviceName());
    }
}