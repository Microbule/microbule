package org.microbule.decorator.errormap;

import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

public class ErrorMapperServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperServerDecorator(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(new WebApplicationExceptionMapper(errorMapperService));
        server.addProvider(new RootExceptionMapper(errorMapperService));
    }
}
