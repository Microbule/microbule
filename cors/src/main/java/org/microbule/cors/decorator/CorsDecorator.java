package org.microbule.cors.decorator;

import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named
public class CorsDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new CorsFilter(config));
    }

    @Override
    public String name() {
        return "cors";
    }
}
