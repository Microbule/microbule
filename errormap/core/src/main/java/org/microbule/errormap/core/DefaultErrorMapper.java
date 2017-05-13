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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.microbule.errormap.spi.ErrorMapper;


public class DefaultErrorMapper implements ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final ErrorMapper INSTANCE = new DefaultErrorMapper();

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public List<String> getErrorMessages(Exception exception) {
        return Collections.singletonList(exception.getMessage());
    }

    @Override
    public Class<? extends Exception> getExceptionType() {
        return Exception.class;
    }

    @Override
    public Response.Status getStatus(Exception exception) {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}
