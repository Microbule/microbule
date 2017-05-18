package org.microbule.metrics.decorator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.microbule.metrics.annotation.Timed;

@Path("/")
public interface TimedResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Timed
    @Path("/defaultTimer")
    String defaultTimer();


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Timed(strategy = "decay")
    @Path("/decayTimer")
    String decayTimer();
}
