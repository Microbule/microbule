package org.microbule.decorator.errormap;


import javax.ws.rs.core.Response;

public interface ErrorMapperService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Response createResponse(Exception e);
}
