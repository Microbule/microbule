/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.validation.decorator;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.container.core.SimpleContainer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.server.hello.HelloTestCase;

public class ValidationDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new ValidationDecorator());
        container.addBean(new JaxrsServerDecorator() {
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