package org.microbule.errormap;


import javax.ws.rs.core.Response;

public interface ErrorMapperService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Response createResponse(Exception e);
}
