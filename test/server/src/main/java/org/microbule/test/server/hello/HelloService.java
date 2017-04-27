package org.microbule.test.server.hello;

import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public interface HelloService {

    @GET
    @Path("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    String sayHello(@PathParam("name") @Size(min = 5, message="Name must be at least 5 characters long.") String name);
}
