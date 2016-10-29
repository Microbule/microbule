package org.microbule.decorator.tracer;

import javax.ws.rs.container.ContainerRequestContext;

public interface TracerIdProvider {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    String generateRequestId(ContainerRequestContext request);
    String generateTraceId(ContainerRequestContext request);
}
