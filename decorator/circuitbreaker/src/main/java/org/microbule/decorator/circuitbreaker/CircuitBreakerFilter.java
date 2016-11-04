package org.microbule.decorator.circuitbreaker;

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
import org.microbule.spi.JaxrsProxyConfig;

@Provider
public class CircuitBreakerFilter implements ClientRequestFilter, ClientResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String OPEN_THRESHOLD_PROP = "microbule.circuitbreaker.openThreshold";
    public static final String OPEN_INTERVAL_PROP = "microbule.circuitbreaker.openInterval";
    public static final String OPEN_INTERVAL_UNIT_PROP = "microbule.circuitbreaker.openIntervalUnit";

    public static final String CLOSE_THRESHOLD_PROP = "microbule.circuitbreaker.closeThreshold";
    public static final String CLOSE_INTERVAL_PROP = "microbule.circuitbreaker.closeInterval";
    public static final String CLOSE_INTERVAL_UNIT_PROP = "microbule.circuitbreaker.closeIntervalUnit";

    public static final int DEFAULT_OPEN_THRESHOLD = 10;
    public static final int DEFUALT_CLOSE_THRESHOLD = 10;
    public static final long DEFAULT_OPEN_INTERVAL = 1L;
    public static final long DEFAULT_CLOSE_INTERVAL = 1L;
    public static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;

    private final CircuitBreaker<Integer> circuitBreaker;
    private final String serviceInterface;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CircuitBreakerFilter(JaxrsProxyConfig proxyConfig) {
        this.serviceInterface = proxyConfig.getServiceInterface().getSimpleName();

        final int openThreshold = proxyConfig.getProperty(OPEN_THRESHOLD_PROP, Integer::parseInt, DEFAULT_OPEN_THRESHOLD);
        final long openInterval = proxyConfig.getProperty(OPEN_INTERVAL_PROP, Long::parseLong, DEFAULT_OPEN_INTERVAL);
        final TimeUnit openIntervalUnit = proxyConfig.getProperty(OPEN_INTERVAL_UNIT_PROP, TimeUnit::valueOf, DEFAULT_UNIT);

        final int closeThreshold = proxyConfig.getProperty(CLOSE_THRESHOLD_PROP, Integer::parseInt, DEFUALT_CLOSE_THRESHOLD);
        final long closeInterval = proxyConfig.getProperty(CLOSE_INTERVAL_PROP, Long::parseLong, DEFAULT_CLOSE_INTERVAL);
        final TimeUnit closeIntervalUnit = proxyConfig.getProperty(CLOSE_INTERVAL_UNIT_PROP, TimeUnit::valueOf, DEFAULT_UNIT);

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
