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
import org.microbule.config.api.ConfigBuilder;
import org.microbule.config.api.ConfigBuilderFactory;
import org.microbule.config.core.EmptyConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DefaultJaxrsConfigServiceTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigBuilder configBuilder;

    @Mock
    private ConfigBuilderFactory configBuilderFactory;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateProxyConfig() {
        SimpleContainer container = new SimpleContainer();
        final DefaultJaxrsConfigService service = new DefaultJaxrsConfigService(container, configBuilderFactory);

        final Config config = service.createProxyConfig(HelloService.class, "HelloService");
        assertSame(EmptyConfig.INSTANCE, config);
    }

    @Test
    public void testCreateServerConfig() {
        SimpleContainer container = new SimpleContainer();
        final DefaultJaxrsConfigService service = new DefaultJaxrsConfigService(container, configBuilderFactory);

        final Config config = service.createServerConfig(HelloService.class, "HelloService");
        assertSame(EmptyConfig.INSTANCE, config);
    }

    @Before
    public void trainMock() {
        when(configBuilder.withPath(any())).thenReturn(configBuilder);
        when(configBuilderFactory.createBuilder()).thenReturn(configBuilder);
        when(configBuilder.build()).thenReturn(EmptyConfig.INSTANCE);
    }
}