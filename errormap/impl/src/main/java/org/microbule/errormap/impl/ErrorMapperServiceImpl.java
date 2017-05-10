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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.microbule.container.api.MicrobuleContainer;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.errormap.spi.ErrorMapper;
import org.microbule.errormap.spi.ErrorResponseStrategy;

import static org.microbule.errormap.impl.PlainTextErrorResponseStrategy.INSTANCE;

@Singleton
@Named("errorMapperService")
public class ErrorMapperServiceImpl implements ErrorMapperService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<Class<?>, ErrorMapper> errorMappers;
    private final AtomicReference<ErrorResponseStrategy> responseStrategyRef;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public ErrorMapperServiceImpl(MicrobuleContainer container) {
        this.errorMappers = container.pluginMap(ErrorMapper.class, ErrorMapper::getExceptionType);
        this.responseStrategyRef = container.pluginReference(ErrorResponseStrategy.class, PlainTextErrorResponseStrategy.INSTANCE);
    }

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapperService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Exception createException(Response response) {
        return getErrorResponseStrategy().createException(response);
    }

    @Override
    public Response createResponse(Exception e) {
        ErrorMapper handler = getExceptionHandler(e);
        return getErrorResponseStrategy().createResponse(handler.getStatus(e), handler.getErrorMessages(e));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private ErrorResponseStrategy getErrorResponseStrategy() {
        return Optional.ofNullable(responseStrategyRef.get()).orElse(INSTANCE);
    }

    private ErrorMapper getExceptionHandler(Exception exception) {
        return findExceptionHandler(exception.getClass());
    }

    private ErrorMapper findExceptionHandler(Class<?> targetType) {
        if (Exception.class.equals(targetType)) {
            return DefaultErrorMapper.INSTANCE;
        }
        final ErrorMapper mapper = errorMappers.get(targetType);
        if (mapper == null) {
            return findExceptionHandler(targetType.getSuperclass());
        }
        return mapper;
    }
}
