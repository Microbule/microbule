package org.microbule.osgi.beanfinder;

import org.junit.Rule;
import org.junit.Test;
import org.microbule.beanfinder.api.BeanFinderListener;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.mockito.Mock;
import org.osgi.framework.ServiceRegistration;

import static org.microbule.test.osgi.ServicePropsBuilder.props;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BeanFinderListenerWhiteboardTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

    @Mock
    private BeanFinderListener<MyService> listener;

    @Mock
    private MyService service;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testBeanFound() {
        new BeanFinderListenerWhiteboard<>(osgiRule.getBundleContext(), MyService.class, listener);
        when(listener.beanFound(service)).thenReturn(true);
        osgiRule.registerService(MyService.class, service, props());
        verify(listener).beanFound(service);
    }

    @Test
    public void testBeanFoundWhenNotAccepted() {
        final BeanFinderListenerWhiteboard<MyService> whiteboard = new BeanFinderListenerWhiteboard<>(osgiRule.getBundleContext(), MyService.class, listener);
        when(listener.beanFound(service)).thenReturn(false);
        osgiRule.registerService(MyService.class, service, props());
        verify(listener).beanFound(service);
        assertEquals(0, whiteboard.getServiceCount());
    }

    @Test
    public void testBeanLost() {
        new BeanFinderListenerWhiteboard<>(osgiRule.getBundleContext(), MyService.class, listener);
        when(listener.beanFound(service)).thenReturn(true);
        final ServiceRegistration<MyService> registration = osgiRule.registerService(MyService.class, service, props());
        verify(listener).beanFound(service);
        registration.unregister();
        verify(listener).beanLost(service);
    }
}