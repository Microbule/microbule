package org.microbule.errormap.decorator;

import javax.inject.Inject;
import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named
public class ErrorMapperProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public ErrorMapperProxyDecorator(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new ErrorMapperResponseExceptionMapper(errorMapperService));
    }

    @Override
    public String name() {
        return "errormap";
    }
}
