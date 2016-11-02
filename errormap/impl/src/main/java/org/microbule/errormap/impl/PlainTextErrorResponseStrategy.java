package org.microbule.errormap.impl;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.microbule.errormap.spi.ErrorResponseStrategy;

public class PlainTextErrorResponseStrategy implements ErrorResponseStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final ErrorResponseStrategy INSTANCE = new PlainTextErrorResponseStrategy();
    public static final String NEWLINE = "\n";

//----------------------------------------------------------------------------------------------------------------------
// ErrorResponseStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public RuntimeException createException(Response response) {
        final String errorMessage = Responses.getErrorMessage(response);
        Class<? extends WebApplicationException> exceptionClass = WebApplicationExceptions.getWebApplicationExceptionClass(response);
        try {
            final Constructor<? extends WebApplicationException> ctor = exceptionClass.getConstructor(String.class, Response.class);
            return ctor.newInstance(errorMessage, response);
        } catch (ReflectiveOperationException e) {
            return new WebApplicationException(errorMessage, response);
        }
    }

    @Override
    public Response createResponse(Response.StatusType status, List<String> errorMessages) {
        return Response.status(status)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(StringUtils.join(errorMessages, NEWLINE))
                .build();
    }
}
