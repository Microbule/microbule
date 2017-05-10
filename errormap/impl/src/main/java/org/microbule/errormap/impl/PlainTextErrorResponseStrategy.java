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
