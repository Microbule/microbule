package org.microbule.example.common;

import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.microbule.cache.annotation.Cacheable;

@Path("/")
public interface HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/hello/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Cacheable
    HelloResponse sayHello(@PathParam("name") @Size(min = 5, message="Name must be at least 5 characters long.") String name);
}
