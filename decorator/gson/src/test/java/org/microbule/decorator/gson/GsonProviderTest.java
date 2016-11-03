package org.microbule.decorator.gson;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.microbule.core.JaxrsProxyFactoryImpl;
import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.decorator.gson.resource.GenericResponse;
import org.microbule.decorator.gson.resource.HelloResource;
import org.microbule.decorator.gson.resource.HelloResourceImpl;
import org.microbule.decorator.gson.resource.HelloResponse;
import org.microbule.test.JaxrsTestCase;

public class GsonProviderTest extends JaxrsTestCase<HelloResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final GsonFactory gsonFactory = new GsonFactory();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        factory.addDecorator("gson", new GsonServerDecorator(gsonFactory));
    }

    @Override
    protected void addDecorators(JaxrsProxyFactoryImpl factory) {
        factory.addDecorator("gson", new GsonProxyDecorator(gsonFactory));
    }

    @Override
    protected HelloResource createImplementation() {
        return new HelloResourceImpl();
    }

    @Test
    public void testSerialization() {
        final HelloResource helloResource = createProxy();
        helloResource.sayHelloGeneric("Microbule");
    }

    @Test
    public void testEncodingGeneric() {
        GenericResponse<String> response = createProxy().sayHelloGeneric("JAX-RS");
        assertEquals("Hello, JAX-RS!", response.getPayload());
    }

    @Test
    public void testEncodingRaw() {
        HelloResponse response = createProxy().sayHelloRaw("JAX-RS");
        assertEquals("Hello, JAX-RS!", response.getPayload());
    }

    @Test
    public void testGetSize() {
        long size = new GsonProvider(gsonFactory).getSize(new HelloResponse("Hello"), HelloResponse.class, HelloResponse.class.getGenericSuperclass(), null, MediaType.APPLICATION_JSON_TYPE);
        assertEquals(-1, size);
    }
}