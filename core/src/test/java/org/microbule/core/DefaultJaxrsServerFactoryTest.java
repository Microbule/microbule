package org.microbule.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.core.providers.RequestCountClientFilter;
import org.microbule.core.providers.RequestCountContainerFilter;
import org.microbule.core.service.HelloService;
import org.microbule.core.service.HelloServiceImpl;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


public class DefaultJaxrsServerFactoryTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String BASE_ADDRESS = "http://localhost:8383/";
    @Mock
    private JaxrsProxyDecorator proxyDecorator;

    @Captor
    private ArgumentCaptor<JaxrsServiceDescriptor> descriptorCaptor;

    @Mock
    private JaxrsServerDecorator serverDecorator;

    private final RequestCountContainerFilter containerRequestCount = new RequestCountContainerFilter();
    private final RequestCountClientFilter clientRequestCount = new RequestCountClientFilter();
    private JaxrsServer server;
    private DefaultJaxrsServerFactory serverFactory;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateProxy() {
        final DefaultJaxrsProxyFactory proxyFactory = new DefaultJaxrsProxyFactory();
        proxyFactory.addDecorator("mock", proxyDecorator);
        proxyFactory.addDecorator("requestcount", (desc, config) -> desc.addProvider(clientRequestCount));
        assertEquals(2, proxyFactory.getDecoratorCount());
        assertSame(proxyDecorator, proxyFactory.getDecorator("mock"));

        MapConfig config = new MapConfig();

        config.addValue(JaxrsProxyFactory.ADDRESS_PROP, BASE_ADDRESS);

        final HelloService proxy = proxyFactory.createProxy(HelloService.class, config);

        verify(proxyDecorator).decorate(descriptorCaptor.capture(), any(Config.class));
        final JaxrsServiceDescriptor descriptor = descriptorCaptor.getValue();

        assertEquals(HelloService.class, descriptor.serviceInterface());

        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        assertEquals(1, clientRequestCount.getRequestCount().get());
        assertEquals(1, containerRequestCount.getRequestCount().get());
    }

    @Test
    public void testCreateProxyWhenDecoratorDisabled() {
        final DefaultJaxrsProxyFactory proxyFactory = new DefaultJaxrsProxyFactory();
        proxyFactory.addDecorator("mock", proxyDecorator);
        proxyFactory.addDecorator("requestcount", (desc, config) -> desc.addProvider(clientRequestCount));

        MapConfig config = new MapConfig();
        config.group("mock").addValue("enabled", "false");
        config.addValue(JaxrsProxyFactory.ADDRESS_PROP, BASE_ADDRESS);
        proxyFactory.createProxy(HelloService.class, config);
        verifyNoMoreInteractions(proxyDecorator);
    }

    @Test
    public void testCreatingServer() {
        verify(serverDecorator).decorate(descriptorCaptor.capture(), any(Config.class));
        final JaxrsServiceDescriptor descriptor = descriptorCaptor.getValue();
        assertEquals(HelloService.class, descriptor.serviceInterface());
        //assertEquals(BASE_ADDRESS, descriptor.baseAddress());
    }

    @Before
    public void startServer() {
        MockitoAnnotations.initMocks(this);
        serverFactory = new DefaultJaxrsServerFactory();
        serverFactory.addDecorator("mock", serverDecorator);
        serverFactory.addDecorator("requestcount", (desc, config) -> desc.addProvider(containerRequestCount));
        MapConfig config = new MapConfig();
        config.addValue(JaxrsServerFactory.ADDRESS_PROP, BASE_ADDRESS);
        server = serverFactory.createJaxrsServer(HelloService.class, new HelloServiceImpl(), config);
    }

    @Test
    public void testRegisterDuplicateDecorator() {
        assertFalse(serverFactory.addDecorator("mock", serverDecorator));
    }

    @After
    public void shutdownServer() {
        serverFactory.removeDecorator("mock");
        if (server != null) {
            server.shutdown();
        }
    }
}
