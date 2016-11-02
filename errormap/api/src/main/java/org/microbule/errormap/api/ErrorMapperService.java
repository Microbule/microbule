package org.microbule.errormap.api;


import javax.ws.rs.core.Response;

public interface ErrorMapperService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Exception createException(Response response);

    Response createResponse(Exception e);
}
