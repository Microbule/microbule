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

package org.microbule.errormap.decorator;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;
import org.microbule.errormap.api.ErrorMapperService;

@Provider
public class ErrorMapperResponseExceptionMapper implements ResponseExceptionMapper<Exception> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;
    private final String strategy;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperResponseExceptionMapper(ErrorMapperService errorMapperService, String strategy) {
        this.errorMapperService = errorMapperService;
        this.strategy = strategy;
    }

//----------------------------------------------------------------------------------------------------------------------
// ResponseExceptionMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Exception fromResponse(Response response) {
        return errorMapperService.createException(strategy, response);
    }
}
