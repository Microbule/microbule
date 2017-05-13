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

import java.util.HashMap;
import java.util.Map;

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

public final class WebApplicationExceptions {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Map<Integer, Class<? extends WebApplicationException>> EXCEPTIONS_MAP = new HashMap<>();

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    static {
        EXCEPTIONS_MAP.put(400, BadRequestException.class);
        EXCEPTIONS_MAP.put(401, NotAuthorizedException.class);
        EXCEPTIONS_MAP.put(403, ForbiddenException.class);
        EXCEPTIONS_MAP.put(404, NotFoundException.class);
        EXCEPTIONS_MAP.put(405, NotAllowedException.class);
        EXCEPTIONS_MAP.put(406, NotAcceptableException.class);
        EXCEPTIONS_MAP.put(415, NotSupportedException.class);
        EXCEPTIONS_MAP.put(500, InternalServerErrorException.class);
        EXCEPTIONS_MAP.put(503, ServiceUnavailableException.class);
    }

    public static Class<? extends WebApplicationException> getWebApplicationExceptionClass(Response response) {
        int status = response.getStatus();
        final Class<? extends WebApplicationException> exceptionType = EXCEPTIONS_MAP.get(status);
        if (exceptionType == null) {
            final int family = status / 100;
            switch (family) {
                case 3:
                    return RedirectionException.class;
                case 4:
                    return ClientErrorException.class;
                case 5:
                    return ServerErrorException.class;
                default:
                    return WebApplicationException.class;
            }
        }
        return exceptionType;
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private WebApplicationExceptions() {

    }
}
