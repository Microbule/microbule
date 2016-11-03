package org.microbule.core;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.api.JaxrsServer;
import org.microbule.core.providers.RequestCountClientFilter;
import org.microbule.core.providers.RequestCountContainerFilter;
import org.microbule.core.service.HelloService;
import org.microbule.core.service.HelloServiceImpl;
import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


public class JaxrsServerFactoryImplTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String BASE_ADDRESS = "http://localhost:8383/";
    @Mock
    private JaxrsProxyDecorator proxyDecorator;

    @Captor
    private ArgumentCaptor<JaxrsProxyConfig> proxyConfigCaptor;

    @Mock
    private JaxrsServerDecorator serverDecorator;

    @Captor
    private ArgumentCaptor<JaxrsServerConfig> serverConfigCaptor;
    private final RequestCountContainerFilter containerRequestCount = new RequestCountContainerFilter();
    private final RequestCountClientFilter clientRequestCount = new RequestCountClientFilter();
    private JaxrsServer server;
    private JaxrsServerFactoryImpl serverFactory;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateProxy() {
        final JaxrsProxyFactoryImpl proxyFactory = new JaxrsProxyFactoryImpl();
        proxyFactory.addDecorator("mock", proxyDecorator);
        proxyFactory.addDecorator("requestcount", config -> config.addProvider(clientRequestCount));

        final HelloService proxy = proxyFactory.createProxy(HelloService.class, BASE_ADDRESS, new HashMap<>());

        verify(proxyDecorator).decorate(proxyConfigCaptor.capture());
        final JaxrsProxyConfig proxyConfig = proxyConfigCaptor.getValue();

        assertEquals(HelloService.class, proxyConfig.getServiceInterface());
        assertEquals(BASE_ADDRESS, proxyConfig.getBaseAddress());

        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        assertEquals(1, clientRequestCount.getRequestCount().get());
        assertEquals(1, containerRequestCount.getRequestCount().get());
    }

    @Test
    public void testCreatingServer() {
        verify(serverDecorator).decorate(serverConfigCaptor.capture());
        final JaxrsServerConfig serverConfig = serverConfigCaptor.getValue();
        assertEquals(HelloService.class, serverConfig.getServiceInterface());
        assertEquals(BASE_ADDRESS, serverConfig.getBaseAddress());
    }



    @Before
    public void startServer() {
        MockitoAnnotations.initMocks(this);
        serverFactory = new JaxrsServerFactoryImpl();
        serverFactory.addDecorator("mock", serverDecorator);
        serverFactory.addDecorator("requestcount", config -> config.addProvider(containerRequestCount));
        server = serverFactory.createJaxrsServer(HelloService.class, new HelloServiceImpl(), BASE_ADDRESS, new HashMap<>());
    }

    @After
    public void shutdownServer() {
        serverFactory.removeDecorator("mock");
        if(server != null) {
            server.shutdown();
        }
    }
}
