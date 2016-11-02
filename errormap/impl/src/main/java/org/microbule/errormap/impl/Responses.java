package org.microbule.errormap.impl;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

public class Responses {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static String getErrorMessage(Response response) {
        if (response.hasEntity()) {
            return StringUtils.defaultString(response.readEntity(String.class), response.getStatusInfo().getReasonPhrase());
        }
        return response.getStatusInfo().getReasonPhrase();
    }
}
