package org.microbule.decorator.errormap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.microbule.errormap.api.ErrorMapperService;

@Provider
public class ErrorMapperResponseExceptionMapper implements ResponseExceptionMapper<Exception> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperResponseExceptionMapper(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// ResponseExceptionMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Exception fromResponse(Response response) {
        return errorMapperService.createException(response);
    }
}
