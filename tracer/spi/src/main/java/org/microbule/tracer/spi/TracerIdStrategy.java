package org.microbule.tracer.spi;

import javax.ws.rs.container.ContainerRequestContext;

public interface TracerIdStrategy {
    String generateTraceId(ContainerRequestContext request);

    String generateRequestId(ContainerRequestContext request);
}
