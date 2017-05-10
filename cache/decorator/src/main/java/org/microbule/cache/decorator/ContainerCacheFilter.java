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

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.microbule.cache.annotation.Cacheable;
import org.microbule.cache.decorator.state.ResourceState;

@Provider
public class ContainerCacheFilter implements ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final CacheControl cacheControl;

    @Context
    private ResourceState resourceState;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ContainerCacheFilter(Cacheable cacheable) {
        this.cacheControl = createCacheControl(cacheable);
    }

    private static CacheControl createCacheControl(Cacheable cacheable) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(cacheable.maxAge());
        cacheControl.setMustRevalidate(cacheable.mustRevalidate());
        cacheControl.setNoCache(cacheable.noCache());
        cacheControl.setNoStore(cacheable.noStore());
        cacheControl.setNoTransform(cacheable.noTransform());
        cacheControl.setPrivate(cacheable.privateFlag());
        cacheControl.setProxyRevalidate(cacheable.proxyRevalidate());
        cacheControl.setSMaxAge(cacheable.sMaxAge());
        cacheControl.getNoCacheFields().addAll(Arrays.asList(cacheable.noCacheFields()));
        cacheControl.getPrivateFields().addAll(Arrays.asList(cacheable.privateFields()));
        return cacheControl;
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        final EntityTag entityTag = getEntityTag(responseContext);
        final Date lastModified = getLastModified(responseContext);

        responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, cacheControl);
        writeHeader(responseContext, HttpHeaders.ETAG, entityTag);
        writeHeader(responseContext, HttpHeaders.LAST_MODIFIED, lastModified == null ? null : HttpUtils.getHttpDateFormat().format(lastModified));

        final Response.ResponseBuilder builder = evaluatePreconditions(requestContext, entityTag, lastModified);
        if (builder != null) {
            responseContext.setStatusInfo(Response.Status.NO_CONTENT);
            responseContext.setEntity(null);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Response.ResponseBuilder evaluatePreconditions(ContainerRequestContext request, EntityTag etag, Date lastModified) {
        if (etag != null && lastModified != null) {
            return request.getRequest().evaluatePreconditions(lastModified, etag);
        } else if (etag != null) {
            return request.getRequest().evaluatePreconditions(etag);
        } else if (lastModified != null) {
            return request.getRequest().evaluatePreconditions(lastModified);
        } else {
            return null;
        }
    }

    private EntityTag getEntityTag(ContainerResponseContext response) {
        return ObjectUtils.defaultIfNull(resourceState.getEntityTag(), response.getEntityTag());
    }

    private Date getLastModified(ContainerResponseContext response) {
        return ObjectUtils.defaultIfNull(resourceState.getLastModified(), response.getLastModified());
    }

    private void writeHeader(ContainerResponseContext response, String name, Object value) {
        if (value == null) {
            response.getHeaders().remove(name);
        } else {
            response.getHeaders().putSingle(name, value);
        }
    }
}
