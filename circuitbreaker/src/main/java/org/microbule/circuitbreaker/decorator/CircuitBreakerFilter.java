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

package org.microbule.circuitbreaker.decorator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.concurrent.CircuitBreaker;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServiceDescriptor;

@Provider
public class CircuitBreakerFilter implements ClientRequestFilter, ClientResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String OPEN_THRESHOLD_PROP = "openThreshold";
    private static final String OPEN_INTERVAL_PROP = "openInterval";
    private static final String OPEN_INTERVAL_UNIT_PROP = "openIntervalUnit";

    private static final String CLOSE_THRESHOLD_PROP = "closeThreshold";
    private static final String CLOSE_INTERVAL_PROP = "closeInterval";
    private static final String CLOSE_INTERVAL_UNIT_PROP = "closeIntervalUnit";

    private static final int DEFAULT_OPEN_THRESHOLD = 10;
    private static final int DEFUALT_CLOSE_THRESHOLD = 10;
    private static final long DEFAULT_OPEN_INTERVAL = 1L;
    private static final long DEFAULT_CLOSE_INTERVAL = 1L;
    private static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;

    private final CircuitBreaker<Integer> circuitBreaker;
    private final String serviceInterface;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    CircuitBreakerFilter(JaxrsServiceDescriptor descriptor, Config config) {
        this.serviceInterface = descriptor.serviceInterface().getSimpleName();

        final int openThreshold = config.integerValue(OPEN_THRESHOLD_PROP).orElse(DEFAULT_OPEN_THRESHOLD);
        final long openInterval = config.longValue(OPEN_INTERVAL_PROP).orElse(DEFAULT_OPEN_INTERVAL);
        final TimeUnit openIntervalUnit = config.enumValue(OPEN_INTERVAL_UNIT_PROP, TimeUnit.class).orElse(DEFAULT_UNIT);

        final int closeThreshold = config.integerValue(CLOSE_THRESHOLD_PROP).orElse(DEFUALT_CLOSE_THRESHOLD);
        final long closeInterval = config.longValue(CLOSE_INTERVAL_PROP).orElse(DEFAULT_CLOSE_INTERVAL);
        final TimeUnit closeIntervalUnit = config.enumValue(CLOSE_INTERVAL_UNIT_PROP, TimeUnit.class).orElse(DEFAULT_UNIT);

        this.circuitBreaker = new EventCountCircuitBreaker(openThreshold, openInterval, openIntervalUnit, closeThreshold, closeInterval, closeIntervalUnit);
    }

//----------------------------------------------------------------------------------------------------------------------
// ClientRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (!circuitBreaker.checkState()) {
            requestContext.abortWith(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(String.format("Service %s is currently unavilable.", serviceInterface)).build());
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// ClientResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        if (Response.Status.Family.SERVER_ERROR == responseContext.getStatusInfo().getFamily()) {
            circuitBreaker.incrementAndCheckState(1);
        }
    }
}
