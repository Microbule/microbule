package org.microbule.metrics.decorator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.apache.commons.lang3.StringUtils;
import org.microbule.config.api.Config;
import org.microbule.metrics.annotation.Timed;
import org.microbule.metrics.api.MetricsService;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.util.jaxrs.AnnotationDrivenDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class MetricsFeature extends AnnotationDrivenDynamicFeature<Timed> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsFeature.class);

    private final MetricsService metricsService;
    private final Config config;
    private final JaxrsServiceDescriptor descriptor;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    MetricsFeature(MetricsService metricsService, JaxrsServiceDescriptor descriptor, Config config) {
        this.metricsService = metricsService;
        this.descriptor = descriptor;
        this.config = config;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configure(Timed annotation, ResourceInfo resourceInfo, FeatureContext featureContext) {
        final Method method = resourceInfo.getResourceMethod();
        final String name = MetricRegistry.name(descriptor.serviceInterface().getSimpleName(), StringUtils.defaultIfBlank(annotation.name(), method.getName()));
        final Timer timer = metricsService.createTimer(name, annotation.strategy(), config);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Adding \"{}\" timer using strategy \"{}\" filter to method {}.{}({})...", name, annotation.strategy(), descriptor.serviceInterface().getSimpleName(), method.getName(), Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")));
        }
        featureContext.register(new MetricsFilter(timer));
    }
}
