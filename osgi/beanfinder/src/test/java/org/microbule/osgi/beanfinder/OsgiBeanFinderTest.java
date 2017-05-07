package org.microbule.osgi.beanfinder;

import org.junit.Rule;
import org.junit.Test;
import org.microbule.beanfinder.api.BeanFinderListener;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.mockito.Mock;

import static org.microbule.test.osgi.ServicePropsBuilder.props;
import static org.mockito.Mockito.verify;

public class OsgiBeanFinderTest extends MockObjectTestCase {
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
    public void testFindBeans() {
        final OsgiBeanFinder finder = new OsgiBeanFinder(osgiRule.getBundleContext(), 10);
        finder.findBeans(MyService.class, listener);

        osgiRule.registerService(MyService.class, service, props());
        verify(listener).beanFound(service);
    }
}