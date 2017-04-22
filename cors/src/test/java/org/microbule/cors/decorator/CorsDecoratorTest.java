package org.microbule.cors.decorator;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.core.MapConfig;
import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.test.hello.HelloTestCase;

public class CorsDecoratorTest extends HelloTestCase {

    public static final String ALLOWED_ORIGIN = "http://localhost/";

    @Override
    protected void addDecorators(DefaultJaxrsServerFactory factory) {
        factory.addDecorator("cors", new CorsDecorator());
    }

    @Override
    protected MapConfig createConfig() {
        return new MapConfig()
                .addValue("cors." + CorsFilter.ALLOWED_ORIGINS_PROP, ALLOWED_ORIGIN)
                .addValue("cors." + CorsFilter.ALLOWED_METHODS_PROP, "GET,PUT,POST")
                .addValue("cors." + CorsFilter.ALLOWED_HEADERS_PROP, "Microbule-Foo");
    }

    @Test
    public void testSimpleCorsRequest() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .get();
        Assert.assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequest() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        Assert.assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidOrigin() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", "http://somebogusorigin/")
                .header("Access-Control-Request-Method", HttpMethod.GET)
                .options();
        Assert.assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }

    @Test
    public void testPreflightCorsRequestWithInvalidMethod() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .header("Access-Control-Request-Method", HttpMethod.HEAD)
                .options();
        Assert.assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
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

        Assert.assertNull(response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }
}