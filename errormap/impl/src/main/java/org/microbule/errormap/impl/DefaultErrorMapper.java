package org.microbule.errormap.impl;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.microbule.errormap.spi.ErrorMapper;


public class DefaultErrorMapper implements ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final ErrorMapper INSTANCE = new DefaultErrorMapper();

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public List<String> getErrorMessages(Exception exception) {
        return Collections.singletonList(exception.getMessage());
    }

    @Override
    public Class<? extends Exception> getExceptionType() {
        return Exception.class;
    }

    @Override
    public Response.Status getStatus(Exception exception) {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}
