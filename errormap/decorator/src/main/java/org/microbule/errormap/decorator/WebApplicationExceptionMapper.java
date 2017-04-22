package org.microbule.errormap.decorator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.microbule.errormap.api.ErrorMapperService;

@Provider
public class WebApplicationExceptionMapper extends ErrorMapperExceptionMapper<WebApplicationException> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public WebApplicationExceptionMapper(ErrorMapperService errorMapperService) {
        super(errorMapperService);
    }
}
