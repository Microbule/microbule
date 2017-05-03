package org.microbule.timeout.decorator;

import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named
public class TimeoutProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new TimeoutFilter(descriptor, config));
    }

    @Override
    public String name() {
        return "timeout";
    }
}
