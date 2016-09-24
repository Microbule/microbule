package org.microbule.spi.error;

import javax.ws.rs.core.Response;

public class ConstantErrorMapper extends AbstractErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<? extends Exception> exceptionType;
    private final Response.Status status;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ConstantErrorMapper(Class<? extends Exception> exceptionType, Response.Status status) {
        this.exceptionType = exceptionType;
        this.status = status;
    }

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Class<? extends Exception> getExceptionType() {
        return exceptionType;
    }

    @Override
    public Response.Status getStatus(Exception exception) {
        return status;
    }
}
