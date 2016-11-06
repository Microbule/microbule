package org.microbule.decorator.tracer;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
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

    @Test
    public void testWithCustomTraceId() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header(TracerConstants.DEFAULT_TRACE_ID_HEADER, "foobarbaz")
                .get();
        assertEquals("foobarbaz", response.getHeaderString(TracerConstants.DEFAULT_TRACE_ID_HEADER));
    }
}