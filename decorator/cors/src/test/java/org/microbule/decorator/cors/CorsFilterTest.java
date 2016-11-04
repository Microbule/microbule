package org.microbule.decorator.cors;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.test.hello.HelloTestCase;

public class CorsFilterTest extends HelloTestCase {

    public static final String ALLOWED_ORIGIN = "http://localhost/";

    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        factory.addDecorator("cors", new CorsDecorator());
    }

    @Override
    protected Map<String, Object> createProperties() {
        Map<String,Object> props = new HashMap<>();
        props.put(CorsFilter.ALLOWED_ORIGINS_PROP, ALLOWED_ORIGIN);
        props.put(CorsFilter.ALLOWED_METHODS_PROP, "GET,PUT,POST");
        props.put(CorsFilter.ALLOWED_HEADERS_PROP, "Microbule-Foo");
        return props;
    }

    @Test
    public void testSimpleCorsRequest() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .get();
        assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequest() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidOrigin() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", "http://somebogusorigin/")
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidMethod() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.HEAD)
                .options();
        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidHeader() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .header("Access-Control-Request-Headers", "Microbule-Bar")
                .options();

        assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        assertEquals("Origin", response.getHeaderString("Vary"));
    }
}