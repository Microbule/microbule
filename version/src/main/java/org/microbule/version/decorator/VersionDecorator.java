package org.microbule.version.decorator;

import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named("versionDecorator")
@Singleton
public class VersionDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final URL url = descriptor.serviceInterface().getProtectionDomain().getCodeSource().getLocation();
        descriptor.addProvider(new VersionRequestFilter(new VersionResponse(descriptor.serviceInterface().getPackage().getImplementationVersion())));
    }

    @Override
    public String name() {
        return "version";
    }
}
