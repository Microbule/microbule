package org.microbule.decorator.tracer;

import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

public class TracerProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String traceIdHeader;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TracerProxyDecorator(String traceIdHeader) {
        this.traceIdHeader = traceIdHeader;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsProxyConfig proxy) {
        proxy.addProvider(new TracerClientFilter(traceIdHeader));
    }
}
