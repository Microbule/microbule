package org.microbule.tracer.decorator;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;

import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.tracer.spi.TracerIdStrategy;

@Named
public class TracerServerDecorator implements JaxrsServerDecorator, TracerConstants {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<TracerIdStrategy> idGeneratorRef;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public TracerServerDecorator(BeanFinder finder) {
        this.idGeneratorRef = finder.beanReference(TracerIdStrategy.class, UuidStrategy.INSTANCE);
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
