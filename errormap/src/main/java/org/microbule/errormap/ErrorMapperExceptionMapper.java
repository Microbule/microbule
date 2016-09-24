package org.microbule.errormap;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.microbule.errormap.ErrorMapperService;

public abstract class ErrorMapperExceptionMapper<E extends Exception> implements ExceptionMapper<E> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperExceptionMapper(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// ExceptionMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Response toResponse(E exception) {
        return errorMapperService.createResponse(exception);
    }
}
