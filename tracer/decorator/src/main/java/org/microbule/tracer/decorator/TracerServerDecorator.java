package org.microbule.tracer.decorator;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.tracer.spi.TracerIdStrategy;

@Singleton
@Named("tracerServerDecorator")
public class TracerServerDecorator implements JaxrsServerDecorator, TracerConstants {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<TracerIdStrategy> idGeneratorRef;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public TracerServerDecorator(MicrobuleContainer container) {
        this.idGeneratorRef = container.pluginReference(TracerIdStrategy.class, UuidStrategy.INSTANCE);
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final String traceIdHeader = config.value("traceIdHeader").orElse(DEFAULT_TRACE_ID_HEADER);
        final String requestIdHeader = config.value("requestIdHeader").orElse(DEFAULT_REQUEST_ID_HEADER);
        descriptor.addProvider(new TracerContainerFilter(idGeneratorRef, traceIdHeader, requestIdHeader));
    }

    @Override
    public String name() {
        return "tracer";
    }
}
