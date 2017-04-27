package org.microbule.osgi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.osgi.ServicePropsBuilder;
import org.microbule.test.server.hello.HelloService;
import org.microbule.test.server.hello.HelloServiceImpl;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.osgi.framework.ServiceRegistration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JaxrsServerManagerTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String BASE_ADDRESS = "http://localhost:8383/test";

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

    @Mock
    private JaxrsServerFactory factory;

    @Mock
    private JaxrsServer jaxrsServer;

    @Captor
    private ArgumentCaptor<Config> configCaptor;

    @Mock
    private ConfigService configService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void trainMocks() {
        when(configService.getServerConfig(eq(HelloService.class), any(Config.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(1));
    }

    @Test
    public void testDestroyServersUponShutdown() {
        final HelloServiceImpl impl = new HelloServiceImpl();
        registerServer(impl);
        final JaxrsServerManager manager = createManager(-1);
        manager.destroy();
        verify(jaxrsServer).shutdown();
    }

    @Test
    public void testServiceDiscoveryWithQuietPeriod() throws Exception {
        final HelloServiceImpl impl = new HelloServiceImpl();
        createManager(100);
        final ServiceRegistration<HelloServiceImpl> registration = registerServer(impl);

        CountDownLatch latch = new CountDownLatch(1);
        when(factory.createJaxrsServer(eq(HelloService.class), same(impl), any(Config.class))).thenAnswer(invocationOnMock -> {
            latch.countDown();
            return jaxrsServer;
        });

        latch.await(5, TimeUnit.SECONDS);

        verify(factory).createJaxrsServer(eq(HelloService.class), same(impl), configCaptor.capture());
        Config config = configCaptor.getValue();
        assertEquals("bar", config.value("foo").get());

        registration.unregister();
    }

    @Test
    public void testServiceDiscovery() {
        final HelloServiceImpl impl = new HelloServiceImpl();
        final ServiceRegistration<HelloServiceImpl> registration = registerServer(impl);
        createManager(-1);
        verify(factory).createJaxrsServer(eq(HelloService.class), same(impl),configCaptor.capture());
        Config config = configCaptor.getValue();
        assertEquals("bar", config.value("foo").get());

        registration.unregister();
        verify(jaxrsServer).shutdown();
    }

    private JaxrsServerManager createManager(long quietPeriod) {
        return new JaxrsServerManager(osgiRule.getBundleContext(), configService, factory, quietPeriod);
    }

    private ServiceRegistration<HelloServiceImpl> registerServer(HelloServiceImpl impl) {
        when(factory.createJaxrsServer(eq(HelloService.class), same(impl), any(Config.class))).thenReturn(jaxrsServer);
        return osgiRule.registerService(HelloService.class, impl, ServicePropsBuilder.props()
                .with("microbule.server", "true")
                .with("microbule." + JaxrsServerFactory.ADDRESS_PROP, BASE_ADDRESS)
                .with("microbule.foo", "bar"));
    }
}