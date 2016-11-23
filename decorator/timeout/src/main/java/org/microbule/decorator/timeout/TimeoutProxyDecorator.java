package org.microbule.decorator.timeout;

import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

public class TimeoutProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final long connectionTimeout;
    private final long receiveTimeout;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TimeoutProxyDecorator(long connectionTimeout, long receiveTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.receiveTimeout = receiveTimeout;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void decorate(JaxrsProxyConfig object) {
        object.addProvider(new TimeoutFilter(object, connectionTimeout, receiveTimeout));
    }
}
