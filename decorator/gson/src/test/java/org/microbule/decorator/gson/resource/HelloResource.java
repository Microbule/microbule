package org.microbule.decorator.gson.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
public interface HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/helloGeneric/{name}")
    @Produces(APPLICATION_JSON)
    @GET
    GenericResponse<String> sayHelloGeneric(@PathParam("name") String name);

    @Path("/helloRaw/{name}")
    @Produces(APPLICATION_JSON)
    @GET
    HelloResponse sayHelloRaw(@PathParam("name") String name);
}
