package org.microbule.metrics.decorator;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.codahale.metrics.MetricRegistry;

@Provider
@PreMatching
public class MetricsRequestFilter implements ContainerRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final MetricRegistry metricRegistry;
    private final String namePrefix;
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MetricsRequestFilter(MetricRegistry metricRegistry, String namePrefix) {
        this.metricRegistry = metricRegistry;
        this.namePrefix = namePrefix;
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("GET".equals(requestContext.getMethod()) && requestContext.getUriInfo().getQueryParameters().containsKey("_metrics")) {
            MetricsResponse metricsResponse = new MetricsResponse(metricRegistry.getTimers((name, metric) -> name.startsWith(namePrefix)));
            requestContext.abortWith(Response.ok(metricsResponse).type(MediaType.APPLICATION_JSON_TYPE).build());
        }
    }
}
