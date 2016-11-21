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
    private final String requestIdHeader;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerServerDecorator(BundleContext bundleContext, String traceIdHeader, String requestIdHeader) {
        super(bundleContext, TracerIdProvider.class);
        this.traceIdHeader = traceIdHeader;
        this.requestIdHeader = requestIdHeader;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(new TracerContainerFilter(traceIdHeader, requestIdHeader, getService(UuidTracerIdProvider.INSTANCE)));
    }
}
