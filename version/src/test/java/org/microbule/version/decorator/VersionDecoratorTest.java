package org.microbule.version.decorator;

import java.io.StringReader;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.jsonb.api.JsonbFactory;
import org.microbule.jsonb.core.DefaultJsonbFactory;
import org.microbule.jsonb.decorator.JsonbProxyDecorator;
import org.microbule.jsonb.decorator.JsonbServerDecorator;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.server.hello.HelloTestCase;

public class VersionDecoratorTest extends HelloTestCase {

    private JsonbFactory jsonbFactory;

    @Override
    protected void addBeans(SimpleContainer container) {
        jsonbFactory = new DefaultJsonbFactory(container);
        container.addBean(jsonbFactory);
        container.addBean(new JsonbServerDecorator(jsonbFactory));
        container.addBean(new JsonbProxyDecorator(jsonbFactory));
        container.addBean(new VersionDecorator());
    }

    @Test
    public void testGetVersion() {
        final Response response = createWebTarget().queryParam("_version", "bogus").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        final VersionResponse versionResponse = jsonbFactory.createJsonb().fromJson(new StringReader(response.readEntity(String.class)), VersionResponse.class);
        final Package pkg = HelloService.class.getPackage();
        assertEquals(pkg.getImplementationVersion(), versionResponse.getVersion());
        assertEquals(pkg.getImplementationTitle(), versionResponse.getTitle());
    }
}