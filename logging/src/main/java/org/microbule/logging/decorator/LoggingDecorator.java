package org.microbule.logging.decorator;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.feature.LoggingFeature;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("loggingDecorator")
public class LoggingDecorator implements JaxrsServerDecorator, JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addFeature(new LoggingFeature());
    }

    @Override
    public String name() {
        return "logging";
    }
}
