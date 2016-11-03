package org.microbule.decorator.tracer;

import org.junit.Rule;
import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.test.hello.HelloTestCase;
import org.microbule.test.osgi.OsgiRule;

public class TracerServerDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        factory.addDecorator("tracer", new TracerServerDecorator(osgiRule.getBundleContext(), TracerConstants.DEFAULT_TRACE_ID_HEADER));
    }
}