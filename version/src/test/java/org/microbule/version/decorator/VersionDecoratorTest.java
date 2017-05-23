package org.microbule.version.decorator;

import java.io.StringReader;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.api.GsonService;
import org.microbule.gson.core.DefaultGsonService;
import org.microbule.gson.decorator.GsonProxyDecorator;
import org.microbule.gson.decorator.GsonServerDecorator;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.server.hello.HelloTestCase;

public class VersionDecoratorTest extends HelloTestCase {

    private GsonService gsonService;

    @Override
    protected void addBeans(SimpleContainer container) {
        gsonService = new DefaultGsonService(container);
        container.addBean(gsonService);
        container.addBean(new GsonServerDecorator(gsonService));
        container.addBean(new GsonProxyDecorator(gsonService));
        container.addBean(new VersionDecorator());
    }

    @Test
    public void testGetVersion() {
        final Response response = createWebTarget().queryParam("_version", "bogus").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        final VersionResponse versionResponse = gsonService.parse(new StringReader(response.readEntity(String.class)), VersionResponse.class);
        final Package pkg = HelloService.class.getPackage();
        assertEquals(pkg.getImplementationVersion(), versionResponse.getVersion());
        assertEquals(pkg.getImplementationTitle(), versionResponse.getTitle());
    }
}