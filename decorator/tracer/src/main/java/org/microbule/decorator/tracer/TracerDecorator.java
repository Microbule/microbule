package org.microbule.decorator.tracer;

import com.savoirtech.eos.pattern.whiteboard.SingleWhiteboard;
import org.microbule.spi.JaxrsProxy;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;

public class TracerDecorator extends SingleWhiteboard<TracerIdProvider> implements JaxrsServerDecorator, JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String traceIdHeader;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerDecorator(BundleContext bundleContext, String traceIdHeader) {
        super(bundleContext, TracerIdProvider.class);
        this.traceIdHeader = traceIdHeader;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsProxy proxy) {
        proxy.addProvider(new TracerClientFilter(traceIdHeader));
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServer server) {
        server.addProvider(new TracerContainerFilter(traceIdHeader, getService(UuidTracerIdProvider.INSTANCE)));
    }
}
