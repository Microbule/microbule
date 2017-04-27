package org.microbule.tracer.decorator;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.server.hello.HelloTestCase;

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
    protected void addDecorators(DefaultJaxrsServerFactory factory) {
        factory.addDecorator("tracer", new TracerServerDecorator(TracerConstants.DEFAULT_TRACE_ID_HEADER, TracerConstants.DEFAULT_REQUEST_ID_HEADER));
    }

    @Test
    public void testWithCustomTraceId() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header(TracerConstants.DEFAULT_TRACE_ID_HEADER, "foobarbaz")
                .get();
        Assert.assertEquals("foobarbaz", response.getHeaderString(TracerConstants.DEFAULT_TRACE_ID_HEADER));
        Assert.assertNotNull(response.getHeaderString(TracerConstants.DEFAULT_REQUEST_ID_HEADER));
    }
}