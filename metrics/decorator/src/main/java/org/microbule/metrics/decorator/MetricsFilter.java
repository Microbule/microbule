package org.microbule.metrics.decorator;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.codahale.metrics.Timer;

@Provider
@Priority(Priorities.USER - 200)
public class MetricsFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String CONTEXT_PROP = MetricsFilter.class.getSimpleName() + "." + Timer.Context.class.getSimpleName();
    private final Timer timer;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    MetricsFilter(Timer timer) {
        this.timer = timer;
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(CONTEXT_PROP, timer.time());
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final Timer.Context context = (Timer.Context) requestContext.getProperty(CONTEXT_PROP);
        if (context != null && Response.Status.Family.SUCCESSFUL.equals(responseContext.getStatusInfo().getFamily())) {
            context.stop();
        }
    }
}