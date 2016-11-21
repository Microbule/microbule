package org.microbule.decorator.tracer;

import java.io.IOException;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

import static org.microbule.decorator.tracer.TracerConstants.REQUEST_ID_KEY;
import static org.microbule.decorator.tracer.TracerConstants.TRACE_ID_KEY;

@Provider
@javax.annotation.Priority(Priorities.HEADER_DECORATOR)
public class TracerContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String traceIdHeader;
    private final String requestIdHeader;
    private final TracerIdProvider idProvider;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerContainerFilter(String traceIdHeader, String requestIdHeader, TracerIdProvider idProvider) {
        this.traceIdHeader = traceIdHeader;
        this.requestIdHeader = requestIdHeader;
        this.idProvider = idProvider;
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        String traceId = request.getHeaderString(traceIdHeader);
        if(traceId == null) {
            traceId = idProvider.generateTraceId(request);
        }
        final String requestId = idProvider.generateRequestId(request);
        request.setProperty(TRACE_ID_KEY, traceId);
        request.setProperty(REQUEST_ID_KEY, requestId);
        MDC.put(TRACE_ID_KEY, traceId);
        MDC.put(REQUEST_ID_KEY, requestId);
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        MDC.remove(TRACE_ID_KEY);
        MDC.remove(REQUEST_ID_KEY);
        response.getHeaders().putSingle(traceIdHeader, request.getProperty(TRACE_ID_KEY));
        response.getHeaders().putSingle(requestIdHeader, request.getProperty(REQUEST_ID_KEY));

    }
}
