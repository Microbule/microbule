package org.microbule.timeout.decorator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface DelayResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/delay/{value}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String delay(@PathParam("value") long value);
}
