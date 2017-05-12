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

package org.microbule.cache.decorator;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.junit.Test;
import org.microbule.cache.decorator.resource.CacheResource;
import org.microbule.cache.decorator.resource.CacheResourceImpl;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.JaxrsServerTestCase;

public class CacheServerDecoratorTest extends JaxrsServerTestCase<CacheResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new CacheServerDecorator());
    }

    @Override
    protected CacheResource createImplementation() {
        return new CacheResourceImpl();
    }

    @Test
    public void testEmptyResponseWithMatchingEtag() {
        final Response response = createWebTarget().path("name").path("etag")
                .request(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.IF_NONE_MATCH, CacheResource.ENTITY_TAG)
                .get();
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testEmptyResponseWithUnmodified() {
        final Response response = createWebTarget().path("name").path("lastModified")
                .request(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.IF_MODIFIED_SINCE, HttpUtils.getHttpDateFormat().format(Date.from(CacheResource.LAST_MODIFIED.toInstant().plus(5, ChronoUnit.MINUTES))))
                .get();
        assertEquals(204, response.getStatus());
    }

    @Test
    public void testEntityTagSupport() {
        final Response response = createWebTarget().path("name").path("etag").request(MediaType.TEXT_PLAIN).get();
        CacheControl cacheControl = CacheControl.valueOf(response.getHeaderString(HttpHeaders.CACHE_CONTROL));
        assertEquals(600, cacheControl.getMaxAge());
        assertTrue(cacheControl.isNoTransform());
        assertFalse(cacheControl.isMustRevalidate());
        final EntityTag entityTag = response.getEntityTag();
        assertNotNull(entityTag);
        assertEquals("12345", entityTag.getValue());
    }

    @Test
    public void testLastUpdatedAndEtagSupport() {
        final Response response = createWebTarget().path("name").path("lastModifiedAndEtag").request(MediaType.TEXT_PLAIN).get();
        CacheControl cacheControl = CacheControl.valueOf(response.getHeaderString(HttpHeaders.CACHE_CONTROL));
        assertEquals(600, cacheControl.getMaxAge());
        assertTrue(cacheControl.isNoTransform());
        assertFalse(cacheControl.isMustRevalidate());

        Date lastModified = response.getLastModified();
        assertEquals(CacheResource.LAST_MODIFIED, lastModified);

        final EntityTag entityTag = response.getEntityTag();
        assertNotNull(entityTag);
        assertEquals("12345", entityTag.getValue());
    }

    @Test
    public void testLastUpdatedSupport() {
        final Response response = createWebTarget().path("name").path("lastModified").request(MediaType.TEXT_PLAIN).get();
        CacheControl cacheControl = CacheControl.valueOf(response.getHeaderString(HttpHeaders.CACHE_CONTROL));
        assertEquals(600, cacheControl.getMaxAge());
        assertTrue(cacheControl.isNoTransform());
        assertFalse(cacheControl.isMustRevalidate());
        Date lastModified = response.getLastModified();
        assertEquals(CacheResource.LAST_MODIFIED, lastModified);
    }

    @Test
    public void testWithNoState() {
        final Response response = createWebTarget().path("name").path("noState").request(MediaType.TEXT_PLAIN).get();
        CacheControl cacheControl = CacheControl.valueOf(response.getHeaderString(HttpHeaders.CACHE_CONTROL));
        assertEquals(600, cacheControl.getMaxAge());
        assertTrue(cacheControl.isNoTransform());
        assertFalse(cacheControl.isMustRevalidate());

        assertNull(response.getLastModified());
        assertNull(response.getEntityTag());
    }

    @Test
    public void testWithResponse() {
        final Response response = createProxy().createResponse();
        CacheControl cacheControl = CacheControl.valueOf(response.getHeaderString(HttpHeaders.CACHE_CONTROL));
        assertEquals(600, cacheControl.getMaxAge());
        assertTrue(cacheControl.isNoTransform());
        assertFalse(cacheControl.isMustRevalidate());

        Date lastModified = response.getLastModified();
        assertEquals(CacheResource.LAST_MODIFIED, lastModified);

        final EntityTag entityTag = response.getEntityTag();
        assertNotNull(entityTag);
        assertEquals("12345", entityTag.getValue());
    }
}