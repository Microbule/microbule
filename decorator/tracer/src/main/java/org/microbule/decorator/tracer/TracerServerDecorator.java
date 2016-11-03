package org.microbule.decorator.tracer;

import com.savoirtech.eos.pattern.whiteboard.SingleWhiteboard;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;

public class TracerServerDecorator extends SingleWhiteboard<TracerIdProvider> implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String traceIdHeader;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerServerDecorator(BundleContext bundleContext, String traceIdHeader) {
        super(bundleContext, TracerIdProvider.class);
        this.traceIdHeader = traceIdHeader;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(new TracerContainerFilter(traceIdHeader, getService(UuidTracerIdProvider.INSTANCE)));
    }
}
