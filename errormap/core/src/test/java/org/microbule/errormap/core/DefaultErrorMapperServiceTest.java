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

package org.microbule.errormap.core;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.errormap.core.json.JsonErrorResponseStrategy;
import org.microbule.errormap.core.text.PlainTextErrorResponseStrategy;
import org.microbule.errormap.spi.ConstantErrorMapper;
import org.microbule.test.core.MockObjectTestCase;

public class DefaultErrorMapperServiceTest extends MockObjectTestCase {

    private DefaultErrorMapperService service;

    @Before
    public void initService() {
        SimpleContainer container = new SimpleContainer();
        container.addBean(new PlainTextErrorResponseStrategy());
        container.addBean(new JsonErrorResponseStrategy());
        container.addBean(new ConstantErrorMapper(IllegalArgumentException.class, Response.Status.BAD_REQUEST));
        service = new DefaultErrorMapperService(container);
        container.initialize();
    }

    @Test
    public void testCreateResponse() {
        final Response response = service.createResponse("text", new IllegalArgumentException("Didn't find it!"));
        assertEquals(400, response.getStatus());
        assertEquals("Didn't find it!", response.readEntity(String.class));
    }

    @Test
    public void testCreateResponseWithNoMapper() {
        final Response response = service.createResponse("text", new RuntimeException("Didn't find it!"));
        assertEquals(500, response.getStatus());
        assertEquals("Didn't find it!", response.readEntity(String.class));
    }

    @Test
    public void testCreateException() {
        final Exception exception = service.createException("text", Response.status(Response.Status.NOT_FOUND).build());
        assertEquals(NotFoundException.class, exception.getClass());
    }

    @Test
    public void testCreateExceptionWhenStrategyNotFound() {
        final Exception exception = service.createException("bogus", Response.status(Response.Status.NOT_FOUND).entity("It was not found!").build());
        assertEquals(NotFoundException.class, exception.getClass());
    }


}