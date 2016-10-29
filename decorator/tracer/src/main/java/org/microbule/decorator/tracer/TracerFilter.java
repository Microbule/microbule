package org.microbule.decorator.tracer;

import java.io.IOException;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

@Provider
@javax.annotation.Priority(Priorities.HEADER_DECORATOR)
public class TracerFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String TRACE_ID_KEY = "microbule_trace_id";
    private static final String REQUEST_ID_KEY = "microbule_request_id";
    private final String traceIdHeader;
    private final TracerIdProvider idProvider;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerFilter(String traceIdHeader, TracerIdProvider idProvider) {
        this.traceIdHeader = traceIdHeader;
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
        request.setProperty(TRACE_ID_KEY, traceId);
        MDC.put(TRACE_ID_KEY, traceId);
        MDC.put(REQUEST_ID_KEY, idProvider.generateRequestId(request));
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        MDC.remove(TRACE_ID_KEY);
        MDC.remove(REQUEST_ID_KEY);
        response.getHeaders().add(traceIdHeader, request.getProperty(TRACE_ID_KEY));
    }
}
