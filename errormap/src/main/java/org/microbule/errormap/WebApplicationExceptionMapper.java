package org.microbule.errormap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper extends ErrorMapperExceptionMapper<WebApplicationException> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public WebApplicationExceptionMapper(ErrorMapperService errorMapperService) {
        super(errorMapperService);
    }
}
