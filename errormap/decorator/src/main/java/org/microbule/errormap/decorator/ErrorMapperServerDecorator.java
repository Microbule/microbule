package org.microbule.errormap.decorator;

import javax.inject.Inject;
import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named
public class ErrorMapperServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public ErrorMapperServerDecorator(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new WebApplicationExceptionMapper(errorMapperService));
        descriptor.addProvider(new RootExceptionMapper(errorMapperService));
    }

    @Override
    public String name() {
        return "errormap";
    }
}
