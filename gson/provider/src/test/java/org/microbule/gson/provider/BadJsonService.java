package org.microbule.gson.provider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface BadJsonService {

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("bad")
    Response createBadJson();
}
