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
import org.microbule.config.core.EmptyConfig;
import org.microbule.config.core.MapConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.scheduler.api.SchedulerService;
import org.microbule.scheduler.core.DefaultSchedulerService;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
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

    private SchedulerService schedulerService = new DefaultSchedulerService(1);


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
        when(configService.createServerConfig(HelloService.class, "HelloService")).thenReturn(serverConfig);

        MapConfig proxyConfig1 = new MapConfig();
        proxyConfig1.addValue("proxyAddress", ADDRESS);
        proxyConfig1.addValue("ignored.enabled", "false");

        MapConfig proxyConfig2 = new MapConfig();
        proxyConfig2.addValue("proxyAddress", ADDRESS);
        proxyConfig2.addValue("ignored.enabled", "true");
        when(configService.createProxyConfig(HelloService.class, "HelloService")).thenReturn(proxyConfig1, proxyConfig1, proxyConfig2);


        when(configService.createConfig(any())).thenReturn(EmptyConfig.INSTANCE);

        MapConfig cacheConfig = new MapConfig();
        cacheConfig.addValue("refreshDelay", "20");
        cacheConfig.addValue("refreshDelayUnit", "MILLISECONDS");

        when(configService.createConfig("JaxrsProxyFactory", "cache")).thenReturn(cacheConfig);

        when(serverDecorator.name()).thenReturn("mock");
        container.addBean(serverDecorator);
        when(ignoredServerDecorator.name()).thenReturn("ignored");
        container.addBean(ignoredServerDecorator);

        when(proxyDecorator.name()).thenReturn("mock");
        container.addBean(proxyDecorator);
        when(ignoredProxyDecorator.name()).thenReturn("ignored");
        container.addBean(ignoredProxyDecorator);

        proxyFactory = new DefaultJaxrsProxyFactory(container, configService, schedulerService);

        container.addBean(new HelloServiceImpl());

        new JaxrsServerBootstrap(container, new DefaultJaxrsServerFactory(container, configService));
        container.initialize();
        verify(serverDecorator).decorate(any(JaxrsServiceDescriptor.class), any(Config.class));
    }

    @After
    public void stopServer() {
        container.shutdown();
        proxyFactory.destroy();
    }

    @Test
    public void testCallingService() {
        final HelloService hello = proxyFactory.createProxy(HelloService.class);


        assertEquals("Hello, Microbule!", hello.sayHello("Microbule"));

        verify(proxyDecorator, atLeastOnce()).decorate(any(JaxrsServiceDescriptor.class), any(Config.class));
    }

    @Test
    public void testCallingServiceAfterRefresh() {
        final HelloService hello = proxyFactory.createProxy(HelloService.class);

        assertEquals("Hello, Microbule!", hello.sayHello("Microbule"));

        await(100);
    }
}
