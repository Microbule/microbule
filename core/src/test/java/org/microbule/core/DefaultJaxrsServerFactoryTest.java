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
//        proxyFactory.addDecorator("requestcount", (desc, config) -> desc.addProvider(clientRequestCount));
//        assertEquals(2, proxyFactory.getDecoratorCount());
//        assertSame(mock, proxyFactory.getDecorator("mock"));
//
//        MapConfig config = new MapConfig();
//
//        config.addValue(JaxrsProxyFactory.ADDRESS_PROP, BASE_ADDRESS);
//
//        final HelloService proxy = proxyFactory.createProxy(HelloService.class, config);
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
//        proxyFactory.addDecorator("requestcount", (desc, config) -> desc.addProvider(clientRequestCount));
//
//        MapConfig config = new MapConfig();
//        config.group("mock").addValue("enabled", "false");
//        config.addValue(JaxrsProxyFactory.ADDRESS_PROP, BASE_ADDRESS);
//        proxyFactory.createProxy(HelloService.class, config);
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
//            public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
//                descriptor.addProvider(containerRequestCount);
//            }
//        });
//        MapConfig config = new MapConfig();
//        config.addValue(JaxrsServerFactory.ADDRESS_PROP, BASE_ADDRESS);
//        server = serverFactory.createJaxrsServer(HelloService.class, new HelloServiceImpl(), config);
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
