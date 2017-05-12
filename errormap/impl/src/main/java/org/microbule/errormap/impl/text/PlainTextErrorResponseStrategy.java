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

package org.microbule.errormap.impl.text;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.errormap.impl.AbstractErrorResponseStrategy;
import org.microbule.errormap.impl.Responses;
import org.microbule.errormap.spi.ErrorResponseStrategy;

@Named("plainTextErrorResponseStrategy")
@Singleton
public class PlainTextErrorResponseStrategy extends AbstractErrorResponseStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final ErrorResponseStrategy INSTANCE = new PlainTextErrorResponseStrategy();

//----------------------------------------------------------------------------------------------------------------------
// ErrorResponseStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public RuntimeException createException(Response response) {
        return createException(response, Responses.getErrorMessage(response));
    }

    @Override
    public Response createResponse(Response.StatusType status, List<String> errorMessages) {
        return Response.status(status)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(StringUtils.join(errorMessages, NEWLINE))
                .build();
    }

    @Override
    public String name() {
        return ErrorMapperService.DEFAULT_STRATEGY;
    }
}
