package org.microbule.osgi.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.core.MapConfig;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.osgi.ServicePropsBuilder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.osgi.framework.ServiceRegistration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class JaxrsServerManagerTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String BASE_ADDRESS = "http://localhost:8383/test";

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

    @Captor
    private ArgumentCaptor<Config> configCaptor;

    @Mock
    private JaxrsServerDecorator serverDecorator;

    @Mock
    private JaxrsProxyDecorator proxyDecorator;

    @Mock
    private ConfigService configService;
    private QuietTimeLatch latch;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void trainMocks() {
        latch = new QuietTimeLatch(10);
        when(serverDecorator.name()).thenReturn("mock");
        when(proxyDecorator.name()).thenReturn("mock");
        osgiRule.registerService(JaxrsServerDecorator.class, serverDecorator, ServicePropsBuilder.props());
        osgiRule.registerService(JaxrsProxyDecorator.class, proxyDecorator, ServicePropsBuilder.props());
        when(configService.getServerConfig(eq(HelloService.class), any(Config.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(1));
    }

    private OsgiJaxrsProxyFactory createProxyFactory() {
        return new OsgiJaxrsProxyFactory(osgiRule.getBundleContext(), latch);
    }

    private OsgiJaxrsServerFactory createServerFactory() {
        return new OsgiJaxrsServerFactory(osgiRule.getBundleContext(), latch);
    }

    @Test
    public void testServiceDiscoveryWithExistingServices() {
        final HelloServiceImpl impl = new HelloServiceImpl();
        registerServer(impl);
        final OsgiJaxrsServerFactory serverFactory = createServerFactory();
        JaxrsServerManager manager = new JaxrsServerManager(osgiRule.getBundleContext(), configService, serverFactory);
        MapConfig proxyConfig = new MapConfig();
        proxyConfig.addValue("proxyAddress", BASE_ADDRESS);
        final HelloService proxy = createProxyFactory().createProxy(HelloService.class, proxyConfig);
        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        manager.destroy();
    }

    @Test
    public void testServiceDiscoveryWithNewServices() {
        osgiRule.registerService(JaxrsServerDecorator.class, serverDecorator, ServicePropsBuilder.props());
        osgiRule.registerService(JaxrsProxyDecorator.class, proxyDecorator, ServicePropsBuilder.props());

        final OsgiJaxrsServerFactory serverFactory = createServerFactory();
        JaxrsServerManager manager = new JaxrsServerManager(osgiRule.getBundleContext(), configService, serverFactory);
        latch.await();
        final HelloServiceImpl impl = new HelloServiceImpl();
        registerServer(impl);
        MapConfig proxyConfig = new MapConfig();
        proxyConfig.addValue("proxyAddress", BASE_ADDRESS);
        final HelloService proxy = createProxyFactory().createProxy(HelloService.class, proxyConfig);
        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        manager.destroy();
    }

    @Test
    public void testServiceDiscoveryWithDuplicateDecorators() {
        final OsgiJaxrsServerFactory serverFactory = createServerFactory();
        JaxrsServerManager manager = new JaxrsServerManager(osgiRule.getBundleContext(), configService, serverFactory);
        latch.await();
        final HelloServiceImpl impl = new HelloServiceImpl();
        registerServer(impl);
        MapConfig proxyConfig = new MapConfig();
        proxyConfig.addValue("proxyAddress", BASE_ADDRESS);
        final HelloService proxy = createProxyFactory().createProxy(HelloService.class, proxyConfig);
        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        manager.destroy();
    }

    @Test
    public void testServiceDiscoveryWhenUnregistered() {
        final OsgiJaxrsServerFactory serverFactory = createServerFactory();
        JaxrsServerManager manager = new JaxrsServerManager(osgiRule.getBundleContext(), configService, serverFactory);
        latch.await();
        final HelloServiceImpl impl = new HelloServiceImpl();
        final ServiceRegistration<HelloServiceImpl> registration = registerServer(impl);
        MapConfig proxyConfig = new MapConfig();
        proxyConfig.addValue("proxyAddress", BASE_ADDRESS);
        final HelloService proxy = createProxyFactory().createProxy(HelloService.class, proxyConfig);
        assertEquals("Hello, Microbule!", proxy.sayHello("Microbule"));
        registration.unregister();
        manager.destroy();
    }

    private ServiceRegistration<HelloServiceImpl> registerServer(HelloServiceImpl impl) {
        return osgiRule.registerService(HelloService.class, impl, ServicePropsBuilder.props()
                .with("microbule.server", "true")
                .with("microbule." + JaxrsServerFactory.ADDRESS_PROP, BASE_ADDRESS)
                .with("microbule.foo", "bar"));
    }
}