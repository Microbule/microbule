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

package org.microbule.requestlog.decorator;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Provider
@Priority(Priorities.USER - 100)
public class RequestLogFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLogFilter.class);

    private static final String BEGIN_TS_PROP = RequestLogFilter.class.getCanonicalName() + ".beginTs";

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(BEGIN_TS_PROP, System.nanoTime());
        LOGGER.info("BEGIN {} {}", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().getPath());
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final Long beginTs = (Long) requestContext.getProperty(BEGIN_TS_PROP);
        final long elapsedNanos = System.nanoTime() - beginTs;
        final Response.StatusType statusInfo = responseContext.getStatusInfo();
        LOGGER.info("END   {} {} - {} {} ({} sec)", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().getPath(), statusInfo.getStatusCode(), statusInfo.getReasonPhrase(), DurationFormatUtils.formatDuration(NANOSECONDS.toMillis(elapsedNanos), "s.SSS"));
    }
}
