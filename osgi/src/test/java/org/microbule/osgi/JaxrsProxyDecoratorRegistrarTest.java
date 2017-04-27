package org.microbule.osgi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.osgi.ServicePropsBuilder;
import org.mockito.Mock;
import org.osgi.framework.ServiceRegistration;

public class JaxrsProxyDecoratorRegistrarTest extends MockObjectTestCase {
    @Rule
    public final OsgiRule osgiRule = new OsgiRule();


    @Mock
    private JaxrsProxyDecorator decorator;
    private JaxrsProxyDecoratorRegistrar registrar;
    private final DefaultJaxrsProxyFactory factory = new DefaultJaxrsProxyFactory();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initRegistrar() {
        registrar = new JaxrsProxyDecoratorRegistrar(osgiRule.getBundleContext(), factory);
    }

    @Test
    public void testRegisterDecorator() {
        final ServiceRegistration<JaxrsProxyDecorator> registration = osgiRule.registerService(JaxrsProxyDecorator.class, decorator, ServicePropsBuilder.props().with("name", "mock"));
        assertSame(decorator, factory.getDecorator("mock"));
        registration.unregister();
        assertEquals(0, factory.getDecoratorCount());
    }

    @Test
    public void testRegisterDecoratorWithNoName() {
        osgiRule.registerService(JaxrsProxyDecorator.class, decorator, ServicePropsBuilder.props());
        assertEquals(0, factory.getDecoratorCount());
    }
}