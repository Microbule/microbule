package org.microbule.tracer.decorator;

import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;

import org.microbule.tracer.spi.TracerIdStrategy;

public class UuidStrategy implements TracerIdStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final TracerIdStrategy INSTANCE = new UuidStrategy();

//----------------------------------------------------------------------------------------------------------------------
// TracerIdStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String generateRequestId(ContainerRequestContext request) {
        return uuid();
    }

    @Override
    public String generateTraceId(ContainerRequestContext request) {
        return uuid();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private String uuid() {
        return UUID.randomUUID().toString();
    }
}
