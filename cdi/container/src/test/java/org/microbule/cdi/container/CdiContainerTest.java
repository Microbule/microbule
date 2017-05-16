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

package org.microbule.cdi.container;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.junit.Test;
import org.microbule.api.JaxrsProxyReference;
import org.microbule.test.cdi.CdiTestCase;
import org.microbule.test.core.hello.HelloService;

@Singleton
public class CdiContainerTest extends CdiTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    private Consumer consumer;

    @Inject
    private JaxrsProxyReference<HelloService> helloProxy;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testInjectingProxy() {
        assertNotNull(helloProxy);
    }

    @Test
    public void testPlugins() {
        assertEquals(1, consumer.getWidgets().size());
    }

    @Test
    public void testServers() {
        assertEquals(1, consumer.getServerDefinitions().size());
    }
}