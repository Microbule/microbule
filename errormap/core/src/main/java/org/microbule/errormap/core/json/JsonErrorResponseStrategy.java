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

package org.microbule.errormap.core.json;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.microbule.errormap.core.AbstractErrorResponseStrategy;

@Named("jsonErrorResponseStrategy")
@Singleton
public class JsonErrorResponseStrategy extends AbstractErrorResponseStrategy {
//----------------------------------------------------------------------------------------------------------------------
// ErrorResponseStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public RuntimeException createException(Response response) {
        final JsonErrorResponse errorResponse = response.readEntity(JsonErrorResponse.class);
        final String errorMessage = errorResponse.getErrorMessages().stream().collect(Collectors.joining(NEWLINE));
        return createException(response, errorMessage);
    }

    @Override
    public Response createResponse(Response.StatusType status, List<String> errorMessages) {
        return Response
                .status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new JsonErrorResponse(status.getStatusCode(), errorMessages))
                .build();
    }

    @Override
    public String name() {
        return "json";
    }
}
