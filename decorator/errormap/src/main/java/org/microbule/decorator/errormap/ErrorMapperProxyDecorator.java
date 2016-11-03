package org.microbule.decorator.errormap;

import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

public class ErrorMapperProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperProxyDecorator(ErrorMapperService errorMapperService) {
        this.errorMapperService = errorMapperService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsProxyConfig proxy) {
        proxy.addProvider(new ErrorMapperResponseExceptionMapper(errorMapperService));
    }
}
