package org.microbule.gson.provider;

import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.api.GsonService;
import org.microbule.gson.core.GsonServiceImpl;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.server.JaxrsServerTestCase;

public class GsonClientProviderTest extends JaxrsServerTestCase<BadJsonService> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        GsonService gsonService = new GsonServiceImpl(container);
        container.addBean(new JaxrsProxyDecorator() {
            @Override
            public String name() {
                return "gsonTest";
            }

            @Override
            public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
                descriptor.addProvider(new GsonProvider(gsonService, e -> new JsonResponseParsingException(e)));
            }
        });
    }

    @Override
    protected BadJsonService createImplementation() {
        return new BadJsonServiceImpl();
    }

    @Test(expected = ResponseProcessingException.class)
    public void testParsingBadResponse() {
        final Response response = createProxy().createBadJson();
        response.readEntity(Person.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class BadJsonServiceImpl implements BadJsonService {
//----------------------------------------------------------------------------------------------------------------------
// BadJsonService Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response createBadJson() {
            return Response.ok("{{{}}", MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
}
