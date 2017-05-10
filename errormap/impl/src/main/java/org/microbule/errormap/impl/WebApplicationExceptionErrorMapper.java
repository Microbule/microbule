/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.errormap.impl;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.microbule.errormap.spi.TypedErrorMapper;

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
            return Collections.singletonList(Responses.getErrorMessage(response));
        } else {
            return Collections.singletonList(exception.getMessage());
        }
    }

    @Override
    protected Response.StatusType doGetStatus(WebApplicationException exception) {
        return exception.getResponse().getStatusInfo();
    }
}
