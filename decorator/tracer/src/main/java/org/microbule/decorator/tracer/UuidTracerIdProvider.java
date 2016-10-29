package org.microbule.decorator.tracer;

import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;

public class UuidTracerIdProvider implements TracerIdProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final UuidTracerIdProvider INSTANCE = new UuidTracerIdProvider();

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    private static String generateUuid() {
        return UUID.randomUUID().toString();
    }

//----------------------------------------------------------------------------------------------------------------------
// TracerIdProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String generateRequestId(ContainerRequestContext request) {
        return generateUuid();
    }

    @Override
    public String generateTraceId(ContainerRequestContext request) {
        return generateUuid();
    }
}
