package org.microbule.validation.decorator;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.junit.Test;
import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.server.hello.HelloTestCase;

public class ValidationDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleBeanFinder finder) {
        finder.addBean(new ValidationDecorator());
        finder.addBean(new JaxrsServerDecorator() {
            @Override
            public String name() {
                return "errorhandler";
            }

            @Override
            public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
                descriptor.addProvider(new CveMapper());
            }
        });
    }

    @Test
    public void validateWithNoParameters() {
        assertEquals("1.0", createProxy().version());
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