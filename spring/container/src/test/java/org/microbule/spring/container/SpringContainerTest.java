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

package org.microbule.spring.container;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.microbule.api.JaxrsProxyReference;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MicrobuleConfig.class)
public class SpringContainerTest extends MockObjectTestCase {

    @Autowired
    private Collector collector;

    @Autowired
    private JaxrsProxyReference<HelloService> helloService;

    @Test
    public void testPlugins() {
        assertEquals(2, collector.getPlugins().size());
    }

    @Test
    public void testServers() {
        assertEquals(1, collector.getServerDefinitions().size());
    }

    @Test
    public void testRef() {
        assertNotNull(helloService);
    }
}