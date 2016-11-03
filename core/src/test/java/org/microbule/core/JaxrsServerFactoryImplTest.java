package org.microbule.core;

import org.junit.Test;
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

import static org.mockito.Mockito.verify;


public class JaxrsServerFactoryImplTest extends JaxrsTestCase<HelloService> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

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

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected HelloService createImplementation() {
        return new HelloServiceImpl();
    }

    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        factory.addDecorator("mock", serverDecorator);
        factory.addDecorator("requestcount", config -> config.addProvider(containerRequestCount));
    }

    @Override
    protected void addDecorators(JaxrsProxyFactoryImpl factory) {
        factory.addDecorator("mock", proxyDecorator);
        factory.addDecorator("requestcount", config -> config.addProvider(clientRequestCount));
    }

    @Test
    public void testCreatingServer() {
        verify(serverDecorator).decorate(serverConfigCaptor.capture());
        final JaxrsServerConfig serverConfig = serverConfigCaptor.getValue();
        assertEquals(HelloService.class, serverConfig.getServiceInterface());
        assertEquals(getBaseAddress(), serverConfig.getBaseAddress());
    }
    @Test
    public void testCreateProxy() {
        final HelloService proxy = createProxy();
        verify(proxyDecorator).decorate(proxyConfigCaptor.capture());
        final JaxrsProxyConfig proxyConfig = proxyConfigCaptor.getValue();

        assertEquals(HelloService.class, proxyConfig.getServiceInterface());
        assertEquals(getBaseAddress(), proxyConfig.getBaseAddress());

        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        assertEquals(1, clientRequestCount.getRequestCount().get());
        assertEquals(1, containerRequestCount.getRequestCount().get());
    }
}
