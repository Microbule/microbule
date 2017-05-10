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

import org.junit.Assert;


public class DefaultJaxrsServerFactoryTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

//    public static final String BASE_ADDRESS = "http://localhost:8383/";
//
//    @Mock
//    private JaxrsProxyDecorator proxyDecorator;
//
//    @Captor
//    private ArgumentCaptor<JaxrsServiceDescriptor> descriptorCaptor;
//
//    @Mock
//    private JaxrsServerDecorator serverDecorator;
//
//    @Mock
//    private BeanDiscoveryService beanDiscoveryService;
//
//    @Captor
//    private ArgumentCaptor<BeanDiscoveryListener<JaxrsServerDecorator>> decoratorListenerCaptor;
//
//
//    private final RequestCountContainerFilter containerRequestCount = new RequestCountContainerFilter();
//    private final RequestCountClientFilter clientRequestCount = new RequestCountClientFilter();
//    private JaxrsServer server;
//    private DefaultJaxrsServerFactory serverFactory;
//
////----------------------------------------------------------------------------------------------------------------------
//// Other Methods
////----------------------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void testCreateProxy() {
//        final DefaultJaxrsProxyFactory proxyFactory = new DefaultJaxrsProxyFactory(beanDiscoveryService);
//        verify(beanDiscoveryService).discoverBeans(eq(JaxrsProxyDecorator.class), decoratorListenerCaptor.capture());
//
//        final BeanDiscoveryListener<JaxrsServerDecorator> listener = decoratorListenerCaptor.getValue();
//        listener.onBeanFound(mock);
//        listener.onBeanFound(new )
//        proxyFactory.addDecorator("mock", mock);
//        proxyFactory.addDecorator("requestcount", (desc, customConfig) -> desc.addProvider(clientRequestCount));
//        assertEquals(2, proxyFactory.getDecoratorCount());
//        assertSame(mock, proxyFactory.getDecorator("mock"));
//
//        MapConfig customConfig = new MapConfig();
//
//        customConfig.addValue(JaxrsProxyFactory.ADDRESS_PROP, BASE_ADDRESS);
//
//        final HelloService proxy = proxyFactory.createProxy(HelloService.class, customConfig);
//
//        verify(mock).decorate(descriptorCaptor.capture(), any(Config.class));
//        final JaxrsServiceDescriptor descriptor = descriptorCaptor.getValue();
//
//        assertEquals(HelloService.class, descriptor.serviceInterface());
//
//        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
//        assertEquals(1, clientRequestCount.getRequestCount().get());
//        assertEquals(1, containerRequestCount.getRequestCount().get());
//    }
//
//    @Test
//    public void testCreateProxyWhenDecoratorDisabled() {
//        final DefaultJaxrsProxyFactory proxyFactory = new DefaultJaxrsProxyFactory();
//        proxyFactory.addDecorator("mock", mock);
//        proxyFactory.addDecorator("requestcount", (desc, customConfig) -> desc.addProvider(clientRequestCount));
//
//        MapConfig customConfig = new MapConfig();
//        customConfig.group("mock").addValue("enabled", "false");
//        customConfig.addValue(JaxrsProxyFactory.ADDRESS_PROP, BASE_ADDRESS);
//        proxyFactory.createProxy(HelloService.class, customConfig);
//        verifyNoMoreInteractions(mock);
//    }
//
//    @Test
//    public void testCreatingServer() {
//        verify(serverDecorator).decorate(descriptorCaptor.capture(), any(Config.class));
//        final JaxrsServiceDescriptor descriptor = descriptorCaptor.getValue();
//        assertEquals(HelloService.class, descriptor.serviceInterface());
//    }
//
//    @Before
//    public void startServer() {
//        MockitoAnnotations.initMocks(this);
//        serverFactory = new DefaultJaxrsServerFactory();
//        serverFactory.addDecorator(serverDecorator);
//        serverFactory.addDecorator(new JaxrsServerDecorator() {
//            @Override
//            public String name() {
//                return "requestcount";
//            }
//
//            @Override
//            public void decorate(JaxrsServiceDescriptor descriptor, Config customConfig) {
//                descriptor.addProvider(containerRequestCount);
//            }
//        });
//        MapConfig customConfig = new MapConfig();
//        customConfig.addValue(JaxrsServerFactory.ADDRESS_PROP, BASE_ADDRESS);
//        server = serverFactory.createJaxrsServer(HelloService.class, new HelloServiceImpl(), customConfig);
//    }
//
//    @Test
//    public void testRegisterDuplicateDecorator() {
//        assertFalse(serverFactory.addDecorator(serverDecorator));
//    }
//
//    @After
//    public void shutdownServer() {
//        serverFactory.removeDecorator("mock");
//        if (server != null) {
//            server.shutdown();
//        }
//    }
}
