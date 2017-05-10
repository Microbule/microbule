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

package org.microbule.tracer.decorator;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.microbule.tracer.spi.TracerIdStrategy;
import org.slf4j.MDC;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class TracerContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String traceIdHeader;
    private final String requestIdHeader;
    private final AtomicReference<TracerIdStrategy> idGeneratorRef;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerContainerFilter(AtomicReference<TracerIdStrategy> idGeneratorRef, String traceIdHeader, String requestIdHeader) {
        this.idGeneratorRef = idGeneratorRef;
        this.traceIdHeader = traceIdHeader;
        this.requestIdHeader = requestIdHeader;
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String traceId = request.getHeaderString(traceIdHeader);
        if(traceId == null) {
            traceId = idGeneratorRef.get().generateTraceId(request);
        }
        final String requestId = idGeneratorRef.get().generateRequestId(request);
        request.setProperty(TracerConstants.TRACE_ID_KEY, traceId);
        request.setProperty(TracerConstants.REQUEST_ID_KEY, requestId);
        MDC.put(TracerConstants.TRACE_ID_KEY, traceId);
        MDC.put(TracerConstants.REQUEST_ID_KEY, requestId);
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        MDC.remove(TracerConstants.TRACE_ID_KEY);
        MDC.remove(TracerConstants.REQUEST_ID_KEY);
        response.getHeaders().putSingle(traceIdHeader, request.getProperty(TracerConstants.TRACE_ID_KEY));
        response.getHeaders().putSingle(requestIdHeader, request.getProperty(TracerConstants.REQUEST_ID_KEY));
    }
}
