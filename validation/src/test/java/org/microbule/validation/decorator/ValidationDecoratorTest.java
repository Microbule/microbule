package org.microbule.validation.decorator;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.junit.Test;
import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.test.hello.HelloTestCase;

public class ValidationDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(DefaultJaxrsServerFactory factory) {
        factory.addDecorator("validation", new ValidationDecorator());
        factory.addDecorator("errorhandler", (desc, config) -> desc.addProvider(new CveMapper()));
    }

    @Test
    public void testSayHelloWithShortName() {
        expectException(BadRequestException.class, "HTTP 400 Bad Request");
        createProxy().sayHello("foo");
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class CveMapper implements ExceptionMapper<ConstraintViolationException> {
//----------------------------------------------------------------------------------------------------------------------
// ExceptionMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response toResponse(ConstraintViolationException exception) {
            final String message = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
            return Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }
}