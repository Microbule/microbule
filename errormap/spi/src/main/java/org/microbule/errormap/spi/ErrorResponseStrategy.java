package org.microbule.errormap.spi;

import java.util.List;

import javax.ws.rs.core.Response;


public interface ErrorResponseStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Response createResponse(Response.StatusType status, List<String> errorMessages);

    RuntimeException createException(Response response);
}
