package org.microbule.errormap.impl;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.errormap.spi.ErrorResponseStrategy;
import org.microbule.test.MockObjectTestCase;

public class PlainTextErrorResponseStrategyTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateResponse() {
        final ErrorResponseStrategy strategy = PlainTextErrorResponseStrategy.INSTANCE;
        final Response response = strategy.createResponse(Response.Status.FORBIDDEN, Lists.newArrayList("You are forbidden!", "Not gonna happen!"));
        assertEquals("You are forbidden!\nNot gonna happen!", response.readEntity(String.class));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateException() {
        assertExceptionType(Response.Status.INTERNAL_SERVER_ERROR, InternalServerErrorException.class);
        assertExceptionType(Response.Status.NOT_FOUND, NotFoundException.class);
        assertExceptionType(Response.Status.FORBIDDEN, ForbiddenException.class);
        assertExceptionType(Response.Status.BAD_REQUEST, BadRequestException.class);
        assertExceptionType(Response.Status.METHOD_NOT_ALLOWED, NotAllowedException.class);
        assertExceptionType(Response.Status.UNAUTHORIZED, NotAuthorizedException.class);
        assertExceptionType(Response.Status.NOT_ACCEPTABLE, NotAcceptableException.class);
        assertExceptionType(Response.Status.UNSUPPORTED_MEDIA_TYPE, NotSupportedException.class);
        assertExceptionType(Response.Status.SERVICE_UNAVAILABLE, ServiceUnavailableException.class);
        assertExceptionType(Response.Status.TEMPORARY_REDIRECT, RedirectionException.class);
        assertExceptionType(Response.Status.LENGTH_REQUIRED, ClientErrorException.class);
        assertExceptionType(Response.Status.BAD_GATEWAY, ServerErrorException.class);
        assertExceptionType(Response.Status.NO_CONTENT, WebApplicationException.class);
    }

    private void assertExceptionType(Response.Status status, Class<? extends WebApplicationException> exceptionType) {
        final RuntimeException exception = PlainTextErrorResponseStrategy.INSTANCE.createException(Response.status(status).entity("The error message").build());
        assertEquals(exceptionType, exception.getClass());
        assertEquals("The error message", exception.getMessage());
    }
}