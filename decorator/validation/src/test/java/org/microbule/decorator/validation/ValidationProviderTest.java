package org.microbule.decorator.validation;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.junit.Test;
import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.test.hello.HelloTestCase;

public class ValidationProviderTest extends HelloTestCase {
    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        factory.addDecorator("validation", new ValidationProvider());
        factory.addDecorator("errorhandler", config -> config.addProvider(new CveMapper()));
    }

    private static class CveMapper implements ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException exception) {
            final String message = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
            return Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }
    @Test
    public void testSayHelloWithShortName() {
        expectException(BadRequestException.class, "HTTP 400 Bad Request");
        createProxy().sayHello("foo");
    }
}