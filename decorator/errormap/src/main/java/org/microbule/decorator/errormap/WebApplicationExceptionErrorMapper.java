package org.microbule.decorator.errormap;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.microbule.spi.error.TypedErrorMapper;

public class WebApplicationExceptionErrorMapper extends TypedErrorMapper<WebApplicationException> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public WebApplicationExceptionErrorMapper() {
        super(WebApplicationException.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected List<String> doGetErrorMessages(WebApplicationException exception) {
        Response response = exception.getResponse();
        if (StringUtils.isBlank(exception.getMessage())) {
            return Collections.singletonList(getErrorMessage(response));
        } else {
            return Collections.singletonList(exception.getMessage());
        }
    }

    private String getErrorMessage(Response response) {
        if (response.hasEntity()) {
            return StringUtils.defaultString(response.readEntity(String.class), response.getStatusInfo().getReasonPhrase());
        }
        return response.getStatusInfo().getReasonPhrase();
    }

    @Override
    protected Response.StatusType doGetStatus(WebApplicationException exception) {
        return exception.getResponse().getStatusInfo();
    }
}
