package org.microbule.decorator.circuitbreaker;

import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

public class CircuitBreakerDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsProxyConfig object) {
        object.addProvider(new CircuitBreakerFilter(object));
    }
}
