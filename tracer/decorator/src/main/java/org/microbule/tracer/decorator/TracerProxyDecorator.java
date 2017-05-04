package org.microbule.tracer.decorator;

import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("tracerProxyDecorator")
public class TracerProxyDecorator implements JaxrsProxyDecorator, TracerConstants {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final String traceIdHeader = config.value("traceIdHeader").orElse(DEFAULT_TRACE_ID_HEADER);
        descriptor.addProvider(new TracerClientFilter(traceIdHeader));
    }

    @Override
    public String name() {
        return "tracer";
    }
}
