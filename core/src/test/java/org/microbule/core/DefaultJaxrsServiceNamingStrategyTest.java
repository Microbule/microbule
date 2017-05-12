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

import javax.ws.rs.Path;

import org.junit.Test;
import org.microbule.annotation.JaxrsService;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;

public class DefaultJaxrsServiceNamingStrategyTest extends MockObjectTestCase {

    @Test
    public void testFallbackNaming() {
        final DefaultJaxrsServiceNamingStrategy namingStrategy = new DefaultJaxrsServiceNamingStrategy();
        assertEquals("HelloService", namingStrategy.serviceName(HelloService.class));
        assertEquals("UnnamedService", namingStrategy.serviceName(UnnamedService.class));
    }


    @Test
    public void testWithSpecifiedName() {
        final DefaultJaxrsServiceNamingStrategy namingStrategy = new DefaultJaxrsServiceNamingStrategy();
        assertEquals("coolService", namingStrategy.serviceName(MyCoolService.class));
    }

    @Path("/")
    @JaxrsService(name = "coolService")
    public interface MyCoolService {

    }

    @Path("/")
    @JaxrsService
    public interface UnnamedService {

    }
}