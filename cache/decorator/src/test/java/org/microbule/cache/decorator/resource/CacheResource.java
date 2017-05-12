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

package org.microbule.cache.decorator.resource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.microbule.cache.annotation.Cacheable;

@Path("/")
public interface CacheResource {

    Date LAST_MODIFIED = Date.from(Instant.now().truncatedTo(ChronoUnit.MINUTES));
    EntityTag ENTITY_TAG = new EntityTag("12345");

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/name/etag")
    @Cacheable(maxAge = 600)
    String getValueWithEtag();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/name/lastModified")
    @Cacheable(maxAge = 600)
    String getValueWithLastModified();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/name/lastModifiedAndEtag")
    @Cacheable(maxAge = 600)
    String getValueWithLastModifiedAndEtag();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/name/noState")
    @Cacheable(maxAge = 600)
    String getValueWithNoState();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/createResponse")
    @Cacheable(maxAge = 600)
    Response createResponse();
}
