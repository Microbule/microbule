package org.microbule.decorator.tracer;

import com.savoirtech.eos.pattern.whiteboard.SingleWhiteboard;
import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServerProperties;
import org.osgi.framework.BundleContext;

public class TracerDecorator extends SingleWhiteboard<TracerIdProvider> implements JaxrsServerDecorator {
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
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServer server, JaxrsServerProperties properties) {
        server.addProvider(new TracerFilter(traceIdHeader, getService(UuidTracerIdProvider.INSTANCE)));
    }
}
