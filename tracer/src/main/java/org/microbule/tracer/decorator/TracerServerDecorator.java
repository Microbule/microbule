package org.microbule.tracer.decorator;

import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named
public class TracerServerDecorator implements JaxrsServerDecorator, TracerConstants {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final String traceIdHeader = config.value("traceIdHeader").orElse(DEFAULT_TRACE_ID_HEADER);
        final String requestIdHeader = config.value("requestIdHeader").orElse(DEFAULT_REQUEST_ID_HEADER);
        descriptor.addProvider(new TracerContainerFilter(traceIdHeader, requestIdHeader));
    }

    @Override
    public String name() {
        return "tracer";
    }
}
