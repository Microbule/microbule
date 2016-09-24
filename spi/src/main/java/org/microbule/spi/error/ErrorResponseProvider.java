package org.microbule.spi.error;

import java.util.List;

import javax.ws.rs.core.Response;


public interface ErrorResponseProvider {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Response createResponse(Response.StatusType status, List<String> exceptionMessages);
}
