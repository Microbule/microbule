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

import org.junit.Before;
import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.core.CompositeConfig;
import org.microbule.config.core.EmptyConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultJaxrsConfigServiceTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigService configService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateProxyConfig() {
        SimpleContainer container = new SimpleContainer();
        final DefaultJaxrsConfigService service = new DefaultJaxrsConfigService(container, configService);

        final Config config = service.createProxyConfig(HelloService.class, "hello");
        assertTrue(config instanceof CompositeConfig);
        final InOrder inOrder = inOrder(configService);
        inOrder.verify(configService).createConfig("hello", "proxy");
        inOrder.verify(configService).createConfig("hello");
        inOrder.verify(configService).createConfig("default", "proxy");
    }

    @Test
    public void testCreateServerConfig() {
        SimpleContainer container = new SimpleContainer();
        final DefaultJaxrsConfigService service = new DefaultJaxrsConfigService(container, configService);

        final Config config = service.createServerConfig(HelloService.class, "hello");
        final InOrder inOrder = inOrder(configService);
        inOrder.verify(configService).createConfig("hello", "server");
        inOrder.verify(configService).createConfig("hello");
        inOrder.verify(configService).createConfig("default", "server");

    }

    @Test
    public void testCreateConfig() {
        SimpleContainer container = new SimpleContainer();
        final DefaultJaxrsConfigService service = new DefaultJaxrsConfigService(container, configService);
        final Config config = service.createConfig("one", "two", "three");
        verify(configService).createConfig("one", "two", "three");
    }

    @Before
    public void trainMock() {
        when(configService.createConfig(any())).thenReturn(EmptyConfig.INSTANCE);
    }
}