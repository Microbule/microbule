/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.cors.decorator;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.core.MapConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.hello.HelloTestCase;

public class CorsDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ALLOWED_ORIGIN = "http://localhost/";

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new CorsDecorator());
    }

    @Override
    protected void configureServer(MapConfig serverConfig) {
        serverConfig.addValue("cors." + CorsFilter.ALLOWED_ORIGINS_PROP, ALLOWED_ORIGIN);
        serverConfig.addValue("cors." + CorsFilter.ALLOWED_METHODS_PROP, "GET,PUT,POST");
        serverConfig.addValue("cors." + CorsFilter.ALLOWED_HEADERS_PROP, "Microbule-Foo");
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
    public void testSimpleCorsRequest() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header("Origin", ALLOWED_ORIGIN)
                .get();
        Assert.assertEquals(ALLOWED_ORIGIN, response.getHeaderString("Access-Control-Allow-Origin"));
        Assert.assertEquals("Origin", response.getHeaderString("Vary"));
    }
}