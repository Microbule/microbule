package org.microbule.osgi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.osgi.ServicePropsBuilder;
import org.mockito.Mock;
import org.osgi.framework.ServiceRegistration;

public class JaxrsServerDecoratorRegistrarTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();


    @Mock
    private JaxrsServerDecorator decorator;
    private JaxrsServerDecoratorRegistrar registrar;
    private final DefaultJaxrsServerFactory factory = new DefaultJaxrsServerFactory();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initRegistrar() {
        registrar = new JaxrsServerDecoratorRegistrar(osgiRule.getBundleContext(), factory);
    }

    @Test
    public void testRegisterDecorator() {
        final ServiceRegistration<JaxrsServerDecorator> registration = osgiRule.registerService(JaxrsServerDecorator.class, decorator, ServicePropsBuilder.props().with("name", "mock"));
        assertSame(decorator, factory.getDecorator("mock"));
        registration.unregister();
        assertEquals(0, factory.getDecoratorCount());
    }

    @Test
    public void testRegisterDecoratorWithNoName() {
        osgiRule.registerService(JaxrsServerDecorator.class, decorator, ServicePropsBuilder.props());
        assertEquals(0, factory.getDecoratorCount());
    }
}