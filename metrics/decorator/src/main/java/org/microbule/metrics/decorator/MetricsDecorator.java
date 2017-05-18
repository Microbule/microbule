package org.microbule.metrics.decorator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.metrics.api.MetricsService;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named("metricsDecorator")
@Singleton
public class MetricsDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final MetricsService metricsService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public MetricsDecorator(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new MetricsFeature(metricsService, descriptor, config));
    }

    @Override
    public String name() {
        return "metrics";
    }
}
