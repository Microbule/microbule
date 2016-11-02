package org.microbule.decorator.errormap;

import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.spi.JaxrsProxy;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;

public class ErrorMapperDecorator implements JaxrsServerDecorator, JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperDecorator(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsProxy proxy) {
        proxy.addProvider(new ErrorMapperResponseExceptionMapper(errorMapperService));
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServer server) {
        server.addProvider(new WebApplicationExceptionMapper(errorMapperService));
        server.addProvider(new RootExceptionMapper(errorMapperService));
    }
}
