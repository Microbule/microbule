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
import javax.ws.rs.ext.ExceptionMapper;

import org.microbule.errormap.api.ErrorMapperService;


public abstract class ErrorMapperExceptionMapper<E extends Exception> implements ExceptionMapper<E> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;
    private final String strategy;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperExceptionMapper(ErrorMapperService errorMapperService, String strategy) {
        this.errorMapperService = errorMapperService;
        this.strategy = strategy;
    }

//----------------------------------------------------------------------------------------------------------------------
// ExceptionMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Response toResponse(E exception) {
        return errorMapperService.createResponse(strategy, exception);
    }
}
