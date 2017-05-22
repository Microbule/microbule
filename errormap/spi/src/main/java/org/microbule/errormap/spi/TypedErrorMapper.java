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

package org.microbule.errormap.spi;

import java.util.List;

import javax.ws.rs.core.Response;

import org.microbule.util.reflect.Types;

public abstract class TypedErrorMapper<E extends Exception> implements ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<E> exceptionType;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected TypedErrorMapper() {
        this.exceptionType = Types.getTypeParameter(getClass(), TypedErrorMapper.class, 0);
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract List<String> doGetErrorMessages(E exception);

    protected abstract Response.StatusType doGetStatus(E exception);

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getErrorMessages(Exception exception) {
        return doGetErrorMessages((E) exception);
    }

    @Override
    public Class<? extends Exception> getExceptionType() {
        return exceptionType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response.StatusType getStatus(Exception exception) {
        return doGetStatus((E) exception);
    }
}
