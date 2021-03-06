package org.microbule.metrics.decorator;

import javax.ws.rs.container.ContainerRequestContext;

import com.codahale.metrics.MetricRegistry;
import org.microbule.util.jaxrs.ExtensionRequestFilter;

public class MetricsRequestFilter extends ExtensionRequestFilter<MetricsResponse> {
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
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected MetricsResponse createResponse(ContainerRequestContext requestContext) {
        return new MetricsResponse(metricRegistry.getTimers((name, metric) -> name.startsWith(namePrefix)));
    }

    @Override
    protected String markerParam() {
        return "_metrics";
    }
}
