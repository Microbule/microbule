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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.microbule.api.JaxrsConfigService;
import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DefaultJaxrsServerFactoryTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String ADDRESS = "http://localhost:8383/hello";

    @Mock
    private JaxrsProxyDecorator proxyDecorator;

    @Mock
    private JaxrsServerDecorator serverDecorator;

    @Mock
    private JaxrsServerDecorator ignoredServerDecorator;
    
    @Mock
    private JaxrsProxyDecorator ignoredProxyDecorator;
    
    @Mock
    private JaxrsConfigService configService;

    private DefaultJaxrsProxyFactory proxyFactory;
    private SimpleContainer container;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void startServer() {
        container = new SimpleContainer();

        MapConfig serverConfig = new MapConfig();
        serverConfig.addValue("serverAddress", ADDRESS);
        serverConfig.addValue("ignored.enabled", "false");
        when(configService.createServerConfig(HelloService.class)).thenReturn(serverConfig);

        MapConfig proxyConfig = new MapConfig();
        proxyConfig.addValue("proxyAddress", ADDRESS);
        proxyConfig.addValue("ignored.enabled", "false");
        when(configService.createProxyConfig(HelloService.class)).thenReturn(proxyConfig);

        when(serverDecorator.name()).thenReturn("mock");
        container.addBean(serverDecorator);
        when(ignoredServerDecorator.name()).thenReturn("ignored");
        container.addBean(ignoredServerDecorator);

        when(proxyDecorator.name()).thenReturn("mock");
        container.addBean(proxyDecorator);
        when(ignoredProxyDecorator.name()).thenReturn("ignored");
        container.addBean(ignoredProxyDecorator);

        proxyFactory = new DefaultJaxrsProxyFactory(container, configService);

        container.addBean(new HelloServiceImpl());

        new JaxrsServerBootstrap(container, new DefaultJaxrsServerFactory(container, configService));
        container.initialize();
        verify(serverDecorator).decorate(any(JaxrsServiceDescriptor.class), any(Config.class));
    }

    @After
    public void stopServer() {
        container.shutdown();
    }

    @Test
    public void testCallingService() {
        final HelloService hello = proxyFactory.createProxy(HelloService.class);




        assertEquals("Hello, Microbule!", hello.sayHello("Microbule"));

        verify(proxyDecorator).decorate(any(JaxrsServiceDescriptor.class), any(Config.class));

    }
}
