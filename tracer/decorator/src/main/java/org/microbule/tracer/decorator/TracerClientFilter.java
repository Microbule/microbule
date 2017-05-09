package org.microbule.tracer.decorator;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class TracerClientFilter implements ClientRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String traceIdHeader;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerClientFilter(String traceIdHeader) {
        this.traceIdHeader = traceIdHeader;
    }

//----------------------------------------------------------------------------------------------------------------------
// ClientRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        final String traceId = MDC.get(TracerConstants.TRACE_ID_KEY);
        if (StringUtils.isNotBlank(traceId)) {
            requestContext.getHeaders().putSingle(traceIdHeader, traceId);
        }
    }
}
