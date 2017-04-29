package org.microbule.timeout.decorator;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

public class TimeoutProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final long DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final long DEFAULT_RECEIVE_TIMEOUT = 60000;

    private final long connectionTimeout;
    private final long receiveTimeout;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------


    public TimeoutProxyDecorator() {
        this(DEFAULT_CONNECTION_TIMEOUT, DEFAULT_RECEIVE_TIMEOUT);
    }

    public TimeoutProxyDecorator(long connectionTimeout, long receiveTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.receiveTimeout = receiveTimeout;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new TimeoutFilter(descriptor, config, connectionTimeout, receiveTimeout));
    }
}
