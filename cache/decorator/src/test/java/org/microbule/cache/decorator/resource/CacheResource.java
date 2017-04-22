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
    @Path("/value/etag")
    @Cacheable(maxAge = 600)
    String getValueWithEtag();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/value/lastModified")
    @Cacheable(maxAge = 600)
    String getValueWithLastModified();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/value/lastModifiedAndEtag")
    @Cacheable(maxAge = 600)
    String getValueWithLastModifiedAndEtag();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/value/noState")
    @Cacheable(maxAge = 600)
    String getValueWithNoState();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/createResponse")
    @Cacheable(maxAge = 600)
    Response createResponse();
}
